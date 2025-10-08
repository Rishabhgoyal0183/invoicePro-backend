package com.invoicePro.auth.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingRequest {

    @Valid
    @NotNull
    private BusinessOwnerDetails businessOwnerDetails;

    @Valid
    @NotNull
    private BusinessDetails businessDetails;

    // =======================
    // Nested classes
    // =======================

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BusinessOwnerDetails {

        @Size(max = 100, message = "First name can have at most 100 characters")
        @NotBlank(message = "First name is required")
        private String firstName;

        @Size(max = 55, message = "Middle name can have at most 55 characters")
        private String middleName;

        @Size(max = 100, message = "Last name can have at most 100 characters")
        @NotBlank(message = "Last name is required")
        private String lastName;

        @Size(max = 20, message = "Gender can have at most 20 characters")
        @NotBlank(message = "Gender is required")
        private String gender;

        @Email
        @NotBlank(message = "Email ID is required")
        @Size(max = 255, message = "Email ID can have at most 255 characters")
        private String emailId;

        @Size(max = 10, message = "Phone number can have at most 10 characters")
        @NotBlank(message = "Phone number is required")
        private String phoneNumber;

        @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
        @NotBlank(message = "Password is required")
        private String password;
    }


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BusinessDetails {

        @NotBlank(message = "Business name is required")
        @Size(max = 255, message = "Business name can have at most 255 characters")
        private String name;

        private Boolean isRegisteredForGst = false;

        @Size(max = 15, message = "GST-IN can have at most 15 characters")
        private String gstin;

        @Size(max = 10, message = "PAN can have at most 10 characters")
        @NotBlank(message = "PAN is required")
        private String pan;

        @Size(max = 50, message = "Business type can have at most 50 characters")
        @NotBlank(message = "Business type is required")
        private String businessType;

        @Size(max = 10000, message = "Address can have at most 10000 characters")
        @NotBlank(message = "Address is required")
        private String address;

        @Size(max = 100, message = "City can have at most 100 characters")
        @NotBlank(message = "City is required")
        private String city;

        @Size(max = 100, message = "State can have at most 100 characters")
        @NotBlank(message = "State is required")
        private String state;

        @Size(max = 6, message = "Pin code can have at most 6 characters")
        @NotBlank(message = "Pin code is required")
        private String pinCode;

        @Size(max = 200, message = "Contact email can have at most 200 characters")
        @Email(message = "Contact email should be valid")
        private String contactEmail;

        @Size(max = 10, message = "Contact phone for Business can have at most 10 characters")
        @NotBlank(message = "Contact phone for Business is required")
        private String contactPhone;
    }
}
