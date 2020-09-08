package com.will.order_management_api.exception;

public class ResultNotFoundException extends RuntimeException{
    public ResultNotFoundException() {
        super();
    }

    public ResultNotFoundException(String message) {
        super(message);
    }

    public ResultNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ResultNotFoundException(Throwable cause) {
        super(cause);
    }
}
