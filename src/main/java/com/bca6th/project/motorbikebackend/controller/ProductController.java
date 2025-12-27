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

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    // ADMIN: Create
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new motorbike product", description = "Admin only")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "Product images (multiple allowed)", required = false)
            @RequestPart("images") MultipartFile[] images) {

        Product created = productService.createProduct(dto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ADMIN: Update
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a existing motorbike product", description = "Admin only")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Parameter(description = "Product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "Product images (multiple allowed)", required = false)
            @RequestPart("images") MultipartFile[] images) {

        Product updated = productService.updateProduct(id, dto, images);
        return ResponseEntity.ok(updated);
    }

    // ADMIN: Soft delete (logical delete – set active = false)
    @DeleteMapping("/{id}/soft")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> softDelete(@PathVariable Long id) {
        productService.softDelete(id);
        return ResponseEntity.noContent().build();
    }

    // ADMIN: Hard delete (permanent removal from DB)
    @DeleteMapping("/{id}/hard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        productService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    // PUBLIC: Get by ID
    @GetMapping("/{id}")
    @Operation(summary = "Get product by ID", description = "Includes images")
    public ResponseEntity<Product> get(@PathVariable Long id) {
        return ResponseEntity.ok(productService.getById(id));
    }

    // PUBLIC: List all active
    @GetMapping
    @Operation(summary = "List or search products", description = "Supports filter, sort, pagination")
    public ResponseEntity<Page<Product>> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer minCc,
            @RequestParam(required = false) Integer maxCc,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        PageRequest pr = PageRequest.of(page, size, sort);

        Page<Product> result = (name == null && brand == null && type == null &&
                minCc == null && maxCc == null && minPrice == null && maxPrice == null)
                ? productService.getAllActive(pr)
                : productService.search(name, brand, type, minCc, maxCc, minPrice, maxPrice, pr);

        return ResponseEntity.ok(result);
    }
}
