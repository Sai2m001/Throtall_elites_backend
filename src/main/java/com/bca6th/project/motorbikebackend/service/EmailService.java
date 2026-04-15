package com.bca6th.project.motorbikebackend.service;

public interface EmailService {
    void sendOtpEmail(String to, String otp);

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

    void sendTestRideConfirmationToUser(
            String to,
            String userName,
            String bikeName,
            String preferredDate,
            String preferredTime
    );

    void sendTestRideDeclineToUser(
            String to,
            String userName,
            String bikeName,
            String declineReason
    );
}
