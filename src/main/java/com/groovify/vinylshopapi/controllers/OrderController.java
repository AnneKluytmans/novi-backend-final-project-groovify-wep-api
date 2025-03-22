package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.OrderPatchDTO;
import com.groovify.vinylshopapi.dtos.OrderRequestDTO;
import com.groovify.vinylshopapi.dtos.OrderResponseDTO;
import com.groovify.vinylshopapi.dtos.OrderStatusUpdateDTO;
import com.groovify.vinylshopapi.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<?> placeOrder(
            @Valid @RequestBody OrderRequestDTO orderRequestDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        OrderResponseDTO newOrder = orderService.placeOrder(orderRequestDTO);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(newOrder.getId())
                .toUri();
        return ResponseEntity.created(location).body(newOrder);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updatePendingOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderPatchDTO orderPatchDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        OrderResponseDTO order = orderService.updatePendingOrder(id, orderPatchDTO);
        return ResponseEntity.ok(order);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatuses(
            @PathVariable Long id,
            @Valid @RequestBody OrderStatusUpdateDTO orderStatusUpdateDTO,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        OrderResponseDTO order = orderService.updateOrderStatuses(id, orderStatusUpdateDTO);
        return ResponseEntity.ok(order);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancelOrder(
            @PathVariable Long id
    ) {
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivateOrder(
            @PathVariable Long id
    ) {
        orderService.deactivateOrder(id);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{id}/reactivate")
    public ResponseEntity<Void> reactivateOrder(
            @PathVariable Long id
    ) {
        orderService.reactivateOrder(id);
        return ResponseEntity.noContent().build();
    }

}
