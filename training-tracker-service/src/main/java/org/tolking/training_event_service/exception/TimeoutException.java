package org.tolking.training_event_service.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException() {
        super("Request timout. Please try again...");
    }
}
