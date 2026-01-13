package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
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
      AND (:brandList IS NULL OR LOWER(p.brand) IN :brandList)
      AND (:typeList IS NULL OR LOWER(p.type) IN :typeList)
      AND (:minCc IS NULL OR p.engineCapacityCc >= :minCc)
      AND (:maxCc IS NULL OR p.engineCapacityCc <= :maxCc)
      AND (:minPrice IS NULL OR p.price >= :minPrice)
      AND (:maxPrice IS NULL OR p.price <= :maxPrice)
    """)
    Page<Product> search(
            @Param("name") String name,
            @Param("brandList") List<String> brandList,
            @Param("typeList") List<String> typeList,
            @Param("minCc") Integer minCc,
            @Param("maxCc") Integer maxCc,
            @Param("minPrice") Double minPrice,
            @Param("maxPrice") Double maxPrice,
            Pageable pageable
    );
}
