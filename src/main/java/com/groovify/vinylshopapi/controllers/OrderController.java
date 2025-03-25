package com.groovify.vinylshopapi.controllers;

import com.groovify.vinylshopapi.dtos.*;
import com.groovify.vinylshopapi.services.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping()
    public ResponseEntity<List<OrderSummaryResponseDTO>> getOrders(
            @RequestParam(required = false) String confirmationStatus,
            @RequestParam(required = false) String paymentStatus,
            @RequestParam(required = false) String shippingStatus,
            @RequestParam(required = false) List<String> excludedShippingStatuses,
            @RequestParam(required = false) LocalDate orderedBefore,
            @RequestParam(required = false) LocalDate orderedAfter,
            @RequestParam(required = false) BigDecimal minTotalPrice,
            @RequestParam(required = false) BigDecimal maxTotalPrice,
            @RequestParam(required = false) Boolean isDeleted,
            @RequestParam(required = false) LocalDate deletedAfter,
            @RequestParam(required = false) LocalDate deletedBefore,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder
    ) {
        List<OrderSummaryResponseDTO> orders = orderService.getOrders(
                confirmationStatus, paymentStatus, shippingStatus, excludedShippingStatuses, orderedBefore, orderedAfter,
                minTotalPrice, maxTotalPrice, isDeleted, deletedAfter, deletedBefore, sortBy, sortOrder
        );
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(
            @PathVariable Long id
    ) {
        OrderResponseDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
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


    @GetMapping("/{id}/invoice")
    public ResponseEntity<InvoiceResponseDTO> getOrderInvoice(
            @PathVariable Long id
    ) {
        InvoiceResponseDTO invoice = orderService.getOrderInvoice(id);
        return ResponseEntity.ok(invoice);
    }

}
