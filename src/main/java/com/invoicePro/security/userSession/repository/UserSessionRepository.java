package com.invoicePro.security.userSession.repository;

import com.invoicePro.security.userSession.entity.UserSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserSessionRepository extends JpaRepository<UserSession, Long> {

    @Query("SELECT us FROM UserSession us WHERE us.businessOwnerId = :businessOwnerId AND us.sessionId = :sessionId AND us.expiresAt > CURRENT_TIMESTAMP")
    Optional<UserSession> findValidUserSession(Long businessOwnerId, String sessionId);

    void deleteAllByBusinessOwnerIdAndSessionId(Long businessOwnerId, String sessionId);

    @Query("SELECT u FROM UserSession u ORDER BY u.id ASC")
    List<UserSession> findAllStreamList(Pageable pageable);

}
