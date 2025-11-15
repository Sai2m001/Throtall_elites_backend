package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    // Fetch product with images (eager)
    @EntityGraph(attributePaths = "images")
    Optional<Product> findWithImagesById(Long id);

    // Fetch all active products with images + pagination
    @EntityGraph(attributePaths = "images")
    Page<Product> findByActiveTrue(Pageable pageable);

    // Search with filters + images + pagination
    @Query("""
        SELECT p FROM Product p LEFT JOIN FETCH p.images
        WHERE p.active = true
          AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:brand IS NULL OR LOWER(p.brand) = LOWER(:brand))
          AND (:type IS NULL OR LOWER(p.type) = LOWER(:type))
          AND (:minCc IS NULL OR p.engineCapacityCc >= :minCc)
          AND (:maxCc IS NULL OR p.engineCapacityCc <= :maxCc)
          AND (:minPrice IS NULL OR p.price >= :minPrice)
          AND (:maxPrice IS NULL OR p.price <= :maxPrice)
        """)
    @EntityGraph(attributePaths = "images")
    Page<Product> search(
            @Param("name") String name,
            @Param("brand") String brand,
            @Param("type") String type,
            @Param("minCc") Integer minCc,
            @Param("maxCc") Integer maxCc,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
