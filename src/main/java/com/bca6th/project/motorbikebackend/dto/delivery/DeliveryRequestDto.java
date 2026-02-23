package com.bca6th.project.motorbikebackend.dto.delivery;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DeliveryRequestDto {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Phone number is required")
    private String customerPhone;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    private String customerEmail;

    @NotBlank(message = "Bike brand is required")
    private String bikeBrand;

    @NotBlank(message = "Bike name is required")
    private String bikeName;

    private String chassisNumber;

    @NotBlank(message = "Delivery address is required")
    private String deliveryAddress;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

    private String assignedDriver;
    private String notes;
}