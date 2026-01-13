package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.service.ProductService;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth") // Assuming JWT with Bearer token
public class ProductController {

    private final ProductService productService;

    // ADMIN: Create new product
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new motorbike product", description = "Admin only. Requires 'product' JSON part and optional 'images' files.")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "Product images (multiple allowed, first is primary)", required = false)
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        Product created = productService.createProduct(dto, images != null ? images : new MultipartFile[0]);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ADMIN: Update existing product (partial update - only provided fields)
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update an existing motorbike product", description = "Admin only. Adds new images if provided; existing images unchanged.")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Parameter(description = "Updated product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "New images to add (multiple allowed)", required = false)
            @RequestPart(value = "images", required = false) MultipartFile[] images) {

        Product updated = productService.updateProduct(id, dto, images != null ? images : new MultipartFile[0]);
        return ResponseEntity.ok(updated);
    }

    // ADMIN: Soft delete (set active = false - hides from public)
    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete a product", description = "Admin only. Sets active=false, hides from public listings.")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Hard delete (permanent removal, including images folder)
    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Hard delete a product", description = "Admin only. Permanently removes product and associated files.")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        productService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN & PUBLIC: Get single product by ID (with images)
    @GetMapping("/{id}")
    @Operation(summary = "Get a product by ID", description = "Public. Includes images. Returns 404 if not found or inactive.")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Product product = productService.getById(id); // Throws if not found or inactive
        return ResponseEntity.ok(product);
    }

    // PUBLIC & ADMIN: List/search products with filters, sorting, pagination
    @GetMapping
    @Operation(summary = "List or search active products", description = "Public. Supports filtering, sorting, and pagination. Admins see all via auth if needed.")
    public ResponseEntity<Page<Product>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,      // comma-separated for multiple
            @RequestParam(required = false) String type,       // comma-separated for multiple
            @RequestParam(required = false) Integer minCc,
            @RequestParam(required = false) Integer maxCc,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(defaultValue = "price") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        // Parse comma-separated lists (lowercase for case-insensitive match)
        List<String> brandList = (brand != null && !brand.isBlank())
                ? Arrays.stream(brand.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toList()
                : null;

        List<String> typeList = (type != null && !type.isBlank())
                ? Arrays.stream(type.split(","))
                .map(String::trim)
                .map(String::toLowerCase)
                .filter(s -> !s.isEmpty())
                .toList()
                : null;

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pageable = PageRequest.of(page, size, sort);

        Page<Product> result;
        if (name == null && brandList == null && typeList == null &&
                minCc == null && maxCc == null && minPrice == null && maxPrice == null) {
            // No filters - simple active list
            result = productService.getAllActive(pageable);
        } else {
            // Use search with filters
            result = productService.search(name, brandList, typeList, minCc, maxCc, minPrice, maxPrice, pageable);
        }

        return ResponseEntity.ok(result);
    }
}