package com.invoicePro.exception;

import com.invoicePro.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;


@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Response> handleAllBadCredentialsException(BadCredentialsException exception) {

        log.error("An Bad Credentials error occurred", exception);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        Response.builder()
                                .status(HttpStatus.UNAUTHORIZED.value())
                                .errors(
                                        List.of(exception.getMessage())
                                ).build());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Response> handleAll(Exception exception) {

        log.error("An unexpected error occurred", exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                Response.builder()
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .errors(
                                List.of("An unexpected error occurred. Please try again later.")
                        ).build());
    }
}
