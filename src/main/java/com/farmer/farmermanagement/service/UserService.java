package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.EmailServiceDTO;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.exception.UserAlreadyExistsException;
import com.farmer.farmermanagement.exception.UserNotFoundException;
import com.farmer.farmermanagement.mapper.UserMapper;
import com.farmer.farmermanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final OtpService otpService; // Firebase OTP service
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    // ✅ Register a new user
    public User registerUser(UserDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());

        if (userRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber()).isPresent()) {
            log.warn("User already exists with email: {} or phone: {}", userDTO.getEmail(), userDTO.getPhoneNumber());
            throw new UserAlreadyExistsException("User already exists with this email or phone number.");
        }

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        User savedUser = userRepository.save(user);

        log.info("User registered successfully with ID: {}", savedUser.getId());

        try {
            emailService.sendRegistrationEmail(savedUser.getEmail(), savedUser.getFirstName());
            log.info("Registration email sent to {}", savedUser.getEmail());
        } catch (Exception e) {
            log.error("Failed to send registration email to {}: {}", savedUser.getEmail(), e.getMessage());
        }

        return savedUser;
    }

    // ✅ Forgot User ID - Sends OTP
    public String forgotUserId(String emailOrPhone) {
        log.info("Processing forgot user ID request for: {}", emailOrPhone);

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);
        if (user.isEmpty()) {
            log.warn("User not found for forgot user ID request: {}", emailOrPhone);
            throw new UserNotFoundException("User not found.");
        }

        String otpResponse = otpService.generateAndSendOtp(emailOrPhone);
        log.info("OTP sent for forgot user ID: {}", otpResponse);
        return otpResponse;
    }

    // ✅ Forgot Password - Sends OTP
    public String forgotPassword(String emailOrPhone) {
        log.info("Processing forgot password for: {}", emailOrPhone);

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);
        if (user.isEmpty()) {
            log.warn("User not found for forgot password request: {}", emailOrPhone);
            throw new UserNotFoundException("User not found with given email or phone number.");
        }

        String otpResponse = otpService.generateAndSendOtp(emailOrPhone);
        log.info("OTP sent for forgot password: {}", otpResponse);
        return otpResponse;
    }

    // ✅ Verify OTP and Reset Password
    public boolean verifyOtpAndResetPassword(String idToken, String emailOrPhone, String newPassword) {
        log.info("Verifying OTP for password reset.");

        if (otpService.verifyOtp(idToken)) {
            User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                    .orElseThrow(() -> {
                        log.warn("User not found for OTP verification: {}", emailOrPhone);
                        return new UserNotFoundException("User not found.");
                    });

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            log.info("Password reset successful for user: {}", emailOrPhone);

            // Send password reset confirmation
            try {
                EmailServiceDTO emailDto = EmailServiceDTO.builder()
                        .to(user.getEmail())
                        .subject("Password Reset Confirmation")
                        .body("Dear " + Optional.ofNullable(user.getFirstName()).orElse("User") + ",\n\n"
                                + "Your password has been changed successfully.\n"
                                + "If you did not perform this action, please contact support immediately.\n\n"
                                + "Regards,\nFarmer Management Team")
                        .build();

                emailService.sendEmail(emailDto);
                log.info("Password reset confirmation email sent to {}", user.getEmail());
            } catch (Exception e) {
                log.error("Failed to send password reset confirmation email to {}: {}", user.getEmail(), e.getMessage());
            }

            return true;
        }

        log.warn("Invalid or expired OTP.");
        return false;
    }

    // ✅ Extract user identifier (email/phone) from Firebase token
    private String extractUserIdentifierFromToken(String idToken) {
        return otpService.getUserEmailOrPhoneFromToken(idToken);
    }
}
