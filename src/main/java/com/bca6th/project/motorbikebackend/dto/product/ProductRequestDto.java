package com.bca6th.project.motorbikebackend.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(max = 100)
    private String brand;

    @NotBlank(message = "Type is required")
    @Size(max = 100)
    private String type;

    @NotBlank(message = "Dimensions are required")
    @Size(max = 400)
    private String dimensionMmLWH;

    @NotNull(message = "Engine capacity is required")
    @Positive
    private Integer engineCapacityCc;

    @NotBlank(message = "Engine type is required")
    @Size(max = 800)
    private String engineType;

    @NotBlank(message = "Max power is required")
    @Size(max = 800)
    private String maxPower;

    @NotBlank(message = "Max torque is required")
    @Size(max = 800)
    private String maxTorque;

    @Size(max = 100)
    private String mileageKmpl;

    @Size(max = 100)
    private String topSpeedKmph;

    @Size(max = 100)
    private String gearbox;

    @Size(max = 100)
    private String clutchType;

    @Size(max = 100)
    private String frontBrake;

    @Size(max = 100)
    private String rearBrake;

    @Size(max = 100)
    private String frontSuspension;

    @Size(max = 100)
    private String rearSuspension;

    @Size(max = 100)
    private String frontTyre;

    @Size(max = 100)
    private String rearTyre;

    @Size(max = 100)
    private String tyreType;

    @Size(max = 100)
    private String fuelTankCapacityL;

    @Size(max = 100)
    private String seatHeightMm;

    @Size(max = 100)
    private String groundClearanceMm;

    @Size(max = 100)
    private String kerbWeightKg;

    @NotNull(message = "Stock is required")
    @Min(value = 0)
    private Integer stock;

    @NotNull(message = "Price is required")
    @PositiveOrZero
    private Double price;
}