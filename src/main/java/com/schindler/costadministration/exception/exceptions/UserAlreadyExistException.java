package com.schindler.costadministration.exception.exceptions;

public class UserAlreadyExistException extends RuntimeException {

    public UserAlreadyExistException() {
        super("User with this email is already exists!");
    }

    public UserAlreadyExistException(String message) {
        super(message);
    }
}
