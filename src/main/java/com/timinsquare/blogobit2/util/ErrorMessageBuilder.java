package com.timinsquare.blogobit2.util;

import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

@Component
public class ErrorMessageBuilder {
    public String of(BindingResult bindingResult) {
        StringBuilder errorMessage = new StringBuilder();
        List<FieldError> errorList = bindingResult.getFieldErrors();
        errorList.forEach(error ->
                errorMessage
                        .append(error.getField())
                        .append(": ")
                        .append(error.getDefaultMessage())
                        .append("; ")
        );
        return errorMessage.toString().trim();
    }
}
