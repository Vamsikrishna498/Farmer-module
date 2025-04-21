package com.farmer.farmermanagement;

import com.farmer.farmermanagement.service.OtpService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OtpServiceTest {
    private FirebaseAuth firebaseAuth;
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        firebaseAuth = mock(FirebaseAuth.class);
        otpService = new OtpService(firebaseAuth);
    }

    @Test
    void generateAndSendOtpShouldReturnOtpCode() {
        String phoneNumber = "1234567890";
        String otp = otpService.generateAndSendOtp(phoneNumber);

        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d{6}"));

        boolean isValid = otpService.verifyOtpCode(phoneNumber, otp);
        assertTrue(isValid);
    }

    @Test
    void verifyOtpShouldReturnTrueForValidIdToken() throws FirebaseAuthException {
        String mockIdToken = "mock-token";
        FirebaseToken mockFirebaseToken = mock(FirebaseToken.class);

        when(firebaseAuth.verifyIdToken(mockIdToken)).thenReturn(mockFirebaseToken);

        boolean result = otpService.verifyOtp(mockIdToken);
    void testVerifyOtp_validToken_returnsTrue() throws Exception {
        FirebaseToken mockToken = mock(FirebaseToken.class);
        when(firebaseAuth.verifyIdToken("valid_token")).thenReturn(mockToken);

        boolean result = otpService.verifyOtp("valid_token");

        assertTrue(result);
    }

    @Test
    void verifyOtpShouldReturnFalseForInvalidIdToken() throws FirebaseAuthException {
        String mockIdToken = "bad-token";
        FirebaseAuthException mockedException = mock(FirebaseAuthException.class);

        when(firebaseAuth.verifyIdToken(mockIdToken)).thenThrow(mockedException);

        boolean result = otpService.verifyOtp(mockIdToken);
    void testVerifyOtp_invalidToken_returnsFalse() throws Exception {
        FirebaseAuthException mockException = mock(FirebaseAuthException.class);
        when(firebaseAuth.verifyIdToken("invalid_token")).thenThrow(mockException);

        boolean result = otpService.verifyOtp("invalid_token");
        assertFalse(result);
    }

    @Test
    void verifyOtpCodeShouldReturnFalseWhenOtpIsInvalid() {
        String phone = "9999999999";
        otpService.generateAndSendOtp(phone);

        boolean result = otpService.verifyOtpCode(phone, "000000"); // Wrong OTP
        assertFalse(result);
    }

    @Test
    void getUserEmailOrPhoneFromTokenShouldReturnEmail() throws FirebaseAuthException {
        String idToken = "mock-token";
        FirebaseToken token = mock(FirebaseToken.class);

        when(token.getEmail()).thenReturn("test@example.com");
        when(firebaseAuth.verifyIdToken(idToken)).thenReturn(token);

        String result = otpService.getUserEmailOrPhoneFromToken(idToken);
        assertEquals("test@example.com", result);
    }

    @Test
    void getUserEmailOrPhoneFromTokenShouldReturnNullOnException() throws FirebaseAuthException {
        String badToken = "bad-token";
        FirebaseAuthException mockedException = mock(FirebaseAuthException.class);

        when(firebaseAuth.verifyIdToken(badToken)).thenThrow(mockedException);

        String result = otpService.getUserEmailOrPhoneFromToken(badToken);
        assertNull(result);
    }
    void testGetUserEmailOrPhoneFromToken_validToken_returnsEmail() throws Exception {
        FirebaseToken mockToken = mock(FirebaseToken.class);
        when(mockToken.getEmail()).thenReturn("test@example.com");
        when(firebaseAuth.verifyIdToken("valid_token")).thenReturn(mockToken);

        String email = otpService.getUserEmailOrPhoneFromToken("valid_token");
        assertEquals("test@example.com", email);
    }

    @Test
    void testGetUserEmailOrPhoneFromToken_invalidToken_returnsNull() throws Exception {
        FirebaseAuthException mockException = mock(FirebaseAuthException.class);
        when(firebaseAuth.verifyIdToken("bad_token")).thenThrow(mockException);

        String result = otpService.getUserEmailOrPhoneFromToken("bad_token");
        assertNull(result);
    }

    @Test
    void testGenerateAndSendOtp_returnsValidOtp() {
        String phoneNumber = "+1234567890";
        String otp = otpService.generateAndSendOtp(phoneNumber);

        assertNotNull(otp);
        assertTrue(otp.matches("\\d{1,6}"));  // Matches 1 to 6 digits
    }
}
