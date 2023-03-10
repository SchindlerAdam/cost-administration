package com.schindler.costadministration.exception.exceptions;

public class AuthenticationException extends RuntimeException {

    public AuthenticationException() {
        super("Authentication failed! Can not find a user with this email address!");
    }

    public AuthenticationException(String message) {
        super(message);
    }
}
