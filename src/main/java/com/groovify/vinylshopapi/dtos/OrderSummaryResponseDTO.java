package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummaryResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private BigDecimal subTotalPrice;
    private ConfirmationStatus confirmationStatus;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
    private UserSummaryResponseDTO customer;
}
