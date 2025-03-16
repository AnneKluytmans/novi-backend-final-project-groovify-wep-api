package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderRequestDTO {
    @NotNull(message = "Customer id is required")
    private Long customerId;

    @ValidDate(max = "now+9M", mustBeFuture = true, message = "Expected delivery date must be between now and nine months from now")
    private LocalDate expectedDeliveryDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Recipient name is required")
    @Size(max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;

    @NotNull(message = "Shipping cost is required")
    @DecimalMin(value = "0.00", message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    @DecimalMin(value = "0.00", message = "Discount amount must be positive")
    @DecimalMax(value = "2000.00", message = "Discount amount cannot exceed 1000,-")
    private BigDecimal fixedDiscountAmount;

    @DecimalMin(value = "0.00", message = "Discount percentage must be at least 0%")
    @DecimalMax(value = "100.00", message = "Discount percentage cannot exceed 100%")
    private BigDecimal percentageDiscount;

    @Size(max = 200, message = "Note cannot be longer than 200 characters")
    private String note;

    @NotNull(message = "Payment method is required")
    @ValidEnum(enumClass = PaymentMethod.class, message = "Invalid payment method")
    private PaymentMethod paymentMethod;

    private Long shippingAddressId;
    private AddressRequestDTO shippingAddress;

    private Long billingAddressId;
    private AddressRequestDTO billingAddress;
}
