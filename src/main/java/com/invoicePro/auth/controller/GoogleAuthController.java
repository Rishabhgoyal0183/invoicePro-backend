package com.invoicePro.auth.controller;

import com.invoicePro.auth.request.ChangePasswordRequest;
import com.invoicePro.auth.request.OnboardingRequest;
import com.invoicePro.auth.service.GoogleAuthService;
import com.invoicePro.response.Response;
import com.invoicePro.utils.ExceptionUtils;
import com.invoicePro.utils.ResponseUtils;
import com.invoicePro.validator.RequestValidator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class GoogleAuthController {
    private final GoogleAuthService googleAuthService;

    @GetMapping("/google/verify-email")
    public ResponseEntity<Response> handleSignUp(@RequestParam String authorizationCode) {
        try {
            String emailId = googleAuthService.handleSignUp(authorizationCode);

            return ResponseUtils.data(emailId);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @PostMapping("/onboarding/register")
    public ResponseEntity<Response> onboardBusinessAndOwnerDetails(@RequestBody @Valid OnboardingRequest onboardingRequest, BindingResult bindingResult) {
        RequestValidator.validateRequest(bindingResult);
        try {
            String message = googleAuthService.onboardBusinessAndOwnerDetails(onboardingRequest);

            return ResponseUtils.data(message);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @GetMapping("/google/forgot-password")
    public ResponseEntity<Response> forgotPassword(@RequestParam String authorizationCode) {
        try {
            String emailId = googleAuthService.forgotPassword(authorizationCode);

            return ResponseUtils.data(emailId);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<Response> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                                   BindingResult bindingResult) {
        RequestValidator.validateRequest(bindingResult);
        try {
            String message = googleAuthService.changePassword(changePasswordRequest);

            return ResponseUtils.data(message);
        } catch (Exception exception) {
            return ExceptionUtils.handleException(exception);
        }
    }
}
