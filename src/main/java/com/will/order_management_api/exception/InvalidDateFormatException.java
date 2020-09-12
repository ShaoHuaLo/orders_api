package com.will.order_management_api.exception;

/**
 * InvalidDateFormatException will be thrown when clients try to enter invalid date format
 * eg abqw-lkja-123 or 2020-11-13-23 etc.
 * @author Will
 */

public class InvalidDateFormatException extends RuntimeException{
    public InvalidDateFormatException() {
        super();
    }

    public InvalidDateFormatException(String message) {
        super(message);
    }

    public InvalidDateFormatException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidDateFormatException(Throwable cause) {
        super(cause);
    }
}
