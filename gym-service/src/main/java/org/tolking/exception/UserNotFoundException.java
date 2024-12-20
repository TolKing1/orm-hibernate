package org.tolking.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String username) {
        super("Can't find any user with provided username(%s) and password".formatted(username));
    }
}
