package org.tolking.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    public static final HttpStatus NOT_FOUND = HttpStatus.NOT_FOUND;
    public static final HttpStatus BAD_REQUEST = HttpStatus.BAD_REQUEST;
    public static final HttpStatus UN_AUTH = HttpStatus.UNAUTHORIZED;
    public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    private static void log(Throwable exception) {
        log.error("{} occurred: {}", exception.getClass().getName(), exception.getMessage(), exception);
    }

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

    @ExceptionHandler(TrainingNotFoundException.class)
    public ResponseEntity<ApiError> handleTrainingNotFoundException(TrainingNotFoundException e, HttpServletRequest request) {
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

    @ExceptionHandler({InvalidFormatException.class, HttpMessageNotReadableException.class, MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ApiError> handleInvalidJson(Exception e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
    }

    @ExceptionHandler({BadLoginException.class, JwtException.class})
    public ResponseEntity<ApiError> handleAuth(Exception e, HttpServletRequest request) {
        return handleException(e, request, UN_AUTH);
    }

    @ExceptionHandler(LoginAttemptExceedException.class)
    public ResponseEntity<ApiError> handleBruteForce(Exception e, HttpServletRequest request) {
        return handleException(e, request, UN_AUTH);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ApiErrorWithoutMessage> handleNullPointer(Exception e, HttpServletRequest request) {
        return handleGenericException(e, request, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorWithoutMessage> handleGenericException(Exception e, HttpServletRequest request) {
        return handleGenericException(e, request, INTERNAL_SERVER_ERROR);
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

    private ResponseEntity<ApiErrorWithoutMessage> handleGenericException(Exception e, HttpServletRequest request, HttpStatus status) {
        log(e);

        ApiErrorWithoutMessage apiError = new ApiErrorWithoutMessage(
                request.getRequestURI(),
                status.name(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(apiError, status);
    }


}
