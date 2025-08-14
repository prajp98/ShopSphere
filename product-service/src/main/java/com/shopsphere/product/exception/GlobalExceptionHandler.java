package com.shopsphere.product.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> notFound(EntityNotFoundException e) {
        return ResponseEntity.status(404).body(e.getMessage());
    }

    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<String> duplicate(DuplicateProductNameException e) {
        return ResponseEntity.status(409).body(e.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> badReq(MethodArgumentNotValidException e) {
        return ResponseEntity.badRequest().body("Invalid payload");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> other(Exception e) {
        return ResponseEntity.status(500).body("Internal Error: " + e.getMessage());
    }
}