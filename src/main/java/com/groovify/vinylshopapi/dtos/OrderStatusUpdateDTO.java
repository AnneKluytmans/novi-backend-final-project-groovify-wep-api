package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    private ConfirmationStatus confirmationStatus;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
}
