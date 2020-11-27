package com.thoughtworks.buddiee.exception;

public class BadRequestException extends RuntimeException{
    public static final long serialVersionUID = 4328743;
    public BadRequestException(String message) {
        super(message);
    }
}
