package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {

    // ADMIN ONLY
    Product create(Product product);
    Product update(Long id, Product product);
    void softDelete(Long id);
    void hardDelete(Long id);

    // PUBLIC
    Product getById(Long id);
    Page<Product> getAllActive(Pageable pageable);
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
}