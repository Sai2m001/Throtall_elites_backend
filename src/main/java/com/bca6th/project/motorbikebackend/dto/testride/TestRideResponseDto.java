package com.bca6th.project.motorbikebackend.dto.testride;

import com.bca6th.project.motorbikebackend.model.TestRideStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class TestRideResponseDto {
    private Long id;
    private Long bikeId;
    private String bikeName;
    private String bikeBrand;

    private Long userId;
    private String userName;
    private String userEmail;

    private String phone;
    private LocalDate preferredDate;
    private LocalTime preferredTime;
    private String notes;
    private TestRideStatus status;
    private String declineReason;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}