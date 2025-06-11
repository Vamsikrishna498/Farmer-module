package com.farmer.farmermanagement.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpService {

    private final FirebaseAuth firebaseAuth;

    // In-memory store — for production use Redis/Caffeine with expiration
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    // ✅ Firebase token verification for Firebase-authenticated flows
    public boolean verifyOtp(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            log.info("Firebase token verified for user: {}", decodedToken.getEmail());
            return decodedToken != null;
        } catch (FirebaseAuthException e) {
            log.warn("Firebase token verification failed: {}", e.getMessage());
            return false;
        }
    }

    // ✅ Verify OTP sent manually via email or phone
    public boolean verifyOtp(String emailOrPhone, String otp) {
        String storedOtp = otpStore.get(emailOrPhone);
        boolean isValid = storedOtp != null && storedOtp.equals(otp);

        log.info("Verifying OTP for {}: provided={}, expected={}, result={}",
                emailOrPhone, otp, storedOtp, isValid);

        if (isValid) {
            otpStore.remove(emailOrPhone); // OTP is single-use
            log.info("OTP verified and removed from store for {}", emailOrPhone);
        }

        return isValid;
    }

    // ✅ Extract email/phone from Firebase token (for Firebase-auth flows)
    public String getUserEmailOrPhoneFromToken(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            String emailOrPhone = decodedToken != null ? decodedToken.getEmail() : null;
            log.info("Extracted user identifier from token: {}", emailOrPhone);
            return emailOrPhone;
        } catch (FirebaseAuthException e) {
            log.error("Failed to extract user identifier from token: {}", e.getMessage());
            return null;
        }
    }

    // ✅ Generate and send OTP (auto-detects email or phone)
    public String generateAndSendOtp(String emailOrPhone) {
        String otp = String.format("%06d", new Random().nextInt(999999));
        otpStore.put(emailOrPhone, otp);

        boolean isEmail = emailOrPhone.contains("@");
        log.info("Generated OTP for {}: {}", isEmail ? "email" : "phone", otp);

        if (isEmail) {
            sendOtpEmail(emailOrPhone, otp);
        } else {
            sendOtpSms(emailOrPhone, otp);
        }

        return otp;
    }

    // ✅ Simulate sending OTP via email (log only — replace with SMTP integration)
    private void sendOtpEmail(String email, String otp) {
        log.info("OTP email sent to {}: {}", email, otp);
        // Integrate JavaMailSender or SendGrid here
    }

    // ✅ Simulate sending OTP via SMS (log only — replace with Firebase/SMS service)
    private void sendOtpSms(String phoneNumber, String otp) {
        log.info("OTP SMS sent to {}: {}", phoneNumber, otp);
        // Integrate Firebase phone auth or Twilio here
    }

    // ✅ Alternative explicit verification method (optional)
    public boolean verifyOtpCode(String phoneNumber, String otp) {
        return verifyOtp(phoneNumber, otp);
    }
}
