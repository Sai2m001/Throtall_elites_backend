package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.product.BrandTag;
import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // ADMIN ONLY
    List<ProductImage> processImageUploads(Product product, MultipartFile[] files);
    Product createProduct(ProductRequestDto dto, MultipartFile[] images);
    Product updateProduct(Long id, ProductRequestDto dto, MultipartFile[] newImages);
    Page<Product> getProductsForAdmin(Pageable pageable);
    /**
     * Toggles the active status of a product.
     * - If active = true → sets to false (soft delete / deactivate)
     * - If active = false → sets to true (reactivate / restore)
     * @param id product ID
     * @return the updated Product
     */
    Product toggleProductActiveStatus(Long id);
    void hardDelete(Long id);

    // PUBLIC
    Product getProductById(Long id);
    Slice<Product> getProductForClients(Pageable pageable);
    Page<Product> search(
            String name,
            String brand,
            String type,
            Integer minCc,
            Integer maxCc,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );
    List<BrandTag> getActiveBrandTags();
}