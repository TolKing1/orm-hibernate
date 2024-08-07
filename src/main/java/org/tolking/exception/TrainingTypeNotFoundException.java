package org.tolking.exception;

public class TrainingTypeNotFoundException extends RuntimeException{
    public TrainingTypeNotFoundException(String name) {
        super("Can't find training type with name: %s".formatted(name));
    }
}
