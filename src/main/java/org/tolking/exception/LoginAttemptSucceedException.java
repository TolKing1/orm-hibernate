package org.tolking.exception;

public class LoginAttemptSucceedException extends RuntimeException{
    public LoginAttemptSucceedException() {
        super("Login attempt exceeded. Try again later");
    }
}
