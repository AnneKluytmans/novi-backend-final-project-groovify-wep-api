package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@Data
@NoArgsConstructor
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity cannot be negative or 0")
    private Integer quantity;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Price is required")
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    private BigDecimal priceAtPurchase;

    @ManyToOne
    @JoinColumn(name = "vinyl_record_id", referencedColumnName = "id", nullable = false)
    private VinylRecord vinylRecord;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false)
    private Order order;


    public OrderItem(Integer quantity, VinylRecord vinylRecord, Order order ) {
        this.quantity = quantity;
        this.priceAtPurchase = vinylRecord.getPrice();
        this.vinylRecord = vinylRecord;
        this.order = order;
    }
}
