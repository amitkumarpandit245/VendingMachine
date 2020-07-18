package com.deloitte.exceptions;

/**
 * Exception class to handle Item not available exception
 */
public class ItemNotAvailableException extends RuntimeException {
    private String message;

    public ItemNotAvailableException(String string) {
        this.message = string;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
