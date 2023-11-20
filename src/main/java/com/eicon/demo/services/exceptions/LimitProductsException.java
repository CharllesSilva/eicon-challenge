package com.eicon.demo.services.exceptions;

public class LimitProductsException extends RuntimeException {

    public LimitProductsException(String msg) {
        super(msg);
    }

    public LimitProductsException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

