package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.OrderStatus;
import com.groovify.vinylshopapi.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponseDTO {
    private Long id;
    private LocalDateTime orderDate;
    private LocalDate expectedDeliveryDate;
    private BigDecimal totalPrice;
    private BigDecimal shippingCost;
    private BigDecimal fixedDiscountAmount;
    private BigDecimal percentageDiscount;
    private OrderStatus orderStatus;
    private PaymentMethod paymentMethod;
    private String email;
    private String recipientName;
    private String note;
    private AddressSummaryResponseDTO shippingAddress;
    private AddressSummaryResponseDTO billingAddress;
    private UserSummaryResponseDTO customer;
    private List<OrderItemResponseDTO> orderItems;
}
