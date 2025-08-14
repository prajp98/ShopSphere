package com.shopsphere.inventory.exception;

public class OutOfStockException extends RuntimeException {
    public OutOfStockException(Long productId, int want, int have) {
        super("Out of stock for productId=" + productId + " want=" + want + " available=" + have);
    }
}
