package com.invoicePro.utils;

import com.invoicePro.response.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseUtils {

    public static ResponseEntity<Response> data(final Object entity) {
        return new ResponseEntity<>(Response.builder().status(HttpStatus.OK.value()).data(entity)
                .build(), HttpStatus.OK);
    }
}
