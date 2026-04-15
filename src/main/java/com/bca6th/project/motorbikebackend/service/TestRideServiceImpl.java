package com.bca6th.project.motorbikebackend.service;

import com.bca6th.project.motorbikebackend.dto.testride.TestRideRequestDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideResponseDto;
import com.bca6th.project.motorbikebackend.dto.testride.TestRideStatusUpdateDto;
import com.bca6th.project.motorbikebackend.model.TestRideRequest;
import com.bca6th.project.motorbikebackend.model.TestRideStatus;
import com.bca6th.project.motorbikebackend.model.User;
import com.bca6th.project.motorbikebackend.repository.TestRideRepository;
import com.bca6th.project.motorbikebackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestRideServiceImpl implements TestRideService {

    private final TestRideRepository testRideRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    @Value("${spring.mail.username}")
    private String adminEmail;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("MMMM d, yyyy");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("hh:mm a");


    @Override
    public TestRideResponseDto submitRequest(TestRideRequestDto dto, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));

        TestRideRequest request = TestRideRequest.builder()
                .bikeId(dto.getBikeId())
                .bikeName(dto.getBikeName())
                .bikeBrand(dto.getBikeBrand())
                .user(user)
                .phone(dto.getPhone())
                .preferredDate(dto.getPreferredDate())
                .preferredTime(dto.getPreferredTime())
                .notes(dto.getNotes())
                .status(TestRideStatus.PENDING)
                .build();

        TestRideRequest saved = testRideRepository.save(request);

        try {
            emailService.sendTestRideRequestNotificationToAdmin(
                    adminEmail,
                    user.getName(),
                    user.getEmail(),
                    dto.getPhone(),
                    dto.getBikeName(),
                    dto.getBikeBrand(),
                    dto.getPreferredDate().format(DATE_FMT),
                    dto.getPreferredTime().format(TIME_FMT),
                    dto.getNotes(),
                    saved.getId()
            );
        } catch (Exception e) {
            log.error("Failed to send admin notification email for request #{}: {}", saved.getId(), e.getMessage());
        }

        return toDto(saved);
    }

    @Override
    public List<TestRideResponseDto> getAllRequests() {
        return testRideRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public List<TestRideResponseDto> getMyRequests(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found: " + userEmail));
        return testRideRepository.findByUserId(user.getId())
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Override
    public TestRideResponseDto updateStatus(Long requestId, TestRideStatusUpdateDto dto) {
        TestRideRequest request = testRideRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Test ride request not found: " + requestId));

        if (dto.getStatus() == TestRideStatus.PENDING) {
            throw new IllegalArgumentException("Cannot manually set status back to PENDING");
        }

        if (dto.getStatus() == TestRideStatus.DECLINED
                && (dto.getDeclineReason() == null || dto.getDeclineReason().isBlank())) {
            throw new IllegalArgumentException("A decline reason is required when declining a request");
        }

        request.setStatus(dto.getStatus());

        if (dto.getStatus() == TestRideStatus.DECLINED) {
            request.setDeclineReason(dto.getDeclineReason());
        }

        TestRideRequest updated = testRideRepository.save(request);

        String userEmail = updated.getUser().getEmail();
        String userName  = updated.getUser().getName();
        String bikeName  = updated.getBikeName();

        try {
            if (dto.getStatus() == TestRideStatus.CONFIRMED) {
                emailService.sendTestRideConfirmationToUser(
                        userEmail,
                        userName,
                        bikeName,
                        updated.getPreferredDate().format(DATE_FMT),
                        updated.getPreferredTime().format(TIME_FMT)
                );
            } else {
                emailService.sendTestRideDeclineToUser(
                        userEmail,
                        userName,
                        bikeName,
                        dto.getDeclineReason()
                );
            }
        } catch (Exception e) {
            log.error("Failed to send status email to user {} for request #{}: {}",
                    userEmail, requestId, e.getMessage());
        }

        return toDto(updated);
    }

    private TestRideResponseDto toDto(TestRideRequest r) {
        return TestRideResponseDto.builder()
                .id(r.getId())
                .bikeId(r.getBikeId())
                .bikeName(r.getBikeName())
                .bikeBrand(r.getBikeBrand())
                .userId(r.getUser().getId())
                .userName(r.getUser().getName())
                .userEmail(r.getUser().getEmail())
                .phone(r.getPhone())
                .preferredDate(r.getPreferredDate())
                .preferredTime(r.getPreferredTime())
                .notes(r.getNotes())
                .status(r.getStatus())
                .declineReason(r.getDeclineReason())
                .createdAt(r.getCreatedAt())
                .updatedAt(r.getUpdatedAt())
                .build();
    }
}