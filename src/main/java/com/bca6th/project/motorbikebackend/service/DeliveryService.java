package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryRequestDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryResponseDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryStatusUpdateDto;

import java.util.List;

public interface DeliveryService {
    DeliveryResponseDto create(DeliveryRequestDto dto);
    List<DeliveryResponseDto> getAll();
    DeliveryResponseDto getById(Long id);
    DeliveryResponseDto updateStatus(Long id, DeliveryStatusUpdateDto dto);
    void delete(Long id);
}