package com.shopsphere.inventory.exception;

public class InventoryNotFoundException extends RuntimeException {
    public InventoryNotFoundException(Long productId){ super("Inventory not found for productId=" + productId); }
}
