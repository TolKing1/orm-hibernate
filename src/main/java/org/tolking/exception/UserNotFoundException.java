package org.tolking.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String username) {
        super("Can't find any user with username = %s".formatted(username));
    }
}
