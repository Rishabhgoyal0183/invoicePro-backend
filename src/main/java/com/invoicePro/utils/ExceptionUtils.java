package com.invoicePro.utils;

import com.invoicePro.exception.ResourceNotFoundException;
import com.invoicePro.response.Response;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;

import java.util.List;

public class ExceptionUtils {

    private static final Logger log = LoggerFactory.getLogger(ExceptionUtils.class);

    public static ResponseEntity<Response> handleException(Exception exception) {
        log.error("Something went wrong, Please try again: ", exception);
        HttpStatus status =
                exception instanceof ResourceNotFoundException ? HttpStatus.NOT_FOUND :
                        exception instanceof IllegalArgumentException ? HttpStatus.BAD_REQUEST :
                                exception instanceof AccessDeniedException ? HttpStatus.FORBIDDEN :
                                        exception instanceof BadCredentialsException || exception instanceof BadRequestException ? HttpStatus.UNAUTHORIZED :
                                                exception instanceof HttpClientErrorException httpEx ? (HttpStatus) httpEx.getStatusCode() :
                                                        (exception instanceof RestClientException) ? HttpStatus.SERVICE_UNAVAILABLE :
                                                                exception instanceof RuntimeException ? HttpStatus.NOT_ACCEPTABLE
                                                                        : HttpStatus.INTERNAL_SERVER_ERROR;

        String message =
                exception instanceof ResourceNotFoundException ? exception.getMessage() :
                        exception instanceof IllegalArgumentException ? exception.getMessage() :
                                exception instanceof AccessDeniedException ? "You are not authorized to access this resource." :
                                        exception instanceof BadCredentialsException ? "Invalid username or password." :
                                                exception instanceof HttpClientErrorException httpEx ?
                                                        (!httpEx.getResponseBodyAsString().isBlank()
                                                                ? "Remote server returned an error: " + httpEx.getResponseBodyAsString() + ", Please try again later."
                                                                : "Remote server returned an error (" + httpEx.getStatusCode().value() + ")") :
                                                        (exception instanceof RestClientException) ?
                                                                "Unable to connect to the remote server. Please try again later." :
                                                                exception instanceof BadRequestException ? exception.getMessage() :
                                                                        exception instanceof RuntimeException ? exception.getMessage() :
                                                                                "Unexpected error, Please try again later.";

        Response resp = Response.builder()
                .status(status.value())
                .errors(List.of(message))
                .build();

        return ResponseEntity.status(status).body(resp);
    }
}
