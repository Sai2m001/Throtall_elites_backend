package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.service.ProductService;
import com.bca6th.project.motorbikebackend.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService        productService;
    private final RecommendationService recommendationService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new motorbike product")
    public ResponseEntity<Product> createProduct(
            @RequestPart("product") @Valid ProductRequestDto dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        Product created = productService.createProduct(dto, images != null ? images : new MultipartFile[0]);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing motorbike product")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestPart("product") @Valid ProductRequestDto dto,
            @RequestPart(value = "images", required = false) MultipartFile[] images) {
        Product updated = productService.updateProduct(id, dto, images != null ? images : new MultipartFile[0]);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        productService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID")
    public ResponseEntity<Product> getById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Product product = productService.getById(id);

        try {
            String viewerKey = (userDetails != null) ? userDetails.getUsername() : "guest";
            recommendationService.recordView(id, viewerKey);
        } catch (Exception e) {
        }

        return ResponseEntity.ok(product);
    }

    @GetMapping
    @Operation(summary = "List or search active products")
    public ResponseEntity<Page<Product>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minCc,
            @RequestParam(required = false) Integer maxCc,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        List<String> brandList = (brand != null && !brand.isBlank())
                ? Arrays.stream(brand.split(",")).map(String::trim).map(String::toLowerCase).filter(s -> !s.isEmpty()).toList()
                : null;

        List<String> typeList = (type != null && !type.isBlank())
                ? Arrays.stream(type.split(",")).map(String::trim).map(String::toLowerCase).filter(s -> !s.isEmpty()).toList()
                : null;

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Product> result = (name == null && brandList == null && typeList == null &&
                minCc == null && maxCc == null && minPrice == null && maxPrice == null)
                ? productService.getAllActive(pageable)
                : productService.search(name, brandList, typeList, minCc, maxCc, minPrice, maxPrice, pageable);

        return ResponseEntity.ok(result);
    }
}