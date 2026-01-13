package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.model.ProductImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // ADMIN ONLY
    List<ProductImage> processImageUploads(Product product, MultipartFile[] files);
    Product createProduct(ProductRequestDto dto, MultipartFile[] images);
    Product updateProduct(Long id, ProductRequestDto dto, MultipartFile[] newImages);
    void softDelete(Long id);
    void hardDelete(Long id);

    // PUBLIC
    Product getById(Long id);
    Page<Product> getAllActive(Pageable pageable);
    Page<Product> search(
            String name,
            List<String> brandList,
            List<String> typeList,
            Integer minCc,
            Integer maxCc,
            Double minPrice,
            Double maxPrice,
            Pageable pageable
    );
}