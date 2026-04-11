package com.bca6th.project.motorbikebackend.dto.testride;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class TestRideRequestDto {

    private Long bikeId;

    private String bikeName;

    private String bikeBrand;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be exactly 10 digits")
    private String phone;

    @NotNull(message = "Preferred date is required")
    private LocalDate preferredDate;

    @NotNull(message = "Preferred time is required")
    private LocalTime preferredTime;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}