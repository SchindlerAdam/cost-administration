package com.schindler.costadministration.exception.exceptions;

public class DeleteUserException extends RuntimeException {

    public DeleteUserException() {
        super("Failed to delete user! Can not find a user with this email address!");
    }

    public DeleteUserException(String message) {
        super(message);
    }
}
