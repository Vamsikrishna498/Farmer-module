package com.farmer.farmermanagement.controller;

import com.farmer.farmermanagement.service.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/otp")
@RequiredArgsConstructor
public class OtpController {

    private final OtpService otpService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String phoneNumber) {
        return ResponseEntity.ok(otpService.generateAndSendOtp(phoneNumber));
    // Generate and send OTP to the phone number
    @PostMapping("/generate")
    public ResponseEntity<String> generateOtp(@RequestParam String phoneNumber) {
        String otp = otpService.generateAndSendOtp(phoneNumber);
        return ResponseEntity.ok("OTP sent successfully to " + phoneNumber);
    }

    // Verify the OTP entered by the user
    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerificationRequest request) {
        // Assuming the OtpVerificationRequest class contains phoneNumber and otp
        boolean isValid = otpService.verifyOtpCode(request.getPhoneNumber(), request.getOtp());
        if (isValid) {
            return ResponseEntity.ok("OTP verified successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid OTP");
        }
    }

    // OTP verification request class (you can extract this to a separate file if necessary)
    public static class OtpVerificationRequest {
        private String phoneNumber;
        private String otp;

        // Getters and Setters
        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }
    }
}
