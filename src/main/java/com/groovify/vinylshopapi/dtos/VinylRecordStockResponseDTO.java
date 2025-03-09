package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VinylRecordStockResponseDTO {
    private Long id;
    private Integer amountInStock;
    private Integer amountSold;
}
