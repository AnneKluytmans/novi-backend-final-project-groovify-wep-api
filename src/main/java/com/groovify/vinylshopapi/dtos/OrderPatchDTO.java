package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class OrderPatchDTO {
    @ValidDate(max = "now+9M", mustBeFuture = true, message = "Expected delivery date must be between now and nine months from now")
    private LocalDate expectedDeliveryDate;

    @Size(min = 3, max = 100, message = "Recipient name cannot exceed 100 characters")
    private String recipientName;

    @DecimalMin(value = "0.00", message = "Shipping cost must be positive")
    private BigDecimal shippingCost;

    @Size(min = 3, max = 200, message = "Note cannot be longer than 200 characters")
    private String note;

    @ValidEnum(enumClass = PaymentMethod.class, message = "Invalid payment method")
    private PaymentMethod paymentMethod;

    private Long shippingAddressId;

    private Long billingAddressId;
}
