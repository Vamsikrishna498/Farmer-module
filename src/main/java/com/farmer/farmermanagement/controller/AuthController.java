package com.farmer.farmermanagement.controller;

import com.farmer.farmermanagement.dto.*;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.security.JwtUtil;
import com.farmer.farmermanagement.service.CountryStateCityService;
import com.farmer.farmermanagement.service.EmailService;
import com.farmer.farmermanagement.service.OtpService;
import com.farmer.farmermanagement.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final OtpService otpService;
    private final EmailService emailService;
    private final CountryStateCityService countryService;

    // ✅ Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            String token = jwtUtil.generateToken(authentication);
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);
            return ResponseEntity.ok().headers(headers).body("Logged in successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
                    .body("Login failed: " + e.getMessage());
        }
    }

    // ✅ Register User
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user, "User registered successfully."));
    }

    // ✅ Send OTP (for login/registration/verification)
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String otp = otpService.generateAndSendOtp(emailOrPhone);
        emailService.sendOtpEmail(emailOrPhone, "Your OTP is: " + otp);
        return ResponseEntity.ok("OTP sent successfully to your registered email or phone.");
    }

    // ✅ Verify OTP
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        String otp = request.get("otp");

        if (emailOrPhone == null || otp == null || emailOrPhone.trim().isEmpty() || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email/Phone and OTP are required.");
        }

        if (otpService.verifyOtp(emailOrPhone, otp)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    // ✅ Forgot User ID
    @PostMapping("/forgot-user-id")
    public ResponseEntity<String> forgotUserId(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        return ResponseEntity.ok(userService.forgotUserId(emailOrPhone));
    }

    // ✅ Forgot Password - sends OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        String otp = otpService.generateAndSendOtp(emailOrPhone);
        emailService.sendOtpEmail(emailOrPhone,
                "Your password reset OTP is: " + otp + ". Use this OTP to reset your password. It is valid for 10 minutes.");

        return ResponseEntity.ok("Password reset OTP sent successfully.");
    }

    // ✅ Confirm Password Reset
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmResetPassword(@RequestBody ResetPasswordDTO request) {
        if (request.getEmailOrPhone() == null || request.getOtp() == null || request.getNewPassword() == null ||
            request.getEmailOrPhone().trim().isEmpty() || request.getOtp().trim().isEmpty() || request.getNewPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        boolean resetSuccess = userService.verifyOtpAndResetPassword(
                request.getEmailOrPhone().trim(),
                request.getOtp().trim(),
                request.getNewPassword().trim());

        if (resetSuccess) {
            emailService.sendEmail(new EmailServiceDTO(
                    request.getEmailOrPhone().trim(),
                    "Password Reset Successful",
                    "Your password has been reset successfully. If this wasn't you, please contact support immediately."));
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    // ✅ Get Countries
    @GetMapping("/countries")
    public ResponseEntity<String> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    // ✅ Get States
    @GetMapping("/states/{countryCode}")
    public ResponseEntity<String> getStates(@PathVariable String countryCode) {
        return ResponseEntity.ok(countryService.getStates(countryCode));
    }

    // ✅ Get Districts
    @GetMapping("/districts/{countryCode}/{stateCode}")
    public ResponseEntity<String> getDistricts(@PathVariable String countryCode, @PathVariable String stateCode) {
        return ResponseEntity.ok(countryService.getDistricts(countryCode, stateCode));
    }

    // ✅ Get Address by ZIP
    @GetMapping("/address-by-zip/{countryCode}/{postalCode}")
    public ResponseEntity<String> getAddressByZip(@PathVariable String countryCode, @PathVariable String postalCode) {
        try {
            String addressData = countryService.getZipAddressByCountryAndPostalCode(countryCode, postalCode);
            return ResponseEntity.ok(addressData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch address details: " + e.getMessage());
        }
    }

    // ✅ Test Endpoint
    @GetMapping("/test")
    public String test() {
        return "This is a test endpoint.";
    }
}
