package com.shopsphere.order.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Map;

// com.shopsphere.order.exception.GlobalExceptionHandler
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> notFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(404).body(Map.of("error","Not Found","message",ex.getMessage()));
    }
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> forbidden(ForbiddenException ex) {
        return ResponseEntity.status(403).body(Map.of("error","Forbidden","message",ex.getMessage()));
    }
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> conflict(ConflictException ex) {
        return ResponseEntity.status(409).body(Map.of("error","Conflict","message",ex.getMessage()));
    }
    @ExceptionHandler(ExternalServiceException.class)
    public ResponseEntity<?> badGateway(ExternalServiceException ex) {
        return ResponseEntity.status(502).body(Map.of("error","Bad Gateway","message",ex.getMessage()));
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> fallback(Exception ex) {
        ex.printStackTrace();
        return ResponseEntity.status(500).body(Map.of("error","Internal Server Error","message",ex.getMessage()));
    }
}

