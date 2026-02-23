package com.bca6th.project.motorbikebackend.dto.testride;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TestRideRequestDto {

    @NotNull(message = "Bike ID is required")
    private Long bikeId;

    @NotBlank(message = "Bike name is required")
    private String bikeName;

    @NotBlank(message = "Bike brand is required")
    private String bikeBrand;

    @NotBlank(message = "Phone number is required")
    private String phone;

    @NotNull(message = "Preferred date is required")
    private LocalDate preferredDate;

    @NotNull(message = "Preferred time is required")
    private LocalTime preferredTime;

    private String notes;
}


















