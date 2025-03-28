package com.farmer.farmermanagement.service;

import com.farmer.farmermanagement.dto.UserDTO;
import com.farmer.farmermanagement.entity.User;
import com.farmer.farmermanagement.exception.UserNotFoundException;
import com.farmer.farmermanagement.repository.UserRepository;
import com.farmer.farmermanagement.utils.OtpUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Registers a new user
     */
    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Use YYYY-MM-DD.");
        }
    }
    public User registerUser(UserDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());

        if (userRepository.findByEmailOrPhoneNumber(userDTO.getEmail(), userDTO.getPhoneNumber()).isPresent()) {
            throw new RuntimeException("User already exists with this email or phone number.");
        }

        User user = User.builder()
            .firstName(userDTO.getFirstName())
            .lastName(userDTO.getLastName())
            .email(userDTO.getEmail())
            .phoneNumber(userDTO.getPhoneNumber())
            .password(passwordEncoder.encode(userDTO.getPassword()))
            .dateOfBirth(parseDate(userDTO.getDateOfBirth())) // Convert String to LocalDate
            .gender(userDTO.getGender())
            .country(userDTO.getCountry())
            .state(userDTO.getState())
            .pinCode(userDTO.getPinCode())
            .timeZone(userDTO.getTimeZone())
            .build();

    return userRepository.save(user);
}

    /**
     * Handles forgot password request
     */
    public String forgotPassword(String emailOrPhone) {
        log.info("Processing forgot password for: {}", emailOrPhone);

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found with given email or phone number.");
        }

        // Generate and send OTP
        return otpService.generateAndSendOtp(emailOrPhone);
    }

    /**
     * Handles forgot User ID request
     */
    public String forgotUserId(String emailOrPhone) {
        log.info("Processing forgot user ID request for: {}", emailOrPhone);

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone);
        if (user.isEmpty()) {
            throw new UserNotFoundException("User not found.");
        }

        // Generate OTP for verification
        return otpService.generateAndSendOtp(emailOrPhone);
    }

    /**
     * Verifies OTP and resets password
     */
    public boolean verifyOtpAndResetPassword(String emailOrPhone, String otp, String newPassword) {
        log.info("Verifying OTP for password reset for: {}", emailOrPhone);

        if (otpService.verifyOtp(emailOrPhone, otp)) {
            User user = userRepository.findByEmailOrPhoneNumber(emailOrPhone, emailOrPhone)
                    .orElseThrow(() -> new UserNotFoundException("User not found."));

            user.setPassword(passwordEncoder.encode(newPassword));
            userRepository.save(user);

            log.info("Password reset successful for user: {}", emailOrPhone);
            return true;
        }

        log.warn("Invalid or expired OTP for: {}", emailOrPhone);
        return false;
    }
}
