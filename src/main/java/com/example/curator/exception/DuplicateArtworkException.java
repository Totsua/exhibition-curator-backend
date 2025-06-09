package com.example.curator.exception;

public class DuplicateArtworkException extends RuntimeException {
    public DuplicateArtworkException(String message) {
        super(message);
    }
}
