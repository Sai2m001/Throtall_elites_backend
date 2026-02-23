package com.bca6th.project.motorbikebackend.dto.delivery;

import com.bca6th.project.motorbikebackend.model.DeliveryStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class DeliveryStatusUpdateDto {

    @NotNull(message = "Status is required")
    private DeliveryStatus status;

    private LocalDate actualDeliveryDate;

    private String assignedDriver;

    private String notes;
}