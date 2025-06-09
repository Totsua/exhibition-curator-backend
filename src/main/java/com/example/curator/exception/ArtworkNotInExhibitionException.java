package com.example.curator.exception;

public class ArtworkNotInExhibitionException extends RuntimeException {
    public ArtworkNotInExhibitionException(String message) {
        super(message);
    }
}
