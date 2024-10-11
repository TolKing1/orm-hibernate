package org.tolking.exception;

public class TrainingNotFoundException extends RuntimeException {
    public TrainingNotFoundException(long id) {
        super("Can't find any training with id: %d".formatted(id));
    }
}
