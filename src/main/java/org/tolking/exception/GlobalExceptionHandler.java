package org.tolking.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiError> handleInvalidDataException(InvalidDataException exception, HttpServletRequest request) {
        log.error("InvalidDataException occurred: {}", exception.getMessage());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getLocalizedMessage(),
                BAD_REQUEST.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException exception, HttpServletRequest request) {
        log.error("UserNotFoundException occurred: {}", exception.getMessage());

        HttpStatus unauthorized = HttpStatus.UNAUTHORIZED;
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getLocalizedMessage(),
                unauthorized.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, unauthorized);
    }

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ApiError> handleTrainerNotFoundException(TrainerNotFoundException exception, HttpServletRequest request) {
        log.error("TrainerNotFoundException occurred: {}", exception.getMessage());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getLocalizedMessage(),
                NOT_FOUND.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(TraineeNotFoundException.class)
    public ResponseEntity<ApiError> handleTraineeNotFoundException(TraineeNotFoundException exception, HttpServletRequest request) {
        log.error("TraineeNotFoundException occurred: {}", exception.getMessage());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                exception.getLocalizedMessage(),
                NOT_FOUND.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(Exception e, HttpServletRequest request) {
        log.error("IllegalArgumentException occurred: {}", e.getMessage());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                BAD_REQUEST.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(Exception e, HttpServletRequest request) {
        log.error("ConstraintViolationException occurred: {}", e.getMessage());

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                BAD_REQUEST.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }
}
