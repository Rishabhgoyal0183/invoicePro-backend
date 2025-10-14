package com.invoicePro.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

@Configuration
public class CryptoConfig {

    @Bean
    public static TextEncryptor textEncryptor(
            @Value("${invoicePro.app.encrypt.password}") String password,
            @Value("${invoicePro.app.encrypt.salt}") String salt) {
        return Encryptors.text(password, salt);
    }
}