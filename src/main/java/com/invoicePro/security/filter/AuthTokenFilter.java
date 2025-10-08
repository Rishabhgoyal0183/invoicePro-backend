package com.invoicePro.security.filter;

import com.invoicePro.security.jwt.JwtUtils;
import com.invoicePro.security.userDetails.BusinessOwnerDetailsServiceImpl;
import com.invoicePro.security.userSession.service.UserSessionService;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Service
public class AuthTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private BusinessOwnerDetailsServiceImpl businessOwnerDetailsService;

    @Autowired
    private UserSessionService userSessionService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String jwt = jwtUtils.parseJwt(request.getHeader("Authorization"));
        if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
            String emailId = jwtUtils.getSubjectFromJwtToken(jwt);

            // Extract information from the JWT
            Integer businessOwnerId = jwtUtils.getBusinessOwnerIdFromJwtToken(jwt);
            String sessionId = jwtUtils.getSessionIdFromJwtToken(jwt);

            setSecurityContext(businessOwnerId, sessionId, emailId, request);
        }

        filterChain.doFilter(request, response);
    }

    private void setSecurityContext(Integer businessOwnerId, String sessionId, String emailId, HttpServletRequest request) {
        // Check if the session is still active in the database
        if (userSessionService.isSessionValid(Long.valueOf(businessOwnerId), sessionId)) {
            UserDetails userDetails = businessOwnerDetailsService.loadUserByUsername(emailId);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            // Set the authentication for the current request
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext(); // NEW context
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        } else {
            log.warn("Session for business Owner Id '{}' and session ID '{}' is invalid.", businessOwnerId, sessionId);
            throw new ExpiredJwtException(null, null, "Your session has expired. Please sign in again to continue.");
        }
    }

}

