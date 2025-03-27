package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.entity.Otp;
import com.farmer.farmermanagement.repository.OtpRepository;
import com.farmer.farmermanagement.utils.OtpUtil;
import com.farmer.farmermanagement.utils.TwilioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;
    private final TwilioService twilioService;
    private final OtpUtil otpUtil;

    /**
     * Generates an OTP, stores it, and sends it via Twilio or email.
     * 
     * @param emailOrPhone Email or phone number to send OTP.
     * @return Success message.
     */
    public String generateAndSendOtp(String emailOrPhone) {
        String otpCode = otpUtil.generateOtp(emailOrPhone); // Fix: Pass emailOrPhone
       
        Otp otpEntity = Otp.builder()
                .emailOrPhone(emailOrPhone)
                .otpCode(otpCode) // Fix: Use correct field name
                .expiryTime(LocalDateTime.now().plusMinutes(5)) // OTP valid for 5 minutes
                .build();

        otpRepository.save(otpEntity);

        // Send OTP via Twilio or Email
        twilioService.sendOtp(emailOrPhone, otpCode);

        return "OTP sent successfully.";
    }

    /**
     * Verifies an OTP and deletes it upon successful validation.
     * 
     * @param emailOrPhone Email or phone number used for OTP.
     * @param otpCode The OTP entered by the user.
     * @return True if OTP is valid, false otherwise.
     */
    public boolean verifyOtp(String emailOrPhone, String otpCode) {
        Optional<Otp> otpEntity = otpRepository.findByEmailOrPhoneAndOtpCode(emailOrPhone, otpCode); // Fix: Correct method signature

        if (otpEntity.isPresent() && otpEntity.get().getExpiryTime().isAfter(LocalDateTime.now())) {
            otpRepository.delete(otpEntity.get()); // Fix: Use delete() with entity
            return true;
        }

        return false;
    }
}
