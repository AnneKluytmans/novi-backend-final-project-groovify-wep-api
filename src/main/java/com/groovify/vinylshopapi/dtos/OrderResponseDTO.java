package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.ConfirmationStatus;
import com.groovify.vinylshopapi.enums.PaymentMethod;
import com.groovify.vinylshopapi.enums.PaymentStatus;
import com.groovify.vinylshopapi.enums.ShippingStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private BigDecimal subTotalPrice;
    private BigDecimal shippingCost;
    private ConfirmationStatus confirmationStatus;
    private PaymentStatus paymentStatus;
    private ShippingStatus shippingStatus;
    private PaymentMethod paymentMethod;
    private String recipientName;
    private String note;
    private AddressResponseDTO shippingAddress;
    private AddressResponseDTO billingAddress;
    private UserSummaryResponseDTO customer;
    private List<OrderItemResponseDTO> orderItems;
}
