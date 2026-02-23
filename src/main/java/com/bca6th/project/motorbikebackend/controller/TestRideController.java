package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.testride.TestRideRequestDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideResponseDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideStatusUpdateDto;
import com.bca6th.project.motorbikebackend.service.TestRideService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test-rides")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class TestRideController {

    private final TestRideService testRideService;

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Submit a test ride request", description = "Authenticated USER only.")
    public ResponseEntity<TestRideResponseDto> submitRequest(
            @RequestBody @Valid TestRideRequestDto dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        TestRideResponseDto response = testRideService.submitRequest(dto, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get current user's test ride requests")
    public ResponseEntity<List<TestRideResponseDto>> getMyRequests(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return ResponseEntity.ok(testRideService.getMyRequests(userDetails.getUsername()));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all test ride requests", description = "Admin only.")
    public ResponseEntity<List<TestRideResponseDto>> getAllRequests() {
        return ResponseEntity.ok(testRideService.getAllRequests());
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update test ride request status", description = "Admin only. CONFIRMED or DECLINED.")
    public ResponseEntity<TestRideResponseDto> updateStatus(
            @PathVariable Long id,
            @RequestBody @Valid TestRideStatusUpdateDto dto
    ) {
        return ResponseEntity.ok(testRideService.updateStatus(id, dto));
    }
}