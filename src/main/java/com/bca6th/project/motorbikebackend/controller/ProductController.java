package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.product.BrandTag;
import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProductController {

    private final ProductService productService;

    // ADMIN: Create Products
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Create a new motorbike product", description = "Creates a new product with optional images. Admin and Superadmin only.")
    public ResponseEntity<Product> createProduct(
            @Parameter(description = "Product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "Product images (multiple allowed)", required = false)
            @RequestPart("images") MultipartFile[] images) {

        Product created = productService.createProduct(dto, images);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // ADMIN: Update Products
    @PatchMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Update a existing motorbike product", description = "Updates product details and images. Admin and Superadmin only.")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Parameter(description = "Product details in JSON format", required = true)
            @RequestPart("product") @Valid ProductRequestDto dto,

            @Parameter(description = "Product images (multiple allowed)", required = false)
            @RequestPart("images") MultipartFile[] images) {

        Product updated = productService.updateProduct(id, dto, images);
        return ResponseEntity.ok(updated);
    }

    // ADMIN: List all products for admin dashboard
    @GetMapping("/getAllProduct")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Gets all products for Admin Dashboard", description = "Gets all products including inactive ones. Admin and Superadmin only.")
    public ResponseEntity<Page<Product>> getProductsForAdmin(Pageable pageable){
        Page<Product> products = productService.getProductsForAdmin(pageable);
        return ResponseEntity.ok(products);
    }

    // ADMIN: Soft delete (logical delete – set active = false)
    @PatchMapping("/toogle-active/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Deactivate active products", description = "Sets product as inactive instead of deleting from DB. Admin and Superadmin only.")
    public ResponseEntity<Product> toggleActiveStatus(@PathVariable Long id) {
        Product updatedProduct  =  productService.toggleProductActiveStatus(id);
        return ResponseEntity.ok(updatedProduct);
    }

    // ADMIN: Hard delete (permanent removal from DB)
    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPERADMIN')")
    @Operation(summary = "Hard delete product from database", description = "Permanently removes product from DB. Admin and Superadmin only.")
    public ResponseEntity<Void> hardDelete(@PathVariable Long id) {
        productService.hardDelete(id);
        return ResponseEntity.noContent().build();
    }

    // PUBLIC: Get product by ID
    @GetMapping("/getProductById/{id}")
    @Operation(summary = "Get product by ID", description = "Retrieves product details by its ID. Publicly accessible.")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    //Client side, Public (Frontend): List all active products (call this api for the client side of the frontend not for admin dashboard)
    @GetMapping("/getAllActiveProduct")
    @Operation(summary = "Gets all active products", description = "Retrieves all active products for clients with pagination. Publicly accessible.")
    public ResponseEntity<Slice<Product>> getProductsForClients(Pageable pageable){
        Slice<Product> products = productService.getProductForClients(pageable);
        return ResponseEntity.ok(products);
    }

    // Gets Brand Tags with active product count
    @GetMapping("/getBrand-tags")
    public ResponseEntity<List<BrandTag>> getActiveBrandTags() {
        return ResponseEntity.ok(productService.getActiveBrandTags());
    }

//    PUBLIC: List all active
//    @GetMapping
//    @Operation(summary = "List or search products", description = "Supports filter, sort, pagination")
//    public ResponseEntity<Page<Product>> list(
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String brand,
//            @RequestParam(required = false) String type,
//            @RequestParam(required = false) Integer minCc,
//            @RequestParam(required = false) Integer maxCc,
//            @RequestParam(required = false) Double minPrice,
//            @RequestParam(required = false) Double maxPrice,
//            @RequestParam(defaultValue = "0") int page,
//            @RequestParam(defaultValue = "10") int size,
//            @RequestParam(defaultValue = "id") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDir) {
//
//        Sort sort = sortDir.equalsIgnoreCase("desc")
//                ? Sort.by(sortBy).descending()
//                : Sort.by(sortBy).ascending();
//
//        PageRequest pr = PageRequest.of(page, size, sort);
//
//        Page<Product> result = (name == null && brand == null && type == null &&
//                minCc == null && maxCc == null && minPrice == null && maxPrice == null)
//                ? productService.getAllActive(pr)
//                : productService.search(name, brand, type, minCc, maxCc, minPrice, maxPrice, pr);
//
//        return ResponseEntity.ok(result);
//    }
}
