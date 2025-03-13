package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.DiscountCategory;
import com.groovify.vinylshopapi.enums.DiscountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCodeResponseDTO {
    private Long id;
    private String code;
    private String description;
    private DiscountType discountType;
    private Double discountValue;
    private BigDecimal minimumOrderPrice;
    private DiscountCategory discountCategory;
    private LocalDate startDate;
    private LocalDate endDate;
}
