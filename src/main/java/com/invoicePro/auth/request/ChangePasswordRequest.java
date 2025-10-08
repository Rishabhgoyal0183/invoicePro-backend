package com.invoicePro.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Old password is mandatory")
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "New Password must contain at least one uppercase letter, one lowercase letter, one number, one special character, and be at least of 8 digits."
    )
    @Size(max = 255, message = "New Password must not exceed 255 characters")
    private String newPassword;

    @NotBlank(message = "Confirm password is mandatory")
    @Size(max = 255, message = "Confirm password must not exceed 255 characters")
    private String confirmPassword;
}
