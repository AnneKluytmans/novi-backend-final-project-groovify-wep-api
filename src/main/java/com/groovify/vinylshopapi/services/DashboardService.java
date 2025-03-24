package com.groovify.vinylshopapi.services;

import com.groovify.vinylshopapi.dtos.DashboardResponseDTO;
import com.groovify.vinylshopapi.dtos.VinylRecordSummaryResponseDTO;
import com.groovify.vinylshopapi.mappers.VinylRecordMapper;
import com.groovify.vinylshopapi.models.Order;
import com.groovify.vinylshopapi.models.OrderItem;
import com.groovify.vinylshopapi.repositories.OrderRepository;
import com.groovify.vinylshopapi.specifications.OrderSpecification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {
    private final OrderRepository orderRepository;
    private final VinylRecordMapper vinylRecordMapper;

    public DashboardService(
            OrderRepository orderRepository,
            VinylRecordMapper vinylRecordMapper
    ) {
        this.orderRepository = orderRepository;
        this.vinylRecordMapper = vinylRecordMapper;
    }

    public DashboardResponseDTO getDashboard() {
        LocalDate startOfLastMonth = LocalDate.now().minusMonths(1).withDayOfMonth(1);
        LocalDate endOfLastMonth = LocalDate.now().minusMonths(1).with(TemporalAdjusters.lastDayOfMonth());

        LocalDate startOfThisMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfThisMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());

        LocalDate startOfLastYear = LocalDate.now().minusYears(1).withDayOfYear(1);
        LocalDate endOfLastYear = LocalDate.now().minusYears(1).with(TemporalAdjusters.lastDayOfYear());

        LocalDate startOfThisYear = LocalDate.now().withDayOfYear(1);
        LocalDate endOfThisYear = LocalDate.now().with(TemporalAdjusters.lastDayOfYear());

        List<Order> ordersLastMonth = findOrders(startOfLastMonth, endOfLastMonth);
        List<Order> ordersThisMonth = findOrders(startOfThisMonth, endOfThisMonth);
        List<Order> ordersLastYear = findOrders(startOfLastYear, endOfLastYear);
        List<Order> ordersThisYear = findOrders(startOfThisYear, endOfThisYear);

        return new DashboardResponseDTO(
                findBestSellers(ordersThisMonth, 3),
                calculateTotalRevenue(ordersLastMonth),
                calculateTotalRevenue(ordersThisMonth),
                calculateTotalRevenue(ordersLastYear),
                calculateTotalRevenue(ordersThisYear),
                calculateTotalAmountSold(ordersLastMonth),
                calculateTotalAmountSold(ordersThisMonth),
                calculateTotalAmountSold(ordersLastYear),
                calculateTotalAmountSold(ordersThisYear)
        );
    }

    private List<Order> findOrders(LocalDate orderedAfter, LocalDate orderedBefore) {
        List<String> excludedShippingStatuses = List.of("LOST", "DAMAGED", "RETURNED");
        Specification<Order> specification = OrderSpecification.filterOrders(
                "CONFIRMED", null, null, excludedShippingStatuses,
                orderedBefore, orderedAfter, null, null, null, null, null
        );
        return orderRepository.findAll(specification);
    }

    private BigDecimal calculateTotalRevenue(List<Order> orders) {
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (Order order : orders) {
            totalRevenue = totalRevenue.add(order.getSubTotalPrice());
        }
        return totalRevenue;
    }

    private Integer calculateTotalAmountSold(List<Order> orders) {
        Integer totalAmountSold = 0;
        for (Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                totalAmountSold += orderItem.getQuantity();
            }
        }
        return totalAmountSold;
    }

    private Map<VinylRecordSummaryResponseDTO, Integer> findBestSellers(List<Order> orders, Integer topN) {
        Map<VinylRecordSummaryResponseDTO, Integer> soldRecords = new HashMap<>();

        for(Order order : orders) {
            for (OrderItem orderItem : order.getOrderItems()) {
                VinylRecordSummaryResponseDTO vinylRecordDTO = vinylRecordMapper.toSummaryResponseDTO(orderItem.getVinylRecord());
                soldRecords.put(vinylRecordDTO, soldRecords.getOrDefault(vinylRecordDTO, 0) + orderItem.getQuantity());
            }
        }

        Map<VinylRecordSummaryResponseDTO, Integer> bestSellers = new LinkedHashMap<>();

        while (bestSellers.size() < topN && !soldRecords.isEmpty()) {
            VinylRecordSummaryResponseDTO bestSeller = null;
            int maxAmountSold = 0;

            for (Map.Entry<VinylRecordSummaryResponseDTO, Integer> entry : soldRecords.entrySet()) {
                if (entry.getValue() > maxAmountSold) {
                    bestSeller = entry.getKey();
                    maxAmountSold = entry.getValue();
                }
            }
            bestSellers.put(bestSeller, maxAmountSold);
            soldRecords.remove(bestSeller);
        }

        return bestSellers;
    }
}
