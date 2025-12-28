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
}
