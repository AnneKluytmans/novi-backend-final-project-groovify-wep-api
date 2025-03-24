package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import com.groovify.vinylshopapi.exceptions.*;
import com.groovify.vinylshopapi.mappers.InvoiceMapper;
import com.groovify.vinylshopapi.mappers.OrderMapper;
import com.groovify.vinylshopapi.models.*;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.specifications.OrderSpecification;
import com.groovify.vinylshopapi.utils.SortHelper;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerCartService customerCartService;
    private final InvoiceMapper invoiceMapper;

    public OrderService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            CustomerCartService customerCartService,
            InvoiceMapper invoiceMapper
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerCartService = customerCartService;
        this.invoiceMapper = invoiceMapper;
    }

    private static final Map<ConfirmationStatus, Set<ConfirmationStatus>> VALID_CONFIRMATION_TRANSITIONS = Map.of(
            ConfirmationStatus.PENDING, Set.of(ConfirmationStatus.CONFIRMED)
    );

    private static final Map<PaymentStatus, Set<PaymentStatus>> VALID_PAYMENT_TRANSITIONS = Map.of(
            PaymentStatus.NOT_APPLICABLE, Set.of(PaymentStatus.AWAITING_PAYMENT, PaymentStatus.PAID, PaymentStatus.FAILED),
            PaymentStatus.AWAITING_PAYMENT, Set.of(PaymentStatus.FAILED, PaymentStatus.PAID),
            PaymentStatus.PAID, Set.of(PaymentStatus.AWAITING_REFUND, PaymentStatus.REFUNDED),
            PaymentStatus.AWAITING_REFUND, Set.of(PaymentStatus.REFUNDED),
            PaymentStatus.FAILED, Set.of(PaymentStatus.PAID, PaymentStatus.FAILED)
    );

    private static final Map<ShippingStatus, Set<ShippingStatus>> VALID_SHIPPING_TRANSITIONS = Map.of(
            ShippingStatus.NOT_APPLICABLE, Set.of(ShippingStatus.PROCESSING),
            ShippingStatus.PROCESSING, Set.of(ShippingStatus.PROCESSED),
            ShippingStatus.PROCESSED, Set.of(ShippingStatus.SHIPPED),
            ShippingStatus.SHIPPED, Set.of(ShippingStatus.DELIVERED, ShippingStatus.LOST),
            ShippingStatus.DELIVERED, Set.of(ShippingStatus.LOST, ShippingStatus.DAMAGED, ShippingStatus.RETURN_REQUESTED),
            ShippingStatus.RETURN_REQUESTED, Set.of(ShippingStatus.RETURN_DECLINED, ShippingStatus.IN_RETURN),
            ShippingStatus.IN_RETURN, Set.of(ShippingStatus.LOST, ShippingStatus.RETURN_DECLINED, ShippingStatus.RETURNED)
    );

    public List<OrderSummaryResponseDTO> getOrders(
            String confirmationStatus,
            String paymentStatus,
            String shippingStatus,
            LocalDate orderedBefore,
            LocalDate orderedAfter,
            BigDecimal minTotalPrice,
            BigDecimal maxTotalPrice,
            Boolean isDeleted,
            LocalDate deletedAfter,
            LocalDate deletedBefore,
            String sortBy,
            String sortOrder
    ) {
        Sort sort = SortHelper.getSort(sortBy, sortOrder, List.of("id", "subTotalPrice", "orderDate"));
        Specification<Order> specification = OrderSpecification.filterOrders(
                confirmationStatus, paymentStatus, shippingStatus, orderedBefore, orderedAfter,
                minTotalPrice, maxTotalPrice, isDeleted, deletedAfter, deletedBefore
        );
        List<Order> orders = orderRepository.findAll(specification, sort);
        return orderMapper.toOrderSummaryResponseDTOs(orders);
    }

    public OrderResponseDTO getOrderById(Long orderId) {
        return orderMapper.toResponseDTO(findOrder(orderId, null));
    }

    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = findCustomer(orderRequestDTO.getCustomerId());
        validateNoExistingPendingOrder(customer);

        Cart cart = customer.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new TeapotException("Oops! Your cart is empty, so you can't place an order. Even a teapot needs water to brew any tea.");
        }

        Order order = orderMapper.toEntity(orderRequestDTO);
        setOrderDefaults(order, customer, orderRequestDTO.getRecipientName(),
                validateAddress(orderRequestDTO.getShippingAddressId(), customer),
                validateAddress(orderRequestDTO.getBillingAddressId(), customer));

        order.setSubTotalPrice(processOrderItems(order, cart));

        customerCartService.clearCart(customer.getId());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO updatePendingOrder(Long orderId, OrderPatchDTO orderPatchDTO) {
        Order order = findOrder(orderId, false);

        if (order.getConfirmationStatus() != ConfirmationStatus.PENDING) {
            throw new ConflictException("You can only update orders with a PENDING confirmation status.");
        }

        Address oldShippingAddress = order.getShippingAddress();
        Address oldBillingAddress = order.getBillingAddress();

        if (orderPatchDTO.getShippingAddressId() != null) {
            order.setShippingAddress(validateAddress(orderPatchDTO.getShippingAddressId(), order.getCustomer()));
        }

        if (orderPatchDTO.getBillingAddressId() != null) {
            order.setBillingAddress(validateAddress(orderPatchDTO.getBillingAddressId(), order.getCustomer()));
        }

        orderMapper.partialUpdateOrder(orderPatchDTO, order);

        Order savedOrder = orderRepository.save(order);
        deleteIfStandAloneAddress(oldShippingAddress, oldBillingAddress);

        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO updateOrderStatuses(Long orderId, OrderStatusUpdateDTO orderStatusUpdateDTO) {
        Order order = findOrder(orderId, false);

        validateStatusUpdates(order, orderStatusUpdateDTO);
        applyStatusChanges(order, orderStatusUpdateDTO);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public void cancelOrder(Long orderId) {
        Order order = findOrder(orderId, false);

        if (order.getConfirmationStatus() != ConfirmationStatus.PENDING) {
            throw new ConflictException("Only pending orders can be cancelled.");
        }

        Address shippingAddress = order.getShippingAddress();
        Address billingAddress = order.getBillingAddress();

        restoreStock(order);

        orderRepository.delete(order);
        deleteIfStandAloneAddress(shippingAddress, billingAddress);
    }

    public void deactivateOrder(Long orderId) {
        Order order = findOrder(orderId, false);

        if (order.getConfirmationStatus() != ConfirmationStatus.CONFIRMED) {
            throw new DeleteOperationException("You can only deactivate orders with a CONFIRMED confirmation status.");
        }
        order.setIsDeleted(true);
        order.setDeletedAt(LocalDateTime.now());

        orderRepository.save(order);
    }

    public void reactivateOrder(Long orderId) {
        Order order = findOrder(orderId, true);

        order.setIsDeleted(false);
        order.setDeletedAt(null);

        orderRepository.save(order);
    }

    public InvoiceResponseDTO getInvoiceByOrder(Long orderId) {
        Order order = findOrder(orderId, false);
        if (order.getInvoice() == null) {
            throw new RecordNotFoundException("No invoice found for order with id: " + orderId);
        }
        return invoiceMapper.toResponseDTO(order.getInvoice());
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("No customer found with id: " + customerId));
    }

    private Order findOrder(Long orderId, Boolean isDeleted) {
        if (isDeleted != null) {
            if (isDeleted) {
                return orderRepository.findByIdAndIsDeletedTrue(orderId)
                        .orElseThrow(() -> new RecordNotFoundException("No deactivated order found with id: " + orderId));
            } else {
                return orderRepository.findByIdAndIsDeletedFalse(orderId)
                        .orElseThrow(() -> new RecordNotFoundException("No order found with id: " + orderId));
            }
        }

        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotFoundException("No order found with id: " + orderId));
    }

    private Address validateAddress(Long addressId, Customer customer) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("No address found with id: " + addressId));

        if (address.getEmployee() != null || (address.getCustomer() != null && !address.getCustomer().equals(customer))) {
            throw new IllegalArgumentException("Address with id: '" + addressId + "' doesn't belong to this customer");
        }
        return address;
    }

    private void validateNoExistingPendingOrder(Customer customer) {
        boolean existingPendingOrder = customer.getOrders().stream()
                .anyMatch(order -> order.getConfirmationStatus() == ConfirmationStatus.PENDING);

        if (existingPendingOrder) {
            throw new ConflictException("Customer already has an order with the status PENDING. " +
                    "You'll have to cancel or confirm that order before placing a new order.");
        }
    }

    private <T extends Enum<T>> void validateStatusTransition(T currentStatus, T newStatus, Map<T, Set<T>> validTransitions) {
        Set<?> allowedTransitions = validTransitions.getOrDefault(currentStatus, Set.of());
        if (!allowedTransitions.contains(newStatus)) {
            throw new InvalidOrderStatusException("Cannot change status from " + currentStatus + " to " + newStatus +
                    ". Allowed transitions from " + currentStatus + " are: " + allowedTransitions
            );
        }
    }

    private void validateStatusUpdates(Order order, OrderStatusUpdateDTO statusDTO) {
        boolean isNewConfirmStatus = statusDTO.getConfirmationStatus() != order.getConfirmationStatus();
        boolean isNewPaymentStatus = statusDTO.getPaymentStatus() != order.getPaymentStatus();
        boolean isNewShippingStatus = statusDTO.getShippingStatus() != order.getShippingStatus();

        if (statusDTO.getConfirmationStatus() != null && isNewConfirmStatus) {
            validateStatusTransition(order.getConfirmationStatus(), statusDTO.getConfirmationStatus(), VALID_CONFIRMATION_TRANSITIONS);
        }

        if (statusDTO.getPaymentStatus() != null && isNewPaymentStatus) {
            validateStatusTransition(order.getPaymentStatus(), statusDTO.getPaymentStatus(), VALID_PAYMENT_TRANSITIONS);
        }

        if (statusDTO.getShippingStatus() != null && isNewShippingStatus) {
            validateStatusTransition(order.getShippingStatus(), statusDTO.getShippingStatus(), VALID_SHIPPING_TRANSITIONS);
        }

        boolean isNotConfirmed = order.getConfirmationStatus() != ConfirmationStatus.CONFIRMED &&
                                    statusDTO.getConfirmationStatus() != ConfirmationStatus.CONFIRMED;

        boolean hasPaymentOrShippingUpdate = statusDTO.getPaymentStatus() != null ||
                                                statusDTO.getShippingStatus() != null;

        if (isNotConfirmed && hasPaymentOrShippingUpdate) {
            throw new InvalidOrderStatusException("You can only update the payment and shipment status of orders" +
                    " with a CONFIRMED confirmation status.");
        }
    }

    private void applyStatusChanges(Order order, OrderStatusUpdateDTO statusDTO) {
        boolean isNewConfirmStatus = statusDTO.getConfirmationStatus() != order.getConfirmationStatus();
        boolean isNewPaymentStatus = statusDTO.getPaymentStatus() != order.getPaymentStatus();
        boolean isNewShippingStatus = statusDTO.getShippingStatus() != order.getShippingStatus();

        if (statusDTO.getConfirmationStatus() == ConfirmationStatus.CONFIRMED && isNewConfirmStatus) {
            order.setOrderDate(LocalDateTime.now());
            increaseSales(order);
        }

        if (statusDTO.getPaymentStatus() == PaymentStatus.PAID && isNewPaymentStatus) {
            Invoice invoice = new Invoice(order);
            order.setInvoice(invoice);
        }

        if (statusDTO.getShippingStatus() != null && isNewShippingStatus) {
            ShippingStatus newShipping = statusDTO.getShippingStatus();

            if (newShipping == ShippingStatus.LOST || newShipping == ShippingStatus.DAMAGED) {
                restoreSales(order);
            }

            if (newShipping == ShippingStatus.RETURNED) {
                restoreStockAndSales(order);
            }

            boolean isPaid = order.getPaymentStatus() == PaymentStatus.PAID ||
                                statusDTO.getPaymentStatus() == PaymentStatus.PAID;

            boolean applicableForRefund = newShipping == ShippingStatus.LOST ||
                                            newShipping == ShippingStatus.DAMAGED ||
                                            newShipping == ShippingStatus.RETURNED;

            if (isPaid && applicableForRefund) {
                statusDTO.setPaymentStatus(PaymentStatus.AWAITING_REFUND);
            }
        }

        if (statusDTO.getConfirmationStatus() != null && isNewConfirmStatus) {
            order.setConfirmationStatus(statusDTO.getConfirmationStatus());
        }
        if (statusDTO.getPaymentStatus() != null && isNewPaymentStatus) {
            order.setPaymentStatus(statusDTO.getPaymentStatus());
        }
        if (statusDTO.getShippingStatus() != null && isNewShippingStatus) {
            order.setShippingStatus(statusDTO.getShippingStatus());
        }
    }

    private void decreaseStock(VinylRecord vinylRecord, Integer quantity) {
        ValidationUtils.validateVinylRecordStock(vinylRecord, quantity);
        vinylRecord.getStock().decreaseStock(quantity);
    }

    private void increaseSales(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getVinylRecord().getStock().increaseSales(orderItem.getQuantity());
        }
    }

    private void restoreStock(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
           orderItem.getVinylRecord().getStock().increaseStock(orderItem.getQuantity());
        }
    }

    private void restoreSales(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getVinylRecord().getStock().decreaseSales(orderItem.getQuantity());
        }
    }

    private void restoreStockAndSales(Order order) {
        for (OrderItem orderItem : order.getOrderItems()) {
            orderItem.getVinylRecord().getStock().increaseStock(orderItem.getQuantity());
            orderItem.getVinylRecord().getStock().decreaseSales(orderItem.getQuantity());
        }
    }

    private void setOrderDefaults(Order order, Customer customer, String recipientName, Address shippingAddress, Address billingAddress) {
        if (recipientName == null) {
            order.setRecipientName(customer.getFirstName() + " " + customer.getLastName());
        }

        order.setConfirmationStatus(ConfirmationStatus.PENDING);
        order.setPaymentStatus(PaymentStatus.NOT_APPLICABLE);
        order.setShippingStatus(ShippingStatus.NOT_APPLICABLE);
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
    }

    private BigDecimal processOrderItems(Order order, Cart cart) {
        BigDecimal subTotalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            VinylRecord vinylRecord = cartItem.getVinylRecord();
            decreaseStock(vinylRecord, cartItem.getQuantity());

            OrderItem orderItem = new OrderItem(cartItem.getQuantity(), vinylRecord, order);
            order.getOrderItems().add(orderItem);

            subTotalPrice = subTotalPrice.add(vinylRecord.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }

        return subTotalPrice;
    }

    private void deleteIfStandAloneAddress(Address... addresses) {
        for (Address address : addresses) {
            if (address.isStandAlone()) {
                addressRepository.delete(address);
            }
        }
    }
}
