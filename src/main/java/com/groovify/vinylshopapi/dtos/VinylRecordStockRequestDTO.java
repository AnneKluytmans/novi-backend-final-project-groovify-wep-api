package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordStockRequestDTO {
    @NotNull(message = "Amount in stock is required")
    @Min(value = 0)
    private Integer amountInStock;

    @NotNull(message = "Amount sold is required")
    @Min(value = 0)
    private Integer amountSold;
}
