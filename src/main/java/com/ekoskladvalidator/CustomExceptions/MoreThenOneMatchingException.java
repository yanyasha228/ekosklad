package com.ekoskladvalidator.CustomExceptions;

public class MoreThenOneMatchingException extends Exception {
    public MoreThenOneMatchingException(String message) {
        super(message);
    }
}
