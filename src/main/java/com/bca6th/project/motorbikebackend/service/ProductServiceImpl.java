package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.exception.ResourceNotFoundException;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.model.ProductImage;
import com.bca6th.project.motorbikebackend.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public Product create(Product product) {
        product.setActive(true);
        linkImagesToProduct(product);
        return productRepository.save(product);
    }

    @Override
    public Product update(Long id, Product updatedProduct) {
        Product existing = getById(id);
        copyProperties(updatedProduct, existing);
        linkImagesToProduct(existing);
        return productRepository.save(existing);
    }

    @Override
    public void softDelete(Long id) {
        Product product = getById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product getById(Long id) {
        return productRepository.findWithImagesById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getAllActive(Pageable pageable) {
        return productRepository.findByActiveTrue(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> search(String name, String brand, String type,
                                Integer minCc, Integer maxCc,
                                Double minPrice, Double maxPrice,
                                Pageable pageable) {
        return productRepository.search(
                name, brand, type, minCc, maxCc, minPrice, maxPrice, pageable
        );
    }

    // Helper: Link images to product (bidirectional)
    private void linkImagesToProduct(Product product) {
        if (product.getImages() != null) {
            product.getImages().forEach(img -> img.setProduct(product));
        }
    }

    // Helper: Copy all fields except id, images (will be replaced)
    private void copyProperties(Product source, Product target) {
        target.setName(source.getName());
        target.setBrand(source.getBrand());
        target.setType(source.getType());
        target.setEngineCapacityCc(source.getEngineCapacityCc());
        target.setEngineType(source.getEngineType());
        target.setMaxPower(source.getMaxPower());
        target.setMaxTorque(source.getMaxTorque());
        target.setMileageKmpl(source.getMileageKmpl());
        target.setTopSpeedKmph(source.getTopSpeedKmph());
        target.setGearbox(source.getGearbox());
        target.setClutchType(source.getClutchType());
        target.setFrontBrake(source.getFrontBrake());
        target.setRearBrake(source.getRearBrake());
        target.setFrontSuspension(source.getFrontSuspension());
        target.setRearSuspension(source.getRearSuspension());
        target.setFrontTyre(source.getFrontTyre());
        target.setRearTyre(source.getRearTyre());
        target.setTyreType(source.getTyreType());
        target.setFuelTankCapacityL(source.getFuelTankCapacityL());
        target.setSeatHeightMm(source.getSeatHeightMm());
        target.setGroundClearanceMm(source.getGroundClearanceMm());
        target.setKerbWeightKg(source.getKerbWeightKg());
        target.setPrice(source.getPrice());
        target.setStock(source.getStock());
        target.setActive(source.getActive());

        // Replace images
        target.getImages().clear();
        if (source.getImages() != null) {
            source.getImages().forEach(img -> {
                ProductImage copy = new ProductImage();
                copy.setImageUrl(img.getImageUrl());
                copy.setPrimary(img.isPrimary());
                target.getImages().add(copy);
            });
        }
    }
}