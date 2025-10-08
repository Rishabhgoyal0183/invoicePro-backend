package com.invoicePro.validator;

import com.invoicePro.exception.ValidationException;
import lombok.Data;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class RequestValidator {

    /**
     * Validates the request and throws a ValidationException if there are binding errors.
     *
     * @param bindingResult BindingResult object containing validation results
     * @throws ValidationException if validation errors are present
     */
    public static void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errors = bindingResult.getFieldErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            throw new ValidationException(errors);
        }
    }
}
