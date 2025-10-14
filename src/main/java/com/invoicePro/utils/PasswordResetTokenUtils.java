package com.invoicePro.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PasswordResetTokenUtils {
    private final TextEncryptor textEncryptor;

    // If you want a single opaque token that includes encrypted email + random UUID:
    public String generateToken(String email) {

        String encryptedEmail = textEncryptor.encrypt(email); // returns a string (base64-like)
        String random = UUID.randomUUID().toString().replace("-", "");
        // Combine — e.g.: random + "." + encryptedEmail
        return random + "." + Base64.getUrlEncoder().withoutPadding().encodeToString(encryptedEmail.getBytes(StandardCharsets.UTF_8));
    }

    public String extractEmailFromToken(String token) {
        String[] parts = token.split("\\.", 2);
        if (parts.length != 2) throw new IllegalArgumentException("Invalid password refresh token format");
        String encryptedBase64 = parts[1];
        String encryptedEmail = new String(Base64.getUrlDecoder().decode(encryptedBase64), StandardCharsets.UTF_8);
        return textEncryptor.decrypt(encryptedEmail);
    }
}
