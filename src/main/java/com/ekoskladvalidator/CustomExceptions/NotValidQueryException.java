package com.ekoskladvalidator.CustomExceptions;

public class NotValidQueryException extends Exception {
    public NotValidQueryException(String message) {
        super(message);
    }
}
