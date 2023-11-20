package com.eicon.demo.services.exceptions;

public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String msg){
        super(msg);
    }

    public EntityNotFoundException(String msg, Throwable cause){
        super(msg, cause);
    }
}
