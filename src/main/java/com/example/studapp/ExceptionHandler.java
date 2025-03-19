package com.example.studapp;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;

public class ExceptionHandler {
    @RestControllerAdvice
    public class Exceptioncls {

        // Handle Resource Not Found Exception (Invalid ID)
        @org.springframework.web.bind.annotation.ExceptionHandler(RuntimeException.class)
        public ResponseEntity<Object> handleResourceNotFound(RuntimeException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
