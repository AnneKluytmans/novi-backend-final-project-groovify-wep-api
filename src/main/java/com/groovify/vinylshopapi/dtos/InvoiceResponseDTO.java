package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceResponseDTO {
    private Long id;
    private LocalDateTime invoiceDate;

    private UserSummaryResponseDTO customer;

    private Long orderId;
    private LocalDateTime orderDate;
    private List<OrderItemResponseDTO> orderItems;

    private PaymentStatus paymentStatus;
    private BigDecimal subTotalPrice;
    private BigDecimal shippingCost;
    private BigDecimal totalPrice;

    private AddressResponseDTO shippingAddress;
    private AddressResponseDTO billingAddress;
}
