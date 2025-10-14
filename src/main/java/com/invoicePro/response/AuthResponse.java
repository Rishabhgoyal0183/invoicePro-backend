package com.invoicePro.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private Long businessOwnerId;
    private String businessOwnerName;
    private String businessOwnerEmail;
    private String businessOwnerPhoneNumber;
    private Integer businessCount;
    private List<BusinessDetails> businessDetails;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class BusinessDetails {
        private Long businessId;
        private String businessName;
    }
}