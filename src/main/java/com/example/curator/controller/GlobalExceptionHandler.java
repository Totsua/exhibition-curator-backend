package com.example.curator.controller;

import com.example.curator.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<Object> handleInvalidRequestException(InvalidRequestException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(APIPageOutOfBoundsException.class)
    public ResponseEntity<Object> handleAPIPageOutOfBoundsException(APIPageOutOfBoundsException e){
        return new ResponseEntity<>(e.getMessage(), HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE); //todo: potentially 416 (Range Not Satisfied)
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
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateArtworkException.class)
    public ResponseEntity<Object> handleDuplicateArtworkException(DuplicateArtworkException e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(final MethodArgumentNotValidException ex) {

        final BindingResult bindingResult = ex.getBindingResult();
        final List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        final Map<String, String> errors = new HashMap<>();
        fieldErrors.forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }



}
