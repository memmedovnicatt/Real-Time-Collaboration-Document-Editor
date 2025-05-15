package com.nicat.realtimecollaborationdocumenteditor.model.exception.child;

public class PasswordMismatchException extends RuntimeException {
    public PasswordMismatchException(String message) {
        super(message);
    }
}
