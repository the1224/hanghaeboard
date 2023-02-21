package com.sparta.hanghaememo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(value = { IllegalArgumentException.class })
    public ResponseEntity<Object> handleApiRequestException(IllegalArgumentException ex) {
        Exceptions exceptions = new Exceptions();
        exceptions.setHttpStatus(HttpStatus.BAD_REQUEST);
        exceptions.setErrorMessage(ex.getMessage());


        return new ResponseEntity(
                exceptions,
                HttpStatus.BAD_REQUEST
        );
    }
}