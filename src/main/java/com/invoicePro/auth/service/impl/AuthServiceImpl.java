package com.invoicePro.auth.service.impl;

import com.invoicePro.auth.request.LoginRequest;
import com.invoicePro.auth.service.AuthService;
import com.invoicePro.entity.Business;
import com.invoicePro.entity.BusinessOwner;
import com.invoicePro.exception.ResourceNotFoundException;
import com.invoicePro.repository.BusinessOwnerRepository;
import com.invoicePro.repository.BusinessRepository;
import com.invoicePro.response.AuthResponse;
import com.invoicePro.security.jwt.JwtUtils;
import com.invoicePro.security.userDetails.BusinessOwnerDetails;
import com.invoicePro.security.userSession.entity.UserSession;
import com.invoicePro.security.userSession.service.UserSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    private final UserSessionService userSessionService;

    private final JwtUtils jwtUtils;

    private final BusinessOwnerRepository businessOwnerRepository;

    private final BusinessRepository businessRepository;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmailId(), loginRequest.getPassword()));

        // Set authentication in the security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Create user session
        UserSession userSession = userSessionService.createSession(authentication);

        // Generate JWT
        String jwt = jwtUtils.generateJwtToken(authentication, userSession.getSessionId(), userSession.getExpiresAt());

        // Get user details
        BusinessOwnerDetails businessOwnerDetails = (BusinessOwnerDetails) authentication.getPrincipal();
        BusinessOwner businessOwner = businessOwnerRepository.findById(businessOwnerDetails.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Business Owner not found"));

        List<Business> businessList = businessRepository.findByOwner(businessOwner);

        if (!businessList.isEmpty()){
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

    private List<AuthResponse.BusinessDetails> getBusinessDetails(List<Business> businessList) {
        return businessList.stream()
                .map(business -> new AuthResponse.BusinessDetails(business.getId(), business.getName()))
                .toList();
    }

    @Override
    public void logout(String token) {

        String jwtToken = jwtUtils.parseJwt(token);
        int businessOwnerId = jwtUtils.getBusinessOwnerIdFromJwtToken(jwtToken);
        String sessionId = jwtUtils.getSessionIdFromJwtToken(jwtToken);

        // Delete the session
        userSessionService.deleteSession((long) businessOwnerId, sessionId);
    }
}
