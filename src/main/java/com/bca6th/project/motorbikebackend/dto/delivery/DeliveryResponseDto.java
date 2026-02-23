package com.bca6th.project.motorbikebackend.dto.delivery;

import com.bca6th.project.motorbikebackend.model.DeliveryStatus;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class DeliveryResponseDto {
    private Long id;
    private String customerName;
    private String customerPhone;
    private String customerEmail;
    private String bikeBrand;
    private String bikeName;
    private String chassisNumber;
    private String deliveryAddress;
    private LocalDate scheduledDate;
    private String assignedDriver;
    private String notes;
    private DeliveryStatus status;
    private LocalDate actualDeliveryDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}