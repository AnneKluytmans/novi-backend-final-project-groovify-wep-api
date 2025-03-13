package com.groovify.vinylshopapi.dtos;

import com.groovify.vinylshopapi.enums.DiscountCategory;
import com.groovify.vinylshopapi.enums.DiscountType;
import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DiscountCodeRequestDTO {
    @NotNull(message = "Discount code is required")
    @Size(min = 3, max = 20, message = "Discount code must be between 3 and 20 characters")
    private String code;

    @Size(min = 3, max = 250, message = "Description cannot be longer than 250 characters")
    private String description;

    @NotNull(message = "Discount type is required")
    @ValidEnum(enumClass = DiscountType.class, message = "Invalid discount type")
    private DiscountType discountType;

    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    private Double discountValue;

    @DecimalMin(value = "0.01", inclusive = false, message = "Minimal Order Price must be greater than 0")
    private BigDecimal minimumOrderPrice;

    @NotNull(message = "Discount category is required")
    @ValidEnum(enumClass = DiscountCategory.class, message = "Invalid discount category")
    private DiscountCategory discountCategory;

    @ValidDate(mustBeFuture = true, message = "Start date must be in the future")
    private LocalDate startDate;

    @ValidDate(mustBeFuture = true, message = "Start date must be in the future")
    private LocalDate endDate;
}
