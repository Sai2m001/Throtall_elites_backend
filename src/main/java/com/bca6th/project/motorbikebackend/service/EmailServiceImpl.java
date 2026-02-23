package com.bca6th.project.motorbikebackend.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService{

    private final JavaMailSender mailSender;

    @Override
    public void sendOtpEmail(String to, String otp) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try{
            helper.setTo(to);
            helper.setSubject("Your OTP for Dhamaka Throat-all Motorbike Login");
            helper.setText("""
                    <h2>Your OTP is : <strong>%s</strong></h2>
                    <p>It is valid for 5 minutes.<p>
                    """.formatted(otp),true);
        }catch (MessagingException me){
            throw new RuntimeException("Failed to send OTP email", me);
        }
        mailSender.send(message);
    }
    @Override
    public void sendTestRideRequestNotificationToAdmin(
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
    ) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(adminEmail);
            helper.setSubject("🏍️ New Test Ride Request — %s %s".formatted(bikeBrand, bikeName));
            helper.setText("""
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                      <div style="background: #111; padding: 24px 32px;">
                        <h1 style="color: #f6e05e; margin: 0; font-size: 1.5rem; letter-spacing: 1px;">
                          THROT-ALL ELITES
                        </h1>
                        <p style="color: #888; margin: 4px 0 0; font-size: 0.85rem;">Admin Notification</p>
                      </div>

                      <div style="background: #fff; padding: 32px;">
                        <h2 style="color: #111; margin-top: 0;">New Test Ride Request</h2>
                        <p style="color: #555;">
                          A new test ride booking has been submitted and is waiting for your review.
                        </p>

                        <div style="background: #f9fafb; border-left: 4px solid #dc2626;
                                    padding: 20px 24px; border-radius: 4px; margin: 24px 0;">
                          <table style="width: 100%%; border-collapse: collapse;">
                            <tr>
                              <td style="padding: 6px 0; color: #888; width: 140px; font-size: 0.875rem;">Request ID</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">#%d</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Customer</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Email</td>
                              <td style="padding: 6px 0; color: #111;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Phone</td>
                              <td style="padding: 6px 0; color: #111;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Bike</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">%s %s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Preferred Date</td>
                              <td style="padding: 6px 0; color: #111;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Preferred Time</td>
                              <td style="padding: 6px 0; color: #111;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem; vertical-align: top;">Notes</td>
                              <td style="padding: 6px 0; color: #111;">%s</td>
                            </tr>
                          </table>
                        </div>

                        <a href="http://localhost:3000/dashboard/test-rides"
                           style="display: inline-block; background: #dc2626; color: white;
                                  padding: 12px 28px; border-radius: 4px; text-decoration: none;
                                  font-weight: 700; font-size: 0.95rem; letter-spacing: 0.5px;">
                          Review in Dashboard →
                        </a>
                      </div>

                      <div style="background: #f3f4f6; padding: 16px 32px; text-align: center;">
                        <p style="color: #9ca3af; font-size: 0.8rem; margin: 0;">
                          Throt-All Elites · Tindobato, Banepa, Nepal
                        </p>
                      </div>
                    </div>
                    """.formatted(
                    requestId, userName, userEmail, phone,
                    bikeBrand, bikeName, preferredDate, preferredTime,
                    notes != null && !notes.isBlank() ? notes : "—"
            ), true);
        } catch (MessagingException me) {
            throw new RuntimeException("Failed to send admin notification email", me);
        }
        mailSender.send(message);
    }

    @Override
    public void sendTestRideConfirmationToUser(
            String to,
            String userName,
            String bikeName,
            String preferredDate,
            String preferredTime
    ) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject("✅ Your Test Ride is Confirmed — %s".formatted(bikeName));
            helper.setText("""
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                      <div style="background: #111; padding: 24px 32px;">
                        <h1 style="color: #f6e05e; margin: 0; font-size: 1.5rem; letter-spacing: 1px;">
                          THROT-ALL ELITES
                        </h1>
                      </div>

                      <div style="background: #fff; padding: 32px;">
                        <h2 style="color: #111; margin-top: 0;">Your Test Ride is Confirmed! 🏍️</h2>
                        <p style="color: #555;">Hi <strong>%s</strong>,</p>
                        <p style="color: #555;">
                          Great news! Your test ride request has been <strong style="color: #16a34a;">confirmed</strong>
                          by our team. We look forward to seeing you.
                        </p>

                        <div style="background: #f0fdf4; border-left: 4px solid #16a34a;
                                    padding: 20px 24px; border-radius: 4px; margin: 24px 0;">
                          <table style="width: 100%%; border-collapse: collapse;">
                            <tr>
                              <td style="padding: 6px 0; color: #888; width: 120px; font-size: 0.875rem;">Bike</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Date</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Time</td>
                              <td style="padding: 6px 0; color: #111; font-weight: 600;">%s</td>
                            </tr>
                            <tr>
                              <td style="padding: 6px 0; color: #888; font-size: 0.875rem;">Location</td>
                              <td style="padding: 6px 0; color: #111;">Tindobato, Banepa, Kavrepalanchok</td>
                            </tr>
                          </table>
                        </div>

                        <p style="color: #555;">
                          Please bring a valid <strong>driving licence</strong> on the day.
                          If you need to reschedule, contact us via WhatsApp at
                          <a href="https://wa.me/9779823141414" style="color: #dc2626;">9823141414</a>.
                        </p>
                      </div>

                      <div style="background: #f3f4f6; padding: 16px 32px; text-align: center;">
                        <p style="color: #9ca3af; font-size: 0.8rem; margin: 0;">
                          Throt-All Elites · Tindobato, Banepa, Nepal
                        </p>
                      </div>
                    </div>
                    """.formatted(userName, bikeName, preferredDate, preferredTime), true);
        } catch (MessagingException me) {
            throw new RuntimeException("Failed to send confirmation email", me);
        }
        mailSender.send(message);
    }

    // ─── New: User decline email ──────────────────────────────────────────────

    @Override
    public void sendTestRideDeclineToUser(
            String to,
            String userName,
            String bikeName,
            String declineReason
    ) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(to);
            helper.setSubject("Update on your Test Ride Request — %s".formatted(bikeName));
            helper.setText("""
                    <div style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                      <div style="background: #111; padding: 24px 32px;">
                        <h1 style="color: #f6e05e; margin: 0; font-size: 1.5rem; letter-spacing: 1px;">
                          THROT-ALL ELITES
                        </h1>
                      </div>

                      <div style="background: #fff; padding: 32px;">
                        <h2 style="color: #111; margin-top: 0;">Test Ride Request Update</h2>
                        <p style="color: #555;">Hi <strong>%s</strong>,</p>
                        <p style="color: #555;">
                          Unfortunately, we are unable to accommodate your test ride request for
                          <strong>%s</strong> at this time.
                        </p>

                        <div style="background: #fef2f2; border-left: 4px solid #dc2626;
                                    padding: 20px 24px; border-radius: 4px; margin: 24px 0;">
                          <p style="margin: 0; color: #888; font-size: 0.875rem; margin-bottom: 8px;">
                            Reason from our team:
                          </p>
                          <p style="margin: 0; color: #111;">%s</p>
                        </div>

                        <p style="color: #555;">
                          We'd love to help you find another time. Please reach out via WhatsApp at
                          <a href="https://wa.me/9779823141414" style="color: #dc2626;">9823141414</a>
                          and we'll get you sorted.
                        </p>
                      </div>

                      <div style="background: #f3f4f6; padding: 16px 32px; text-align: center;">
                        <p style="color: #9ca3af; font-size: 0.8rem; margin: 0;">
                          Throt-All Elites · Tindobato, Banepa, Nepal
                        </p>
                      </div>
                    </div>
                    """.formatted(userName, bikeName, declineReason), true);
        } catch (MessagingException me) {
            throw new RuntimeException("Failed to send decline email", me);
        }
        mailSender.send(message);
    }
}
