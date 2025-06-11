package com.farmer.farmermanagement.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.farmer.farmermanagement.dto.EmailServiceDTO;
import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.exception.UserAlreadyExistsException;
import com.farmer.farmermanagement.exception.UserNotFoundException;
import com.farmer.farmermanagement.mapper.UserMapper;
import com.farmer.farmermanagement.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final EmailService emailService;

    // Register a new user
    public User registerUser(UserDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());

        userRepository.findByEmail(userDTO.getEmail()).ifPresent(user -> {
            log.warn("Attempted registration with existing email: {}", userDTO.getEmail());
            throw new UserAlreadyExistsException("Email already registered: " + userDTO.getEmail());
        });

        User user = userMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
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

    // Forgot User ID via email or phone
    public String forgotUserId(String emailOrPhone) {
        log.info("Forgot user ID request for: {}", emailOrPhone);

        User user = findUserByEmailOrPhone(emailOrPhone);

        try {
            emailService.sendUserIdEmail(user.getEmail(), String.valueOf(user.getId()));
            log.info("User ID email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send user ID email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send user ID email.");
        }

        return "User ID sent to your registered email.";
    }

    // Forgot Password - send OTP
    public String forgotPassword(String emailOrPhone) {
        log.info("Forgot password request for: {}", emailOrPhone);

        User user = findUserByEmailOrPhone(emailOrPhone);

        String otp = otpService.generateAndSendOtp(user.getEmail());

        try {
            emailService.sendOtpEmail(user.getEmail(), otp);
            log.info("OTP sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send OTP email to {}: {}", user.getEmail(), e.getMessage());
            throw new RuntimeException("Failed to send OTP email.");
        }

        return "OTP sent successfully to your registered email.";
    }

    // Verify OTP and reset password (no invalidateOtp call needed)
    public boolean verifyOtpAndResetPassword(String emailOrPhone, String otp, String newPassword) {
        log.info("Verifying OTP and resetting password for: {}", emailOrPhone);

        if (!otpService.verifyOtp(emailOrPhone, otp)) {
            log.warn("Invalid or expired OTP for: {}", emailOrPhone);
            return false;
        }

        User user = findUserByEmailOrPhone(emailOrPhone);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        log.info("Password reset successfully for user: {}", emailOrPhone);

        // Send password reset confirmation email
        try {
            String subject = "Password Reset Confirmation";
            String body = "Dear " + (user.getFirstName() != null ? user.getFirstName() : "User") + ",\n\n"
                    + "Your password has been changed successfully.\n"
                    + "If you did not perform this action, please contact support immediately.\n\n"
                    + "Regards,\nFarmer Management Team";

            EmailServiceDTO emailDto = EmailServiceDTO.builder()
                    .to(user.getEmail())
                    .subject(subject)
                    .body(body)
                    .build();

            emailService.sendEmail(emailDto);

            log.info("Password reset confirmation email sent to {}", user.getEmail());
        } catch (Exception e) {
            log.error("Failed to send password reset confirmation email to {}: {}", user.getEmail(), e.getMessage());
        }

        return true;
    }

    // Helper method to find user by email or phone
    private User findUserByEmailOrPhone(String emailOrPhone) {
        return userRepository.findByEmail(emailOrPhone)
                .or(() -> userRepository.findByPhoneNumber(emailOrPhone))
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with email or phone: " + emailOrPhone));
    }
}
