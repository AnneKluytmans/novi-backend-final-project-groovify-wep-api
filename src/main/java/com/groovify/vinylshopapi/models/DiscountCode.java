package com.groovify.vinylshopapi.models;

import com.groovify.vinylshopapi.enums.DiscountCategory;
import com.groovify.vinylshopapi.enums.DiscountType;
import com.groovify.vinylshopapi.validation.ValidDate;
import com.groovify.vinylshopapi.validation.ValidEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "discount_codes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscountCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Discount code is required")
    @Size(min = 3, max = 20, message = "Discount code must be between 3 and 20 characters")
    private String code;

    @Size(min = 3, max = 250, message = "Description cannot be longer than 250 characters")
    private String description;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Discount type is required")
    @ValidEnum(enumClass = DiscountType.class, message = "Invalid discount type")
    private DiscountType discountType;

    @DecimalMin(value = "0.01", message = "Discount value must be greater than 0")
    private Double discountValue;

    @Column(precision = 10, scale = 2)
    @DecimalMin(value = "0.01", inclusive = false, message = "Minimal Order Price must be greater than 0")
    private BigDecimal minimumOrderPrice;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Discount category is required")
    @ValidEnum(enumClass = DiscountCategory.class, message = "Invalid discount category")
    private DiscountCategory discountCategory;

    @ValidDate(mustBeFuture = true, message = "Start date must be in the future")
    private LocalDate startDate;

    @ValidDate(mustBeFuture = true, message = "Start date must be in the future")
    private LocalDate endDate;

    @ManyToMany(mappedBy = "discountCodes")
    private List<Order> orders = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "discount_applicable_vinyl_records",
            joinColumns = @JoinColumn(name = "discount_code_id"),
            inverseJoinColumns = @JoinColumn(name = "vinyl_record_id")
    )
    private List<VinylRecord> discountVinylRecords = new ArrayList<>();
}
