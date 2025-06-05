package com.example.curator.controller;

import com.example.curator.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(APIPageOutOfBoundsException.class)
    public ResponseEntity<Object> handleAPIPageOutOfBoundsException(APIPageOutOfBoundsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnknownAPIOriginException.class)
    public ResponseEntity<Object> handleUnknownAPIOriginException(UnknownAPIOriginException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(ErrorSendingGETRequestException.class)
    public ResponseEntity<Object> handleErrorSendingGETRequestException(ErrorSendingGETRequestException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidArtworkException.class)
    public ResponseEntity<Object> handleInvalidArtworkException(InvalidArtworkException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(InvalidExhibitionException.class)
    public ResponseEntity<Object> handleInvalidExhibitionException(InvalidExhibitionException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
