package org.tolking.exception;

public class LoginAttemptExceedException extends RuntimeException {
    public LoginAttemptExceedException() {
        super("Login attempt exceeded. Try again later");
    }
}
