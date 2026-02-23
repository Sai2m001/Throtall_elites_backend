package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.testride.TestRideRequestDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideResponseDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideStatusUpdateDto;

import java.util.List;

public interface TestRideService {
    TestRideResponseDto submitRequest(TestRideRequestDto dto, String userEmail);
    List<TestRideResponseDto> getAllRequests();
    TestRideResponseDto updateStatus(Long requestId, TestRideStatusUpdateDto dto);
    List<TestRideResponseDto> getMyRequests(String userEmail);
}