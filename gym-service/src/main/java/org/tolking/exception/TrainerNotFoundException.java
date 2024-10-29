package org.tolking.exception;

public class TrainerNotFoundException extends RuntimeException {
    public TrainerNotFoundException(String message) {
        super("Can't find any trainer with username: " + message);
    }
}
