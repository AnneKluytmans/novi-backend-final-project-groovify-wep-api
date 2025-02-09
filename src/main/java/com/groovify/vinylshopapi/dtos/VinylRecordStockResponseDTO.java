package com.groovify.vinylshopapi.dtos;

import lombok.Data;

@Data
public class VinylRecordStockResponseDTO {
    private Long id;
    private Integer amountInStock;
    private Integer amountSold;
}
