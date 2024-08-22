package org.tolking.exception;

public class TraineeNotFoundException extends RuntimeException{
    public TraineeNotFoundException(String message) {
        super("Can't find any trainee with username: "+ message);
    }
}

