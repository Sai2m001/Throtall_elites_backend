package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.recommendation.RecommendationDto;
import com.bca6th.project.motorbikebackend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;
    @GetMapping("/most-viewed")
    @Operation(summary = "Top N most-viewed bikes in the last 30 days")
    public ResponseEntity<List<RecommendationDto>> mostViewed(
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(required = false) Long excludeId
    ) {
        return ResponseEntity.ok(recommendationService.getMostViewed(limit, excludeId));
    }

    @GetMapping("/most-requested")
    @Operation(summary = "Top N bikes with the most test ride requests")
    public ResponseEntity<List<RecommendationDto>> mostRequested(
            @RequestParam(defaultValue = "6") int limit,
            @RequestParam(required = false) Long excludeId
    ) {
        return ResponseEntity.ok(recommendationService.getMostRequested(limit, excludeId));
    }
}