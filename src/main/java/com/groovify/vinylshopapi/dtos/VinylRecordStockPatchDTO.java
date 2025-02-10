package com.groovify.vinylshopapi.dtos;

import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class VinylRecordStockPatchDTO {
    @Min(value = 0)
    private Integer amountInStock;

    @Min(value = 0)
    private Integer amountSold;
}
