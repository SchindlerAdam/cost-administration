package com.schindler.costadministration.exception.exceptions;

public class ModifyUserException extends RuntimeException {

    public ModifyUserException() {
        super("User modification failed! Can not find a user with this email address!");
    }

    public ModifyUserException(String message) {
        super(message);
    }
}
