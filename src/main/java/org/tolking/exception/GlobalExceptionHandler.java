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
    public static final HttpStatus UN_AUTH = HttpStatus.UNAUTHORIZED;

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ApiError> handleInvalidDataException(InvalidDataException e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> handleUserNotFoundException(UserNotFoundException e, HttpServletRequest request) {
        return handleException(e, request, NOT_FOUND);
    }

    @ExceptionHandler(TrainerNotFoundException.class)
    public ResponseEntity<ApiError> handleTrainerNotFoundException(TrainerNotFoundException e, HttpServletRequest request) {
        return handleException(e, request, NOT_FOUND);
    }

    @ExceptionHandler(TraineeNotFoundException.class)
    public ResponseEntity<ApiError> handleTraineeNotFoundException(TraineeNotFoundException e, HttpServletRequest request) {
        return handleException(e, request, NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(Exception e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(Exception e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
    }

    @ExceptionHandler(BadLoginException.class)
    public ResponseEntity<ApiError> handleAuthn(Exception e, HttpServletRequest request) {
        return handleException(e, request, UN_AUTH);
    }

    @ExceptionHandler(LoginAttemptSucceedException.class)
    public ResponseEntity<ApiError> handleBruteForce(Exception e, HttpServletRequest request) {
        return handleException(e, request, UN_AUTH);
    }

    private ResponseEntity<ApiError> handleException(Exception e, HttpServletRequest request, HttpStatus status) {
        log(e);

        ApiError apiError = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                status.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }

    private static void log(Throwable exception) {
        log.error("{} occurred: {}", exception.getClass().getName(), exception.getMessage());
    }


}
