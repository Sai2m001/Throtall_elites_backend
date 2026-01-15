package com.bca6th.project.motorbikebackend.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for creating or updating a motorbike product")
public class ProductRequestDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Schema(description = "Name of the motorbike", example = "Pulsar NS200")
    private String name;

    @NotBlank(message = "Brand is required")
    @Size(max = 100, message = "Brand cannot exceed 100 characters")
    @Schema(description = "Name of the brand", example = "Bajaj")
    private String brand;

    @NotBlank(message = "Type is required")
    @Size(max = 100, message = "Type cannot exceed 100 characters")
    @Schema(description = "Bike type (e.g., Naked, Cruiser, Sportbike, Touring, Standard, Dual-Sport)",
            example = "Naked, Cruiser, Sportbike, Touring, Standard, Dual-Sport")
    private String type;

    @NotBlank(message = "Dimensions are required")
    @Size(max = 50, message = "Dimensions cannot exceed 50 characters")
    @Schema(description = "Dimensions (L×W×H in mm)", example = "1990mm x 800mm x 1,070mm")
    private String dimensionMmLWH;

    @NotNull(message = "Engine capacity is required")
    @Positive(message = "Engine capacity must be positive")
    @Schema(description = "Engine capacity in cc", example = "200")
    private Integer engineCapacityCc;

    @Size(max = 100, message = "Engine type cannot exceed 100 characters")
    @Schema(description = "Engine type", example = "Liquid Cooled, Single Cylinder, DOHC, Twin Spark FI")
    private String engineType;

    @Size(max = 50, message = "Max power cannot exceed 50 characters")
    @Schema(description = "Maximum power output", example = "24.5 PS @ 9750 rpm")
    private String maxPower;

    @Size(max = 50, message = "Max torque cannot exceed 50 characters")
    @Schema(description = "Maximum torque", example = "18.6 Nm @ 8000 rpm")
    private String maxTorque;

    @Size(max = 20, message = "Mileage cannot exceed 20 characters")
    @Schema(description = "Mileage", example = "40 kmpl")
    private String mileageKmpl;

    @Size(max = 20, message = "Top speed cannot exceed 20 characters")
    @Schema(description = "Top speed", example = "135 km/h")
    private String topSpeedKmph;

    @Size(max = 20, message = "Gearbox cannot exceed 20 characters")
    @Schema(description = "Gearbox type", example = "6 Speed")
    private String gearbox;

    @Size(max = 50, message = "Clutch type cannot exceed 50 characters")
    @Schema(description = "Clutch type", example = "Wet multi-plate clutch")
    private String clutchType;

    @Size(max = 50, message = "Front brake cannot exceed 50 characters")
    @Schema(description = "Front brake", example = "Disc Brake")
    private String frontBrake;

    @Size(max = 50, message = "Rear brake cannot exceed 50 characters")
    @Schema(description = "Rear brake", example = "Disc Brake")
    private String rearBrake;

    @Size(max = 100, message = "Front suspension cannot exceed 100 characters")
    @Schema(description = "Front suspension", example = "Telescopic Fork")
    private String frontSuspension;

    @Size(max = 100, message = "Rear suspension cannot exceed 100 characters")
    @Schema(description = "Rear suspension", example = "Monoshock")
    private String rearSuspension;

    @Size(max = 50, message = "Front tyre cannot exceed 50 characters")
    @Schema(description = "Front tyre", example = "120/70 ZR17")
    private String frontTyre;

    @Size(max = 50, message = "Rear tyre cannot exceed 50 characters")
    @Schema(description = "Rear tyre", example = "150/60 ZR17")
    private String rearTyre;

    @Size(max = 30, message = "Tyre type cannot exceed 30 characters")
    @Schema(description = "Tyre type", example = "Tubeless")
    private String tyreType;

    @Size(max = 20, message = "Fuel tank capacity cannot exceed 20 characters")
    @Schema(description = "Fuel tank capacity", example = "12 L")
    private String fuelTankCapacityL;

    @Size(max = 20, message = "Seat height cannot exceed 20 characters")
    @Schema(description = "Seat height", example = "800 mm")
    private String seatHeightMm;

    @Size(max = 20, message = "Ground clearance cannot exceed 20 characters")
    @Schema(description = "Ground clearance", example = "170 mm")
    private String groundClearanceMm;

    @Size(max = 20, message = "Kerb weight cannot exceed 20 characters")
    @Schema(description = "Kerb weight", example = "154 kg")
    private String kerbWeightKg;

    @NotNull(message = "Stock quantity is required")
    @Min(value = 0, message = "Stock cannot be negative")
    @Schema(description = "Number of units in stock", example = "10")
    private Integer stock;

    @NotNull(message = "Price is required")
    @PositiveOrZero(message = "Price must be zero or positive")
    @Schema(description = "Price of the bike", example = "99999.99")
    private Double price;
}
