package com.thoughtworks.buddiee.exception;

public class ResourceNotFoundException extends RuntimeException{
    public static final long serialVersionUID = 4328744;
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
