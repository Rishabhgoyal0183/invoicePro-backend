package com.invoicePro.auth.service.impl;

import com.invoicePro.auth.request.ChangePasswordRequest;
import com.invoicePro.auth.request.OnboardingRequest;
import com.invoicePro.auth.service.GoogleAuthService;
import com.invoicePro.entity.Business;
import com.invoicePro.entity.BusinessOwner;
import com.invoicePro.entity.PasswordResetToken;
import com.invoicePro.exception.ResourceNotFoundException;
import com.invoicePro.repository.BusinessOwnerRepository;
import com.invoicePro.repository.BusinessRepository;
import com.invoicePro.repository.PasswordResetTokenRepository;
import com.invoicePro.response.AuthResponse;
import com.invoicePro.security.jwt.JwtUtils;
import com.invoicePro.security.userDetails.BusinessOwnerDetails;
import com.invoicePro.security.userSession.entity.UserSession;
import com.invoicePro.security.userSession.service.UserSessionService;
import com.invoicePro.utils.PasswordResetTokenUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleAuthServiceImpl implements GoogleAuthService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String redirectUri;

    @Value("${spring.security.oauth2.client.provider.google.token-uri}")
    private String tokenUri;

    @Value("${spring.security.oauth2.client.provider.google.user-info-uri}")
    private String userInfoUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final BusinessOwnerRepository businessOwnerRepository;
    private final BusinessRepository businessRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordResetTokenUtils passwordResetTokenUtils;
    private final UserSessionService userSessionService;
    private final JwtUtils jwtUtils;

    private static final int TOKEN_EXPIRY_MINUTES = 5;

    @Override
    public String handleSignUp(String authorizationCode, boolean isRegistering) throws BadRequestException {

        try {
            MultiValueMap<String, String> params = getMultiValueMap(authorizationCode);
            HttpHeaders headers = getHttpHeaders();
            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            // Post to token endpoint and get response as String (JSON)
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(tokenUri, request, Map.class);

            String idToken = (String) tokenResponse.getBody().get("id_token");
            String userInfoEndpoint = userInfoUri + idToken.trim();

            ResponseEntity<Map> userInfoResponse = restTemplate.getForEntity(userInfoEndpoint, Map.class);

            if (userInfoResponse.getBody().get("email_verified").equals("false")) {
                throw new BadRequestException("Email not verified by Google. Please check and try again later.");
            }
            String emailId = userInfoResponse.getBody().get("email").toString().trim();

            if (isRegistering) {
                businessOwnerRepository.findByEmailIdAndStatus(emailId, "ACTIVE")
                        .ifPresent(businessOwner -> {
                            throw new IllegalArgumentException("User with this email: " + emailId + " already exists, Please Login instead.");
                        });
            }
            return emailId;

        } catch (HttpClientErrorException exception) {
            throw new BadRequestException("your Google authorization code is invalid or expired, please try again.");
        } catch (Exception exception) {
            throw new BadRequestException("Failed to authenticate with Google: " + exception.getMessage());
        }
    }

    private static HttpHeaders getHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> getMultiValueMap(String authorizationCode) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", authorizationCode);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        return params;
    }

    @Override
    public String onboardBusinessAndOwnerDetails(OnboardingRequest onboardingRequest) {

        BusinessOwner businessOwner = new BusinessOwner();
        businessOwner.setFirstName(onboardingRequest.getBusinessOwnerDetails().getFirstName());
        businessOwner.setMiddleName(onboardingRequest.getBusinessOwnerDetails().getMiddleName());
        businessOwner.setLastName(onboardingRequest.getBusinessOwnerDetails().getLastName());
        String fullName = onboardingRequest.getBusinessOwnerDetails().getFirstName() + " "
                + (onboardingRequest.getBusinessOwnerDetails().getMiddleName() != null ? onboardingRequest.getBusinessOwnerDetails().getMiddleName() + " " : "")
                + onboardingRequest.getBusinessOwnerDetails().getLastName();

        businessOwner.setFullName(fullName);
        businessOwner.setGender(onboardingRequest.getBusinessOwnerDetails().getGender());
        businessOwner.setEmailId(onboardingRequest.getBusinessOwnerDetails().getEmailId());
        businessOwner.setPhoneNumber(onboardingRequest.getBusinessOwnerDetails().getPhoneNumber());
        businessOwner.setPassword(getEncryptedPassword(onboardingRequest.getBusinessOwnerDetails().getPassword()));
        businessOwner.setCreatedAt(LocalDateTime.now());
        businessOwner.setCreatedBy(getCurrentBusinessOwner().getId());
        businessOwner.setUpdatedAt(LocalDateTime.now());

        BusinessOwner savedOwner = businessOwnerRepository.save(businessOwner);
        Business business = saveBusinessDetailsForTheOwner(onboardingRequest, savedOwner);
        businessRepository.save(business);

        return "Onboarding completed successfully!";
    }

    @Override
    public String forgotPassword(String authorizationCode) throws BadRequestException {

        String email = handleSignUp(authorizationCode, false);
        validateBusinessOwnerAndGetIt(email);
        String token = passwordResetTokenUtils.generateToken(email);

        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .token(token)
                .email(email)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(TOKEN_EXPIRY_MINUTES))
                .isUsed(false)
                .build();

        passwordResetTokenRepository.save(passwordResetToken);
        return token;
    }

    @Override
    @Transactional
    public String changePassword(String passwordResetToken, ChangePasswordRequest changePasswordRequest) throws BadRequestException {
        validatePasswordResetToken(passwordResetToken);
        String email = passwordResetTokenUtils.extractEmailFromToken(passwordResetToken);

        BusinessOwner businessOwner = validateBusinessOwnerAndGetIt(email);

        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("New password and Confirm password do not match.");
        }
        businessOwner.setPassword(getEncryptedPassword(changePasswordRequest.getNewPassword()));
        businessOwner.setUpdatedAt(LocalDateTime.now());
        businessOwner.setUpdatedBy(businessOwner.getId());
        businessOwnerRepository.save(businessOwner);

        return "Password changed successfully!";
    }

    @Override
    public AuthResponse loginWithGoogle(String authorizationCode) throws BadRequestException {
        String emailId = handleSignUp(authorizationCode, false);

        BusinessOwner businessOwner = validateBusinessOwnerAndGetIt(emailId);

        BusinessOwnerDetails businessOwnerDetails = BusinessOwnerDetails.build(businessOwner);

        // Create user session
        UserSession userSession = userSessionService.createSession(businessOwnerDetails);

        // Generate JWT
        String jwt = jwtUtils.generateJwtToken(businessOwnerDetails, userSession.getSessionId(), userSession.getExpiresAt());

        List<Business> businessList = businessRepository.findByOwner(businessOwner);

        if (!businessList.isEmpty()) {
            // Create AuthResponse with user details and JWT
            AuthResponse authResponse = new AuthResponse();
            authResponse.setAccessToken(jwt);
            authResponse.setBusinessOwnerName(businessOwner.getFullName());
            authResponse.setBusinessOwnerEmail(businessOwnerDetails.getEmail());
            authResponse.setBusinessOwnerPhoneNumber(businessOwnerDetails.getPhoneNumber());
            authResponse.setBusinessOwnerId(businessOwnerDetails.getId());
            authResponse.setBusinessCount(businessList.size());
            authResponse.setBusinessDetails(getBusinessDetails(businessList));
            return authResponse;
        }
        throw new ResourceNotFoundException("You don't have any business registered. Please contact to admin.");
    }

    private BusinessOwner validateBusinessOwnerAndGetIt(String email) throws BadRequestException {
        Optional<BusinessOwner> businessOwnerOptional = businessOwnerRepository.findByEmailId(email);
        if (businessOwnerOptional.isEmpty()) {
            throw new BadRequestException("No business owner found with the provided email, Please Register first.");
        }
        return businessOwnerOptional.get();
    }

    private List<AuthResponse.BusinessDetails> getBusinessDetails(List<Business> businessList) {
        return businessList.stream()
                .map(business -> new AuthResponse.BusinessDetails(business.getId(), business.getName()))
                .toList();
    }

    private void validatePasswordResetToken(String passwordResetToken) throws BadRequestException {
        Optional<PasswordResetToken> tokenOptional = passwordResetTokenRepository.findByToken(passwordResetToken);
        if (tokenOptional.isEmpty()) {
            throw new BadRequestException("Invalid password reset token.");
        }
        PasswordResetToken token = tokenOptional.get();
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Password reset token has expired.");
        }
        if (token.isUsed()) {
            throw new BadRequestException("This password reset token already has been used.");
        }
        // Mark the token as used
        token.setUsed(true);
        passwordResetTokenRepository.save(token);
    }

    private static String getEncryptedPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(password);
    }

    private BusinessOwner getCurrentBusinessOwner() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        BusinessOwnerDetails businessOwnerDetails = (BusinessOwnerDetails) auth.getPrincipal();
        return businessOwnerRepository.findById(businessOwnerDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }


    private Business saveBusinessDetailsForTheOwner(OnboardingRequest onboardingRequest, BusinessOwner savedOwner) {
        Business business = new Business();
        business.setOwner(savedOwner);
        business.setName(onboardingRequest.getBusinessDetails().getName());
        business.setIsRegisteredForGst(onboardingRequest.getBusinessDetails().getIsRegisteredForGst());
        business.setGstin(onboardingRequest.getBusinessDetails().getGstin());
        business.setPan(onboardingRequest.getBusinessDetails().getPan());
        business.setBusinessType(onboardingRequest.getBusinessDetails().getBusinessType());
        business.setAddress(onboardingRequest.getBusinessDetails().getAddress());
        business.setCity(onboardingRequest.getBusinessDetails().getCity());
        business.setState(onboardingRequest.getBusinessDetails().getState());
        business.setPinCode(onboardingRequest.getBusinessDetails().getPinCode());
        business.setContactEmail(onboardingRequest.getBusinessDetails().getContactEmail());
        business.setContactPhone(onboardingRequest.getBusinessDetails().getContactPhone());
        business.setCreatedAt(LocalDateTime.now());
        business.setCreatedBy(getCurrentBusinessOwner().getId());
        business.setUpdatedAt(LocalDateTime.now());
        return business;
    }
}
