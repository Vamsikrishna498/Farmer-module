package com.farmer.farmermanagement.dto;

<<<<<<< HEAD
import jakarta.validation.constraints.Email;
=======
>>>>>>> b8dc8b5a4679b70462404f7421f0ecbebefd2057
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {

    @NotBlank(message = "Email or Phone number is required")
    private String emailOrPhone;
}
