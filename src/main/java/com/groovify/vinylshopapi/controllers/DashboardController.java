package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.DashboardResponseDTO;
import com.groovify.vinylshopapi.services.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public ResponseEntity<DashboardResponseDTO> getDashboard() {
        DashboardResponseDTO dashboard = dashboardService.getDashboard();
        return ResponseEntity.ok(dashboard);
    }
}
