package com.example.curator.exception;

public class InvalidArtworkException extends RuntimeException {
    public InvalidArtworkException(String message) {
        super(message);
    }
}
