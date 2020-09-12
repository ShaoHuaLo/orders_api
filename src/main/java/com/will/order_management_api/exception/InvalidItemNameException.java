package com.will.order_management_api.exception;

/**
 * InvalidItemNameException will be thrown when client enter wrong product_name
 * eg. when our database has product "pizza", then this exception will be thrown when client type in "piza" or "pissa"
 * @author Will
 */


public class InvalidItemNameException extends RuntimeException{
    public InvalidItemNameException() {
        super();
    }

    public InvalidItemNameException(String message) {
        super(message);
    }

    public InvalidItemNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidItemNameException(Throwable cause) {
        super(cause);
    }
}
