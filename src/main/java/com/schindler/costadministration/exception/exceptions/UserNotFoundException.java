package com.schindler.costadministration.exception.exceptions;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException() {
        super("Can not find a user with this email address!");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
