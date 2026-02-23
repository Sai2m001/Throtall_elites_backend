package com.bca6th.project.motorbikebackend.dto.testride;

import com.bca6th.project.motorbikebackend.model.TestRideStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TestRideStatusUpdateDto {

    @NotNull(message = "Status is required")
    private TestRideStatus status;
    private String declineReason;
}