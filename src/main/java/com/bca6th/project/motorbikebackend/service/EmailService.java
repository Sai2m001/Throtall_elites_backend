package com.bca6th.project.motorbikebackend.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);

    // Sent to admin when a new test ride request is submitted
    void sendTestRideRequestNotificationToAdmin(
            String adminEmail,
            String userName,
            String userEmail,
            String phone,
            String bikeName,
            String bikeBrand,
            String preferredDate,
            String preferredTime,
            String notes,
            Long requestId
    );

    // Sent to user when admin confirms their request
    void sendTestRideConfirmationToUser(
            String to,
            String userName,
            String bikeName,
            String preferredDate,
            String preferredTime
    );

    // Sent to user when admin declines their request
    void sendTestRideDeclineToUser(
            String to,
            String userName,
            String bikeName,
            String declineReason
    );
}
