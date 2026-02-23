package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryRequestDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryResponseDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryStatusUpdateDto;
import com.bca6th.project.motorbikebackend.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DeliveryController {

    private final DeliveryService deliveryService;
    @PostMapping
    @Operation(summary = "Create a new delivery record")
    public ResponseEntity<DeliveryResponseDto> create(@RequestBody @Valid DeliveryRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(dto));
    }

    @GetMapping
    @Operation(summary = "Get all delivery records")
    public ResponseEntity<List<DeliveryResponseDto>> getAll() {
        return ResponseEntity.ok(deliveryService.getAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a delivery record by ID")
    public ResponseEntity<DeliveryResponseDto> getById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.getById(id));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update delivery status (and optionally driver/notes)")
    public ResponseEntity<DeliveryResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid DeliveryStatusUpdateDto dto
    ) {
        return ResponseEntity.ok(deliveryService.updateStatus(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a delivery record")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}