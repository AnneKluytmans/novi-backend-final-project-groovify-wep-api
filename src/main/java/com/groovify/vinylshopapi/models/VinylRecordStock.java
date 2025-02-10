package com.groovify.vinylshopapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vinyl_records_stock")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount in stock is required")
    @Min(value = 0)
    private Integer amountInStock;

    @NotNull(message = "Amount sold is required")
    @Min(value = 0)
    private Integer amountSold;

    @OneToOne
    @JoinColumn(name = "vinyl_record_id", referencedColumnName = "id", nullable = false, unique = true)
    private VinylRecord vinylRecord;
}
