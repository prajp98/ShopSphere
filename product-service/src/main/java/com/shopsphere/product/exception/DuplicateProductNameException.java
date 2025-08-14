package com.shopsphere.product.exception;

public class DuplicateProductNameException extends RuntimeException {
    public DuplicateProductNameException(String name){ super("Product name already exists: " + name); }
}