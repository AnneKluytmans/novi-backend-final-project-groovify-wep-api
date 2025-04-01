package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.BestsellersResponseDTO;
import com.groovify.vinylshopapi.dtos.DashboardResponseDTO;
import com.groovify.vinylshopapi.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService
    ) {
        this.dashboardService = dashboardService;
    }

    @GetMapping()
    public ResponseEntity<DashboardResponseDTO> getDashboard(
            @RequestParam(defaultValue = "3") Integer topN
    ) {
        DashboardResponseDTO dashboard = dashboardService.getDashboard(topN);
        return ResponseEntity.ok(dashboard);
    }

    @GetMapping("/bestsellers")
    public ResponseEntity<BestsellersResponseDTO> getBestsellers(
            @RequestParam(defaultValue = "3") Integer topN
    ) {
        BestsellersResponseDTO bestSellers = dashboardService.getBestsellers(topN);
        return ResponseEntity.ok(bestSellers);
    }
}
