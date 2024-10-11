package org.tolking.training_event_service.exception;

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
    public static final HttpStatus INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    @ExceptionHandler(TimeoutException.class)
    public ResponseEntity<ApiError> handleTimeoutException(TimeoutException ex, HttpServletRequest request){
        return handleException(ex, request, HttpStatus.REQUEST_TIMEOUT);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(Exception e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleValidation(Exception e, HttpServletRequest request) {
        return handleException(e, request, BAD_REQUEST);
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

    private static void log(Throwable exception) {
        log.error("{} occurred: {}", exception.getClass().getName(), exception.getMessage());
    }


}
