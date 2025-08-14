package com.shopsphere.order.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String msg) { super(msg); }
}