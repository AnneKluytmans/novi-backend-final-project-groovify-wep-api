package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.OrderStatus;
import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order date is required")
    @ValidDate(min = "now-2M", message = "Order date must be between now and two months ago")
    private LocalDate orderDate;

    @ValidDate(max = "now+9M", mustBeFuture = true, message = "Expected delivery date must be between now and nine months from now")
    private LocalDate expectedDeliveryDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Recipient name is required")
    @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be positive")
    private BigDecimal totalPrice;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.00", inclusive = false, message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    @Size(max = 200, message = "Note cannot be longer than 200 characters")
    private String note;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment method is required")
    @ValidEnum(enumClass = PaymentMethod.class, message = "Invalid payment method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Order status is required")
    @ValidEnum(enumClass = OrderStatus.class, message = "Invalid order status")
    private OrderStatus orderStatus;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id", nullable = false)
    private Address shippingAddress;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", nullable = false)
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();
}
