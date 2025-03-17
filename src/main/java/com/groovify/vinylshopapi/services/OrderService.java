package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.OrderPatchDTO;
import com.groovify.vinylshopapi.dtos.OrderRequestDTO;
import com.groovify.vinylshopapi.dtos.OrderResponseDTO;
import com.groovify.vinylshopapi.enums.OrderStatus;
import com.groovify.vinylshopapi.exceptions.ConflictException;
import com.groovify.vinylshopapi.exceptions.RecordNotFoundException;
import com.groovify.vinylshopapi.exceptions.TeapotException;
import com.groovify.vinylshopapi.mappers.OrderMapper;
import com.groovify.vinylshopapi.models.*;
import com.groovify.vinylshopapi.repositories.AddressRepository;
import com.groovify.vinylshopapi.repositories.CustomerRepository;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.repositories.VinylRecordRepository;
import com.groovify.vinylshopapi.validation.ValidationUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class OrderService {
    private final CustomerRepository customerRepository;
    private final AddressRepository addressRepository;
    private final VinylRecordRepository vinylRecordRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(
            CustomerRepository customerRepository,
            AddressRepository addressRepository,
            VinylRecordRepository vinylRecordRepository,
            OrderRepository orderRepository,
            OrderMapper orderMapper
    ) {
        this.customerRepository = customerRepository;
        this.addressRepository = addressRepository;
        this.vinylRecordRepository = vinylRecordRepository;
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public OrderResponseDTO placeOrder(OrderRequestDTO orderRequestDTO) {
        Customer customer = findCustomer(orderRequestDTO.getCustomerId());
        Cart cart = customer.getCart();

        if (cart == null || cart.getCartItems().isEmpty()) {
            throw new TeapotException("Oops! Your cart is empty, so you can't place an order. Even a teapot needs water to brew any tea.");
        }

        Address shippingAddress = validateAddress(orderRequestDTO.getShippingAddressId(), customer);
        Address billingAddress = validateAddress(orderRequestDTO.getBillingAddressId(), customer);

        Order order = orderMapper.toEntity(orderRequestDTO);

        if (orderRequestDTO.getRecipientName() == null) {
            order.setRecipientName(customer.getFirstName() + " " + customer.getLastName());
        }

        order.setOrderStatus(OrderStatus.PENDING);
        order.setCustomer(customer);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getCartItems()) {
            VinylRecord vinylRecord = cartItem.getVinylRecord();
            Integer quantity = cartItem.getQuantity();

            decreaseStockAndIncreaseSales(vinylRecord, quantity);

            OrderItem orderItem = new OrderItem();
            orderItem.setQuantity(quantity);
            orderItem.setPriceAtPurchase(vinylRecord.getPrice());
            orderItem.setVinylRecord(vinylRecord);
            orderItem.setOrder(order);

            order.getOrderItems().add(orderItem);

            totalPrice = totalPrice.add(vinylRecord.getPrice().multiply(new BigDecimal(quantity)));
        }

        totalPrice = totalPrice.add(order.getShippingCost());
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponseDTO(savedOrder);
    }

    public OrderResponseDTO updatePendingOrder(Long orderId, OrderPatchDTO orderPatchDTO) {
        Order order = findOrder(orderId);

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new ConflictException("You can only update orders with a PENDING status.");
        }

        if (orderPatchDTO.getShippingAddressId() != null) {
            Address shippingAddress = validateAddress(orderPatchDTO.getShippingAddressId(), order.getCustomer());
            order.setShippingAddress(shippingAddress);
        }

        if (orderPatchDTO.getBillingAddressId() != null) {
            Address billingAddress = validateAddress(orderPatchDTO.getBillingAddressId(), order.getCustomer());
            order.setBillingAddress(billingAddress);
        }

        orderMapper.partialUpdateOrder(orderPatchDTO, order);

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

    private void decreaseStockAndIncreaseSales(VinylRecord vinylRecord, Integer quantity) {
        ValidationUtils.validateVinylRecordStock(vinylRecord, quantity);

        VinylRecordStock stock = vinylRecord.getStock();
        stock.setAmountInStock(stock.getAmountInStock() - quantity);
        stock.setAmountSold(stock.getAmountSold() + quantity);

        vinylRecordRepository.save(vinylRecord);
    }
}
