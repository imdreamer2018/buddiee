package com.thoughtworks.buddiee.exception;

public class AuthorizationException extends RuntimeException{
    public static final long serialVersionUID = 4328745;
    public AuthorizationException(String message) {
        super(message);
    }
}
