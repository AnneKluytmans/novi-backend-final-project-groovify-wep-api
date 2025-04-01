package com.groovify.vinylshopapi.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardResponseDTO {
    private Map<VinylRecordSummaryResponseDTO, Integer> bestSellersThisMonth;
    private Map<VinylRecordSummaryResponseDTO, Integer> bestSellersThisYear;
    private BigDecimal totalRevenueLastMonth;
    private BigDecimal totalRevenueThisMonth;
    private BigDecimal totalRevenueLastYear;
    private BigDecimal totalRevenueThisYear;
    private Integer totalAmountSoldLastMonth;
    private Integer totalAmountSoldThisMonth;
    private Integer totalAmountSoldLastYear;
    private Integer totalAmountSoldThisYear;
}
