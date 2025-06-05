package com.example.curator.exception;

public class ErrorSendingGETRequestException extends RuntimeException {
    public ErrorSendingGETRequestException(String message) {
        super(message);
    }
}
