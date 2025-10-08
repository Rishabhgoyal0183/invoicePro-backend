package com.invoicePro.security.userSession.service;

import com.invoicePro.security.userDetails.BusinessOwnerDetails;
import com.invoicePro.security.userSession.entity.UserSession;
import com.invoicePro.security.userSession.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserSessionService {

    @Value("${invoicePro.app.jwtExpirationMs}")
    private Long jwtExpirationMs;

    private final UserSessionRepository userSessionRepository;

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int SESSION_ID_LENGTH = 10;

    public UserSession createSession(Authentication authentication) {

        BusinessOwnerDetails businessOwnerDetails = (BusinessOwnerDetails) authentication.getPrincipal();

          // delete records from user_sessions by user id
//        userSessionRepository.deleteByUserId(userPrincipal.getId());

        UserSession newUserSession = new UserSession();
        newUserSession.setBusinessOwnerId(businessOwnerDetails.getId());
        newUserSession.setSessionId(generateSessionId());
        newUserSession.setExpiresAt(new Date((new Date()).getTime() + jwtExpirationMs));
        newUserSession.setStatus("Active");
        newUserSession.setCreatedAt(LocalDateTime.now());
        newUserSession.setCreatedBy(businessOwnerDetails.getId());

        return userSessionRepository.save(newUserSession);
    }

    public boolean isSessionValid(Long businessOwnerId, String sessionId) {
        Optional<UserSession> userSession = userSessionRepository.findValidUserSession(businessOwnerId, sessionId);
        return userSession.isPresent();
    }

    public static String generateSessionId() {
        Random random = new Random();
        StringBuilder sessionId = new StringBuilder(SESSION_ID_LENGTH);
        for (int i = 0; i < SESSION_ID_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sessionId.append(CHARACTERS.charAt(index));
        }
        return sessionId.toString();
    }

    @Transactional
    public void deleteSession(Long businessOwnerId, String sessionId) {
        userSessionRepository.deleteAllByBusinessOwnerIdAndSessionId(businessOwnerId, sessionId);
    }
}

