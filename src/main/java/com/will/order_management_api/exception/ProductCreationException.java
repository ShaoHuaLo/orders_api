package com.will.order_management_api.exception;

public class ProductCreationException extends RuntimeException{
    public ProductCreationException() {
        super();
    }

    public ProductCreationException(String message) {
        super(message);
    }

    public ProductCreationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProductCreationException(Throwable cause) {
        super(cause);
    }
}
