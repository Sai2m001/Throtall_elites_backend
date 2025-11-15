package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    // ADMIN: Create
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a new motorbike product", description = "Admin only")
    public ResponseEntity<Product> create(@Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.create(product));
    }

    // ADMIN: Update
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update existing product", description = "Admin only")
    public ResponseEntity<Product> update(@PathVariable Long id, @Valid @RequestBody Product product) {
        return ResponseEntity.ok(productService.update(id, product));
    }

    // ADMIN: Soft Delete
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Soft delete product", description = "Sets active = false")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.softDelete(id);
        return ResponseEntity.ok().build();
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
