package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrderRequestDTO {
    @NotNull(message = "Customer id is required")
    private Long customerId;

    @Future
    private LocalDateTime expectedDeliveryDate;

    @Size(min = 3, max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;

    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.00", message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    @Size(min = 3, max = 200, message = "Note cannot be longer than 200 characters")
    private String note;

    @NotNull(message = "Payment method is required")
    @ValidEnum(enumClass = PaymentMethod.class, message = "Invalid payment method")
    private PaymentMethod paymentMethod;

    @NotNull(message = "Shipping address id is required")
    private Long shippingAddressId;

    @NotNull(message = "Billing address id is required")
    private Long billingAddressId;
}
