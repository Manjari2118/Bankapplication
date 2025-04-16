package com.example.studapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlerClass {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Object> handleRuntime(RuntimeException ex) {
        if (ex.getMessage().contains("Insufficient amount")) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST); // Return 400 for insufficient balance
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND); // Default 404 for other RuntimeExceptions
    }
}
