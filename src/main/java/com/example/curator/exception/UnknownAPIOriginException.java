package com.example.curator.exception;

public class UnknownAPIOriginException extends RuntimeException {
    public UnknownAPIOriginException(String message) {
        super(message);
    }
}
