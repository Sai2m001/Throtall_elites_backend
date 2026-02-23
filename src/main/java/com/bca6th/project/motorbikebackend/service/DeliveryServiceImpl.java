package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryRequestDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryResponseDto;
import com.bca6th.project.motorbikebackend.dto.delivery.DeliveryStatusUpdateDto;
import com.bca6th.project.motorbikebackend.model.Delivery;
import com.bca6th.project.motorbikebackend.model.DeliveryStatus;
import com.bca6th.project.motorbikebackend.repository.DeliveryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    @Override
    public DeliveryResponseDto create(DeliveryRequestDto dto) {
        Delivery delivery = Delivery.builder()
                .customerName(dto.getCustomerName())
                .customerPhone(dto.getCustomerPhone())
                .customerEmail(dto.getCustomerEmail())
                .bikeBrand(dto.getBikeBrand())
                .bikeName(dto.getBikeName())
                .chassisNumber(dto.getChassisNumber())
                .deliveryAddress(dto.getDeliveryAddress())
                .scheduledDate(dto.getScheduledDate())
                .assignedDriver(dto.getAssignedDriver())
                .notes(dto.getNotes())
                .status(DeliveryStatus.SCHEDULED)
                .build();

        return toDto(deliveryRepository.save(delivery));
    }

    @Override
    public List<DeliveryResponseDto> getAll() {
        return deliveryRepository.findAllByOrderByCreatedAtDesc()
                .stream().map(this::toDto).toList();
    }

    @Override
    public DeliveryResponseDto getById(Long id) {
        return toDto(findOrThrow(id));
    }

    @Override
    public DeliveryResponseDto updateStatus(Long id, DeliveryStatusUpdateDto dto) {
        Delivery delivery = findOrThrow(id);
        delivery.setStatus(dto.getStatus());

        if (dto.getStatus() == DeliveryStatus.DELIVERED) {
            delivery.setActualDeliveryDate(
                    dto.getActualDeliveryDate() != null
                            ? dto.getActualDeliveryDate()
                            : LocalDate.now()
            );
        }

        if (dto.getAssignedDriver() != null && !dto.getAssignedDriver().isBlank()) {
            delivery.setAssignedDriver(dto.getAssignedDriver());
        }

        if (dto.getNotes() != null && !dto.getNotes().isBlank()) {
            delivery.setNotes(dto.getNotes());
        }

        return toDto(deliveryRepository.save(delivery));
    }

    @Override
    public void delete(Long id) {
        deliveryRepository.delete(findOrThrow(id));
    }

    private Delivery findOrThrow(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Delivery not found: " + id));
    }

    private DeliveryResponseDto toDto(Delivery d) {
        return DeliveryResponseDto.builder()
                .id(d.getId())
                .customerName(d.getCustomerName())
                .customerPhone(d.getCustomerPhone())
                .customerEmail(d.getCustomerEmail())
                .bikeBrand(d.getBikeBrand())
                .bikeName(d.getBikeName())
                .chassisNumber(d.getChassisNumber())
                .deliveryAddress(d.getDeliveryAddress())
                .scheduledDate(d.getScheduledDate())
                .assignedDriver(d.getAssignedDriver())
                .notes(d.getNotes())
                .status(d.getStatus())
                .actualDeliveryDate(d.getActualDeliveryDate())
                .createdAt(d.getCreatedAt())
                .updatedAt(d.getUpdatedAt())
                .build();
    }
}