package com.farmer.farmermanagement.controller;

import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.farmer.farmermanagement.dto.LoginRequest;
import com.farmer.farmermanagement.dto.ResetPasswordDTO;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.dto.UserResponseDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.security.JwtUtil;
import com.farmer.farmermanagement.service.CountryStateCityService;
import com.farmer.farmermanagement.service.EmailService;
import com.farmer.farmermanagement.service.OtpService;
import com.farmer.farmermanagement.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    // Login endpoint
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginRequest request) {
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

    // Registration endpoint
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserDTO userDTO) {
        User user = userService.registerUser(userDTO);
        return ResponseEntity.ok(UserResponseDTO.fromEntity(user, "User registered successfully."));
    }

    // Send OTP endpoint
    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String otp = otpService.generateAndSendOtp(emailOrPhone);
        emailService.sendOtpEmail(emailOrPhone, otp); // Currently sends email only
        return ResponseEntity.ok("OTP sent successfully to your registered email.");
    }

    // Verify OTP endpoint
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        String otp = request.get("otp");

        if (emailOrPhone == null || emailOrPhone.trim().isEmpty() || otp == null || otp.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email/Phone and OTP are required.");
        }

        if (otpService.verifyOtp(emailOrPhone, otp)) {
            return ResponseEntity.ok("OTP verified successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    // Forgot User ID
    @PostMapping("/forgot-user-id")
    public ResponseEntity<String> forgotUserId(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }
        String result = userService.forgotUserId(emailOrPhone);
        return ResponseEntity.ok(result);
    }

    // Forgot password - sends OTP
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        // Generate OTP for password reset
        String otp = otpService.generateAndSendOtp(emailOrPhone);

        // Send OTP email
        emailService.sendOtpEmail(emailOrPhone,
                "Your password reset OTP is: " + otp +
                ". Use this OTP to reset your password. It is valid for 10 minutes.");

        return ResponseEntity.ok("Password reset OTP sent successfully to your registered email or phone.");
    }

    // Reset password with OTP - generate OTP
    @PostMapping("/reset-password")
    public ResponseEntity<String> sendResetPasswordOtp(@RequestBody Map<String, String> request) {
        String emailOrPhone = request.get("emailOrPhone");
        if (emailOrPhone == null || emailOrPhone.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Email or phone number is required.");
        }

        String otp = otpService.generateAndSendOtp(emailOrPhone.trim());

        emailService.sendOtpEmail(emailOrPhone.trim(),
                "Your OTP for password reset is: " + otp + ". It is valid for 10 minutes.");

        return ResponseEntity.ok("Password reset OTP sent successfully to your registered email or phone.");
    }

    // Confirm password reset with OTP and new password
    @PostMapping("/reset-password/confirm")
    public ResponseEntity<String> confirmResetPassword(@RequestBody ResetPasswordDTO request) {

        if (request.getEmailOrPhone() == null || request.getOtp() == null || request.getNewPassword() == null ||
            request.getEmailOrPhone().trim().isEmpty() || request.getOtp().trim().isEmpty() || request.getNewPassword().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("All fields are required.");
        }

        boolean resetSuccess = userService.verifyOtpAndResetPassword(
            request.getEmailOrPhone().trim(),
            request.getOtp().trim(),
            request.getNewPassword());

        if (resetSuccess) {
            emailService.sendEmail(
                new com.farmer.farmermanagement.dto.EmailServiceDTO(
                    request.getEmailOrPhone().trim(),
                    "Password Reset Successful",
                    "Dear user,\n\nYour password has been changed successfully. If you did not perform this action, please contact support immediately.\n\nRegards,\nDigitalAgristack Team"
                )
            );
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired OTP.");
        }
    }

    // Get countries
    @GetMapping("/countries")
    public ResponseEntity<String> getCountries() {
        return ResponseEntity.ok(countryService.getCountries());
    }

    // Get states by countryCode
    @GetMapping("/states/{countryCode}")
    public ResponseEntity<String> getStates(@PathVariable String countryCode) {
        return ResponseEntity.ok(countryService.getStates(countryCode));
    }

    // Get districts by countryCode and stateCode
    @GetMapping("/districts/{countryCode}/{stateCode}")
    public ResponseEntity<String> getDistricts(@PathVariable String countryCode, @PathVariable String stateCode) {
        return ResponseEntity.ok(countryService.getDistricts(countryCode, stateCode));
    }

    // New endpoint - Get address by ZIP (using countryCode + postalCode)
    @GetMapping("/address-by-zip/{countryCode}/{postalCode}")
    public ResponseEntity<String> getAddressByZip(
            @PathVariable String countryCode,
            @PathVariable String postalCode) {
        try {
            String addressData = countryService.getZipAddressByCountryAndPostalCode(countryCode, postalCode);
            return ResponseEntity.ok(addressData);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to fetch address details: " + e.getMessage());
        }
    }

    // Test endpoint
    @GetMapping("/test")
    public String test() {
        return "This is a test endpoint.";
    }
}
