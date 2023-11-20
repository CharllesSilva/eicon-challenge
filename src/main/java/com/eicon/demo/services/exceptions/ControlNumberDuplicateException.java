package com.eicon.demo.services.exceptions;

public class ControlNumberDuplicateException extends RuntimeException {
    public ControlNumberDuplicateException(String msg){
        super(msg);
    }

    public ControlNumberDuplicateException(String msg, Throwable cause){
        super(msg, cause);
    }
}
