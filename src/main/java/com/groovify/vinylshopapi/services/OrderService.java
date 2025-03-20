package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.OrderPatchDTO;
import com.groovify.vinylshopapi.dtos.OrderRequestDTO;
import com.groovify.vinylshopapi.dtos.OrderResponseDTO;
import com.groovify.vinylshopapi.dtos.OrderStatusUpdateDTO;
import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.InvalidOrderStatusException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.exceptions.TeapotException;
import com.groovify.vinylshopapi.mappers.OrderMapper;
import com.groovify.vinylshopapi.models.*;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final CustomerCartService customerCartService;

    public OrderService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            OrderRepository orderRepository,
            OrderMapper orderMapper,
            CustomerCartService customerCartService
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
        this.customerCartService = customerCartService;
    }

    private static final Map<ConfirmationStatus, Set<ConfirmationStatus>> VALID_CONFIRMATION_TRANSITIONS = Map.of(
            ConfirmationStatus.PENDING, Set.of(ConfirmationStatus.CONFIRMED, ConfirmationStatus.CANCELLED)
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

    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = findCustomer(orderRequestDTO.getCustomerId());
        validateNoPendingOrders(customer);

        Cart cart = customer.getCart();
        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new TeapotException("Oops! Your cart is empty, so you can't place an order. Even a teapot needs water to brew any tea.");
        }

        Order order = orderMapper.toEntity(orderRequestDTO);
        setOrderDefaults(order, customer, orderRequestDTO.getRecipientName(),
                validateAddress(orderRequestDTO.getShippingAddressId(), customer),
                validateAddress(orderRequestDTO.getBillingAddressId(), customer));

        BigDecimal totalPrice = processOrderItems(order, cart).add(order.getShippingCost());
        order.setTotalPrice(totalPrice);

        customerCartService.clearCart(customer.getId());

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO updatePendingOrder(Long orderId, OrderPatchDTO orderPatchDTO) {
        Order order = findOrder(orderId);

        if (order.getConfirmationStatus() != ConfirmationStatus.PENDING) {
            throw new ConflictException("You can only update orders with a PENDING confirmation status.");
        }

        if (orderPatchDTO.getShippingAddressId() != null) {
            order.setShippingAddress(validateAddress(orderPatchDTO.getShippingAddressId(), order.getCustomer()));
        }

        if (orderPatchDTO.getBillingAddressId() != null) {
            order.setBillingAddress(validateAddress(orderPatchDTO.getBillingAddressId(), order.getCustomer()));
        }

        orderMapper.partialUpdateOrder(orderPatchDTO, order);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO updateOrderStatuses(Long orderId, OrderStatusUpdateDTO orderStatusUpdateDTO) {
        Order order = findOrder(orderId);

        validateStatusUpdates(order, orderStatusUpdateDTO);
        applyStatusChanges(order, orderStatusUpdateDTO);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }


    private Customer findCustomer(Long customerId) {
        return customerRepository.findByIdAndIsDeletedFalse(customerId)
                .orElseThrow(() -> new RecordNotFoundException("Customer with id " + customerId + " not found"));
    }

    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RecordNotFoundException("Order with id " + orderId + " not found"));
    }

    private Address validateAddress(Long addressId, Customer customer) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RecordNotFoundException("Address with id " + addressId + " not found"));

        if (address.getEmployee() != null || (address.getCustomer() != null && !address.getCustomer().equals(customer))) {
            throw new IllegalArgumentException("Address with id " + addressId + " doesn't belong to this customer");
        }
        return address;
    }

    private void validateNoPendingOrders(Customer customer) {
        boolean alreadyPendingOrder = customer.getOrders().stream()
                .anyMatch(order -> order.getConfirmationStatus() == ConfirmationStatus.PENDING);

        if (alreadyPendingOrder) {
            throw new ConflictException("Customer already has an order with the status PENDING. " +
                    "You'll have to cancel or confirm that order first before placing a new order.");
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
        validateStatusTransition(order.getConfirmationStatus(), statusDTO.getConfirmationStatus(), VALID_CONFIRMATION_TRANSITIONS);
        validateStatusTransition(order.getPaymentStatus(), statusDTO.getPaymentStatus(), VALID_PAYMENT_TRANSITIONS);
        validateStatusTransition(order.getShippingStatus(), statusDTO.getShippingStatus(), VALID_SHIPPING_TRANSITIONS);

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
        if (statusDTO.getConfirmationStatus() == ConfirmationStatus.CANCELLED) {
            restoreStock(order);
        }

        if (statusDTO.getConfirmationStatus() == ConfirmationStatus.CONFIRMED) {
            order.setOrderDate(LocalDateTime.now());
            increaseSales(order);
        }

        if (statusDTO.getPaymentStatus() == PaymentStatus.PAID) {
            String createInvoice = "Here comes a method that creates a new invoice.";
        }

        if (statusDTO.getShippingStatus() != null) {
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

        if (statusDTO.getShippingStatus() != null) {
            order.setConfirmationStatus(statusDTO.getConfirmationStatus());
        }
        if (statusDTO.getPaymentStatus() != null) {
            order.setPaymentStatus(statusDTO.getPaymentStatus());
        }
        if (statusDTO.getShippingStatus() != null) {
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
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            VinylRecord vinylRecord = cartItem.getVinylRecord();
            decreaseStock(vinylRecord, cartItem.getQuantity());

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPriceAtPurchase(vinylRecord.getPrice());
            orderItem.setVinylRecord(vinylRecord);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);
            totalPrice = totalPrice.add(vinylRecord.getPrice().multiply(new BigDecimal(cartItem.getQuantity())));
        }

        return totalPrice;
    }
}
