package com.farmer.farmermanagement.dto;

import java.time.LocalDate;
import java.util.Optional;

import com.farmer.farmermanagement.entity.User;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String country;
    private String state;
    private String pinCode;
    private String timeZone;
    private String token;
    private String message;

    // Basic conversion without message/token
    public static UserResponseDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .firstName(Optional.ofNullable(user.getFirstName()).orElse(""))
                .lastName(Optional.ofNullable(user.getLastName()).orElse(""))
                .email(Optional.ofNullable(user.getEmail()).orElse(""))
                .phoneNumber(Optional.ofNullable(user.getPhoneNumber()).orElse(""))
                .dateOfBirth(user.getDateOfBirth())
                .gender(Optional.ofNullable(user.getGender()).orElse(""))
                .country(Optional.ofNullable(user.getCountry()).orElse(""))
                .state(Optional.ofNullable(user.getState()).orElse(""))
                .pinCode(Optional.ofNullable(user.getPinCode()).orElse(""))
                .timeZone(Optional.ofNullable(user.getTimeZone()).orElse(""))
                .build();
    }

    // Overloaded method to include token
    public static UserResponseDTO fromEntity(User user, String token) {
        UserResponseDTO dto = fromEntity(user);
        if (dto != null) {
            dto.setToken(token);
        }
        return dto;
    }

    // Overloaded method to include message
    public static UserResponseDTO fromEntityWithMessage(User user, String message) {
        UserResponseDTO dto = fromEntity(user);
        if (dto != null) {
            dto.setMessage(message);
        }
        return dto;
    }
}
