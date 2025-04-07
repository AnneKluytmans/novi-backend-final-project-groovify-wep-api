package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vinyl_records_stock")
@Data
@NoArgsConstructor
public class VinylRecordStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount in stock is required")
    @Min(value = 0, message = "Amount cannot be negative")
    private Integer amountInStock;

    @NotNull(message = "Amount sold is required")
    @Min(value = 0, message = "Amount cannot be negative")
    private Integer amountSold;

    @OneToOne
    @JoinColumn(name = "vinyl_record_id", referencedColumnName = "id", nullable = false, unique = true)
    private VinylRecord vinylRecord;

    public VinylRecordStock(Integer amountInStock, Integer amountSold, VinylRecord vinylRecord) {
        this.amountInStock = amountInStock;
        this.amountSold = amountSold;
        this.vinylRecord = vinylRecord;
    }

    public void increaseStock(Integer quantity) {
        amountInStock += quantity;
    }

    public void decreaseStock(Integer quantity) {
        amountInStock -= quantity;
    }

    public void increaseSales(Integer quantity) {
        amountSold += quantity;
    }

    public void decreaseSales(Integer quantity) {
        amountSold -= quantity;
    }

}
