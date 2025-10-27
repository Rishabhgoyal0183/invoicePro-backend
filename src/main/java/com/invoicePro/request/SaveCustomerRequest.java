package com.invoicePro.request;

import com.invoicePro.enums.State;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class SaveCustomerRequest {
    @NotBlank(message = "Customer name is required")
    @Size(message = "Customer name must be between 1 and 255 characters", min = 1, max = 255)
    private String name;

    @NotNull(message = "Email is required")
    @Size(message = "Email must be between 5 and 100 characters", min = 5, max = 100)
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Phone number is required")
    @Size(message = "Phone number must be 10 digits", min = 10, max = 10)
    private String phoneNumber;

    @NotBlank(message = "Address is required")
    @Size(message = "Address must be between 1 and 255 characters", min = 1, max = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Size(message = "City must be between 1 and 100 characters", min = 1, max = 100)
    private String city;

    @NotNull(message = "State is required")
    private State state;

    @NotBlank(message = "Pin code is required")
    @Size(message = "Pin code must be between 4 and 20 characters", min = 4, max = 20)
    private String pinCode;

    @NotBlank(message = "Country is required")
    @Size(message = "Country must be between 1 and 100 characters", min = 1, max = 100)
    private String country;

    @NotBlank(message = "Customer type is required")
    @Size(message = "Customer type must be between 1 and 50 characters", min = 1, max = 50)
    @Pattern(regexp = "RETAIL|WHOLESALE", message = "Customer type must be either 'RETAIL' or 'WHOLESALE'")
    private String customerType;
}
