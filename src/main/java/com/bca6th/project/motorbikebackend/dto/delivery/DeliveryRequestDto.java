package com.bca6th.project.motorbikebackend.dto.delivery;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DeliveryRequestDto {

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name should contain only letters and spaces")
    private String customerName;

//    @NotBlank(message = "Phone number is required")
//    @Pattern(regexp = "^[0-9]{10}$", message = "Phone number must be 10 digits")
    private String customerPhone;

    @NotBlank(message = "Email is required")
    @Email(message = "Must be a valid email")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String customerEmail;

//    @NotBlank(message = "Bike brand is required")
//    @Size(min = 2, max = 50, message = "Brand name must be between 2 and 50 characters")
    private String bikeBrand;

//    @NotBlank(message = "Bike name is required")
//    @Size(min = 2, max = 100, message = "Bike name must be between 2 and 100 characters")
    private String bikeName;

//    @Pattern(regexp = "^[A-Z0-9]{17}$", message = "Chassis number must be 17 alphanumeric characters")
    private String chassisNumber;

    @NotBlank(message = "Delivery address is required")
    @Size(min = 2, max = 250, message = "Address must be between 2 and 250 characters")
    private String deliveryAddress;

    @NotNull(message = "Scheduled date is required")
    private LocalDate scheduledDate;

//    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Driver name should contain only letters and spaces")
    private String assignedDriver;

    @Size(max = 500, message = "Notes cannot exceed 500 characters")
    private String notes;
}