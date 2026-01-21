package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.product.ProductRequestDto;
import com.bca6th.project.motorbikebackend.exception.ResourceNotFoundException;
import com.bca6th.project.motorbikebackend.model.Product;
import com.bca6th.project.motorbikebackend.model.ProductImage;
import com.bca6th.project.motorbikebackend.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.io.File;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Value("{app.upload.dir}")
    private String uploadDir;

    public List<ProductImage> processImageUploads(Product product, MultipartFile[] files) {
        List<ProductImage> images = new ArrayList<>();

        System.out.println("=== IMAGE UPLOAD DEBUG START ===");
        System.out.println("uploadDir (raw): " + uploadDir);
        System.out.println("Product ID: " + product.getId());

        if (files == null || files.length == 0) {
            System.out.println("No files received");
            return images;
        }

        // FIX: Convert to absolute path + normalize
        File baseDir = new File(uploadDir).getAbsoluteFile();
        String productUploadPath = baseDir.getAbsolutePath() + File.separator + "products" + File.separator + product.getId();
        Path uploadPath = Paths.get(productUploadPath);

        System.out.println("Resolved absolute upload directory: " + uploadPath);

        try {
            Files.createDirectories(uploadPath);
            System.out.println("Directory OK");
        } catch (IOException e) {
            System.err.println("Dir creation failed: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot create dir", e);
        }

        for (int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            if (file.isEmpty()) continue;

            String original = file.getOriginalFilename();
            String ext = original != null && original.contains(".")
                    ? original.substring(original.lastIndexOf("."))
                    : ".jpg";

            String filename = UUID.randomUUID().toString() + ext;
            Path filePath = uploadPath.resolve(filename);

            try {
                Files.write(filePath, file.getBytes());
                System.out.println("SUCCESS saved: " + filePath.toAbsolutePath());

                ProductImage img = new ProductImage();
                img.setImageUrl("/uploads/products/" + product.getId() + "/" + filename);
                img.setPrimary(i == 0);
                img.setProduct(product);
                images.add(img);
            } catch (IOException e) {
                System.err.println("Write failed for " + filename + ": " + e.getMessage());
                e.printStackTrace();
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "File save failed", e);
            }
        }

        System.out.println("=== IMAGE UPLOAD DEBUG END ===");
        return images;
    }

    private String getExtension(String original) {
        if (original == null) return "";
        int dot = original.lastIndexOf('.');
        return dot > 0 ? original.substring(dot) : "";
    }


    @Override
    public Product createProduct(ProductRequestDto dto, MultipartFile[] images) {

        // 1. Create new Product entity from DTO
        Product product = new Product();
        product.setName(dto.getName());
        product.setBrand(dto.getBrand());
        product.setType(dto.getType());
        product.setDimensionMmLWH(dto.getDimensionMmLWH());
        product.setEngineCapacityCc(dto.getEngineCapacityCc());
        product.setEngineType(dto.getEngineType());
        product.setMaxPower(dto.getMaxPower());
        product.setMaxTorque(dto.getMaxTorque());
        product.setMileageKmpl(dto.getMileageKmpl());
        product.setTopSpeedKmph(dto.getTopSpeedKmph());
        product.setGearbox(dto.getGearbox());
        product.setClutchType(dto.getClutchType());
        product.setFrontBrake(dto.getFrontBrake());
        product.setRearBrake(dto.getRearBrake());
        product.setFrontSuspension(dto.getFrontSuspension());
        product.setRearSuspension(dto.getRearSuspension());
        product.setFrontTyre(dto.getFrontTyre());
        product.setRearTyre(dto.getRearTyre());
        product.setTyreType(dto.getTyreType());
        product.setFuelTankCapacityL(dto.getFuelTankCapacityL());
        product.setSeatHeightMm(dto.getSeatHeightMm());
        product.setGroundClearanceMm(dto.getGroundClearanceMm());
        product.setKerbWeightKg(dto.getKerbWeightKg());
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());

        product.setActive(true); // New products are active by default

        // 2. Save product first to generate ID (required for folder path)
        product = productRepository.save(product);

        // 3. Handle image uploads (if any)
        if (images != null && images.length > 0) {
            List<ProductImage> uploadedImages = processImageUploads(product, images);
            product.getImages().addAll(uploadedImages);

            // 4. Save again with attached images
            product = productRepository.save(product);
        }

        return product;
    }

    @Override
    public Product updateProduct(Long id, ProductRequestDto dto, MultipartFile[] newImages) {

        // 1. Find existing product (throws if not found – you can customize the exception)
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));

        // 2. Update scalar fields from DTO
        existing.setName(dto.getName());
        existing.setBrand(dto.getBrand());
        existing.setType(dto.getType());
        existing.setDimensionMmLWH(dto.getDimensionMmLWH());
        existing.setEngineCapacityCc(dto.getEngineCapacityCc());
        existing.setEngineType(dto.getEngineType());
        existing.setMaxPower(dto.getMaxPower());
        existing.setMaxTorque(dto.getMaxTorque());
        existing.setMileageKmpl(dto.getMileageKmpl());
        existing.setTopSpeedKmph(dto.getTopSpeedKmph());
        existing.setGearbox(dto.getGearbox());
        existing.setClutchType(dto.getClutchType());
        existing.setFrontBrake(dto.getFrontBrake());
        existing.setRearBrake(dto.getRearBrake());
        existing.setFrontSuspension(dto.getFrontSuspension());
        existing.setRearSuspension(dto.getRearSuspension());
        existing.setFrontTyre(dto.getFrontTyre());
        existing.setRearTyre(dto.getRearTyre());
        existing.setTyreType(dto.getTyreType());
        existing.setFuelTankCapacityL(dto.getFuelTankCapacityL());
        existing.setSeatHeightMm(dto.getSeatHeightMm());
        existing.setGroundClearanceMm(dto.getGroundClearanceMm());
        existing.setKerbWeightKg(dto.getKerbWeightKg());
        existing.setStock(dto.getStock());
        existing.setPrice(dto.getPrice());
        // active remains unchanged (use separate endpoint for soft delete if needed)

        // 3. Handle new image uploads (optional – if no new images sent, skip)
        if (newImages != null && newImages.length > 0) {
            List<ProductImage> uploadedImages = processImageUploads(existing, newImages);
            existing.getImages().addAll(uploadedImages);
        }

        // 4. Save and return updated product (with new images if added)
        return productRepository.save(existing);
    }

    @Override
    public void softDelete(Long id) {
        Product product = getById(id);
        product.setActive(false);
        productRepository.save(product);
    }

    @Override
    public void hardDelete(Long id) {
        Product product = getById(id);
        productRepository.delete(product);
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
    public Page<Product> search(String name, List<String> brandList, List<String> typeList,
                                Integer minCc, Integer maxCc,
                                Double minPrice, Double maxPrice,
                                Pageable pageable) {
        return productRepository.search(
                name, brandList, typeList, minCc, maxCc, minPrice, maxPrice, pageable
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

    @Value("${app.test.property:NOT_FOUND}")
    private String testProperty;

    @PostConstruct
    public void logTest() {
        System.out.println("TEST PROPERTY VALUE: " + testProperty);
    }
}