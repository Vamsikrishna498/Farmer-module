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

    // In-memory OTP store — ideally use Redis/Caffeine with expiry
    private final Map<String, String> otpStore = new ConcurrentHashMap<>();

    // Used only if you're doing Firebase ID token verification (not manual OTP)
    public boolean verifyOtp(String idToken) {
        try {
            FirebaseToken decodedToken = firebaseAuth.verifyIdToken(idToken);
            log.info("OTP verification successful for user: {}", decodedToken.getEmail());
            return decodedToken != null;
        } catch (FirebaseAuthException e) {
            log.warn("OTP verification failed: {}", e.getMessage());
            return false;
        }
    }

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

    public String generateAndSendOtp(String phoneNumber) {
        // Generate a random 6-digit OTP
        String otp = String.format("%06d", new Random().nextInt(999999));
        
        // Store the OTP in the memory 
        otpStore.put(phoneNumber, otp);
        
        // Log the generated OTP for debugging
        log.info("Generated OTP for phone {}: {}", phoneNumber, otp);
        
        // Call the method to send OTP (for now, it just logs the OTP)
        sendOtpThroughServerSide(phoneNumber, otp);
        
        return otp;
    }

    // Method to verify OTP
    public boolean verifyOtpCode(String phoneNumber, String otp) {
        String storedOtp = otpStore.get(phoneNumber);
        boolean isValid = storedOtp != null && storedOtp.equals(otp);

        log.info("Verifying OTP for phone {}: provided={}, expected={}, result={}",
                phoneNumber, otp, storedOtp, isValid);

        if (isValid) {
            otpStore.remove(phoneNumber); // OTP is single-use
            log.info("OTP verified and removed from store for phone {}", phoneNumber);
        }

        return isValid;
    }

    // Method to simulate sending OTP through server-side (could be replaced with actual service like Firebase, etc.)
    private void sendOtpThroughServerSide(String phoneNumber, String otp) {
        log.info("OTP sent to phone {}: {}", phoneNumber, otp);
    }
}
