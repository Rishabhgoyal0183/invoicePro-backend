package com.invoicePro.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.invoicePro.response.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Slf4j
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {

        log.error("Unauthorized error: {}", authException.getMessage());
//        authException.printStackTrace();

        // Build a custom response using your global Response class
        Response resp = Response.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .errors(List.of( "Your session is not authenticated or has expired. Please sign in again to continue.",
                        "If you still see this error, try refreshing the page or contact support."
                ))
                .build();

        // Convert the response object to JSON string
        String jsonResponse = objectMapper.writeValueAsString(resp);

        // Set the response details and write the JSON
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
