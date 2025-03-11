package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name = "cart_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity cannot be negative or 0")
    private Integer quantity;

    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", inclusive = false, message = "Price must be greater than 0")
    @Column(precision = 10, scale = 2)
    private BigDecimal priceAtAdding;

    @ManyToOne
    @JoinColumn(name = "vinyl_record_id")
    private VinylRecord vinylRecord;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;
}
