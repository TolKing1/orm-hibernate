package org.tolking.exception;

public class BadLoginException extends RuntimeException{
    public BadLoginException() {
        super("Bad login credentials");
    }
}
