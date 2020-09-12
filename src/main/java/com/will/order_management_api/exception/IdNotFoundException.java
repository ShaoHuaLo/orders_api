package com.will.order_management_api.exception;

/**
 * IdNotFoundException will be thrown when OrderController.getById() get invalid id parameter,
 * invalid id means our database doesn't have this order.
 * @author Will
 */

public class IdNotFoundException extends RuntimeException{
    public IdNotFoundException() {
        super();
    }

    public IdNotFoundException(String message) {
        super(message);
    }

    public IdNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public IdNotFoundException(Throwable cause) {
        super(cause);
    }
}
