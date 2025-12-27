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

    @Schema(description = "Name of the motorbike", example = "Pulsar NS200")
    @NotBlank(message = "Name is required")
    @Size(max = 100)
    private String name;

    @Schema(description = "Name of the brand", example = "Bajaj")
    @NotBlank(message = "Brand is required")
    @Size(max = 100)
    private String brand;

    @NotBlank(message = "Type is required")
    @Schema(description = "Bike Type", example = "Naked, Cruiser, Sportbike, Touring, Standard, Dual-Sport")
    @Size(max = 100)
    private String type;

    @NotBlank(message = "Dimensions (L×W×H in mm) are required")
    @Schema(description = "Dimension", example = "1990mm x 800mm x 1,070mm")
    @Size(max = 50)
    private String dimensionMmLWH;

    @NotNull(message = "Engine capacity is required")
    @Schema(description = "Engine Capacity CC", example = "200")
    @Positive
    private Integer engineCapacityCc;

    @NotBlank(message = "Engine type is required")
    @Schema(description = "Engine Type", example = "Liquid Cooled, Single Cylinder, DOHC")
    @Size(max = 50)
    private String engineType;

    @NotBlank(message = "Max power is required")
    @Schema(description = "Max Power", example = "24.5 PS @ 9750 rpm")
    @Size(max = 30)
    private String maxPower;

    @NotBlank(message = "Max torque is required")
    @Schema(description = "Name of the brand", example = "18.6 Nm @ 8000 rpm")
    @Size(max = 30)
    private String maxTorque;

    @Size(max = 20)
    @Schema(description = "Mileage", example = "40 kmpl")
    private String mileageKmpl;

    @Size(max = 20)
    @Schema(description = "Top Speed", example = "135 km/h")
    private String topSpeedKmph;

    @Size(max = 20)
    @Schema(description = "Gear Box", example = "6 Speed")
    private String gearbox;

    @Size(max = 50)
    @Schema(description = "Clutch Type", example = "Wet multi-plate clutch")
    private String clutchType;

    @Size(max = 50)
    @Schema(description = "Front Brake", example = "Disc Brake")
    private String frontBrake;

    @Size(max = 50)
    @Schema(description = "Rear Brake", example = "Disc Brake")
    private String rearBrake;

    @Size(max = 100)
    @Schema(description = "Front Suspension", example = "Telescopic Fork")
    private String frontSuspension;

    @Size(max = 100)
    @Schema(description = "Rear Suspension", example = "Monoshock")
    private String rearSuspension;

    @Size(max = 50)
    @Schema(description = "FrontTyre", example = "120/70 ZR17")
    private String frontTyre;

    @Size(max = 50)
    @Schema(description = "RearTyre", example = "150/60 ZR17")
    private String rearTyre;

    @Size(max = 30)
    @Schema(description = "Tyre Type", example = "Tubeless")
    private String tyreType;

    @Size(max = 20)
    @Schema(description = "Fuel Tank Capacity", example = "12 L")
    private String fuelTankCapacityL;

    @Size(max = 20)
    @Schema(description = "Seat Height", example = "800 mm")
    private String seatHeightMm;

    @Size(max = 20)
    @Schema(description = "Ground Clearance", example = "170 mm")
    private String groundClearanceMm;

    @Size(max = 20)
    @Schema(description = "Kerb Weight", example = "154 kg")
    private String kerbWeightKg;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "no. of stock", example = "10")
    private Integer stock;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Schema(description = "Price of bike", example = "99999.99")
    private Double price;
}
