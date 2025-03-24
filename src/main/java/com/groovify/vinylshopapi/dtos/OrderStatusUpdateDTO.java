package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import com.groovify.vinylshopapi.validation.ValidEnum;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    @ValidEnum(enumClass = ConfirmationStatus.class, message = "Invalid confirmation status")
    private ConfirmationStatus confirmationStatus;

    @ValidEnum(enumClass = PaymentStatus.class, message = "Invalid payment status")
    private PaymentStatus paymentStatus;

    @ValidEnum(enumClass = ShippingStatus.class, message = "Invalid shipping status")
    private ShippingStatus shippingStatus;
}
