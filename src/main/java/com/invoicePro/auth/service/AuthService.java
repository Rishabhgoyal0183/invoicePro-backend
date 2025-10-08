package com.invoicePro.auth.service;

import com.invoicePro.auth.request.LoginRequest;
import com.invoicePro.response.AuthResponse;

public interface AuthService {

    AuthResponse login(LoginRequest loginRequest);

    void logout(String token);

}
