package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    private LocalDateTime expectedDeliveryDate;

    @NotBlank(message = "Recipient name is required")
    @Size(max = 200, message = "Recipient name cannot exceed 200 characters")
    private String recipientName;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Total price is required")
    @DecimalMin(value = "0.00", message = "Price must be positive")
    private BigDecimal subTotalPrice;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.00", message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    @Size(max = 200, message = "Note cannot be longer than 200 characters")
    private String note;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment method is required")
    @ValidEnum(enumClass = PaymentMethod.class, message = "Invalid payment method")
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Confirmation status is required")
    @ValidEnum(enumClass = ConfirmationStatus.class, message = "Invalid confirmation status")
    private ConfirmationStatus confirmationStatus;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment status is required")
    @ValidEnum(enumClass = PaymentStatus.class, message = "Invalid payment status")
    private PaymentStatus paymentStatus;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Shipping status is required")
    @ValidEnum(enumClass = ShippingStatus.class, message = "Invalid shipping status")
    private ShippingStatus shippingStatus;

    @ManyToOne()
    @JoinColumn(name = "shipping_address_id", referencedColumnName = "id", nullable = false)
    private Address shippingAddress;

    @ManyToOne()
    @JoinColumn(name = "billing_address_id", referencedColumnName = "id", nullable = false)
    private Address billingAddress;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Invoice invoice;

    private Boolean isDeleted = false;

    private LocalDateTime deletedAt = null;
}
