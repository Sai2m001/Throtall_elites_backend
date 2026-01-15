package com.bca6th.project.motorbikebackend.repository;

import com.bca6th.project.motorbikebackend.dto.product.BrandTag;
import com.bca6th.project.motorbikebackend.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @EntityGraph(attributePaths = {"images"})
    Optional<Product> findProductWithImagesById(Long id);

    //Client/Public users side: Get all active product
    @EntityGraph(attributePaths = {"images"})
    @Query("SELECT p FROM Product p WHERE  p.active = true")
    Slice<Product> findAllActiveSlice(Pageable pageable);

    //Admin side: Get all product for admin dashboard
    @EntityGraph(attributePaths = {"images"})
    Page<Product> findAll(Pageable pageable);

    // List of brands with count of active products
    @Query("SELECT new com.bca6th.project.motorbikebackend.dto.product.BrandTag(p.brand, COUNT(p)) " +
            "FROM Product p " +
            "WHERE p.active = true " +
            "GROUP BY p.brand " +
            "ORDER BY p.brand ASC")
    List<BrandTag> findActiveBrandTags();


     //Search with filters + images + pagination
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
