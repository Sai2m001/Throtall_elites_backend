package com.bca6th.project.motorbikebackend.controller;

import com.bca6th.project.motorbikebackend.dto.contact.ContactRequestDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/contact")
@CrossOrigin(origins = "http://localhost:3000")
public class ContactController {

    @PostMapping
    public ResponseEntity<String> submitContactForm(@Valid @RequestBody ContactRequestDto request) {
        log.info("📧 Contact form received from: {} ({})", request.getName(), request.getEmail());
        log.info("📝 Message: {}", request.getMessage());

        return ResponseEntity.ok("Message received successfully");
    }
}