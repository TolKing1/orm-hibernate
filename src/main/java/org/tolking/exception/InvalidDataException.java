package org.tolking.exception;

import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.stream.Collectors;

public class InvalidDataException extends RuntimeException{
    public InvalidDataException(BindingResult bindingResult) {
        super(listErrors(bindingResult));
    }

    public static String listErrors(BindingResult bindingResult) {
         return bindingResult.getAllErrors().stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(", "));
    }
}
