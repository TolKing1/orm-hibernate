package org.tolking.exception;

public class TimeoutException extends RuntimeException {
    public TimeoutException() {
        super("Request timout. Please try again...");
    }
}
