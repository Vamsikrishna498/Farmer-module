package com.farmer.farmermanagement;

import com.farmer.farmermanagement.service.OtpService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    private FirebaseAuth firebaseAuth;
    private OtpService otpService;

    @BeforeEach
    void setUp() {
        firebaseAuth = mock(FirebaseAuth.class);
        otpService = new OtpService(firebaseAuth);
    }

    @Test
    void testVerifyOtp_validToken_returnsTrue() throws Exception {
        FirebaseToken mockToken = mock(FirebaseToken.class);
        when(firebaseAuth.verifyIdToken("valid_token")).thenReturn(mockToken);

        boolean result = otpService.verifyOtp("valid_token");
        assertTrue(result);
    }

    @Test
    void testVerifyOtp_invalidToken_returnsFalse() throws Exception {
        FirebaseAuthException mockException = mock(FirebaseAuthException.class);
        when(firebaseAuth.verifyIdToken("invalid_token")).thenThrow(mockException);

        boolean result = otpService.verifyOtp("invalid_token");
        assertFalse(result);
    }

    @Test
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
