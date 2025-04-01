package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BestsellersResponseDTO {
    private Map<VinylRecordSummaryResponseDTO, Integer> bestSellersThisMonth;
    private Map<VinylRecordSummaryResponseDTO, Integer> bestSellersThisYear;
}
