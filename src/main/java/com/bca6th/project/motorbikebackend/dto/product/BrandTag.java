package com.bca6th.project.motorbikebackend.dto.product;

public class BrandTag {

    private String brand;
    private long count;

    // MUST have this public no-arg constructor (Hibernate requires it for projection)
    public BrandTag() {
    }

    // MUST match the query parameters exactly
    public BrandTag(String brand, long count) {
        this.brand = brand;
        this.count = count;
    }

    public String getBrand() {
        return brand;
    }

    public long getCount() {
        return count;
    }
}
