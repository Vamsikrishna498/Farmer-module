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

    // In-memory OTP store (for demo only — replace with Redis/Caffeine in production)
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    /**
     * ✅ Generate a 6-digit OTP, store it, and simulate sending via email or SMS
     */
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

    /**
     * ✅ Manual OTP verification (used with email or phone OTPs)
     */
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

    /**
     * ✅ Firebase OTP/token verification
     */
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

    /**
     * ✅ Extract email or phone from Firebase token (useful for post-verification lookup)
     */
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

    /**
     * ✅ Stub: Simulate sending OTP via email
     */
    private void sendOtpEmail(String email, String otp) {
        log.info("Simulated: OTP email sent to {}: {}", email, otp);
        // Replace with actual email service like JavaMailSender or SendGrid
    }

    /**
     * ✅ Stub: Simulate sending OTP via SMS
     */
    private void sendOtpSms(String phoneNumber, String otp) {
        log.info("Simulated: OTP SMS sent to {}: {}", phoneNumber, otp);
        // Replace with Firebase Phone Auth, Twilio, etc.
    }
}
