package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.OrderStatus;
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
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private String email;
}
