package com.invoicePro.auth.service;

import com.invoicePro.auth.request.ChangePasswordRequest;
import com.invoicePro.auth.request.OnboardingRequest;
import org.apache.coyote.BadRequestException;

public interface GoogleAuthService {

    String handleSignUp(String authorizationCode) throws BadRequestException;

    String onboardBusinessAndOwnerDetails(OnboardingRequest onboardingRequest);

    String forgotPassword(String authorizationCode) throws BadRequestException;

    String changePassword(ChangePasswordRequest changePasswordRequest) throws BadRequestException;
}
