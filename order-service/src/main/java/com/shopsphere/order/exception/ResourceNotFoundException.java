package com.shopsphere.order.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String msg){ super(msg); }
}