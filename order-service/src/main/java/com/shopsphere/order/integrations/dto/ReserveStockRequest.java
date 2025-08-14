package com.shopsphere.order.integrations.dto;

import lombok.Data;

@Data
public class ReserveStockRequest {
    private Long productId;
    private int quantity;
    private String reference; // e.g., "order:{orderIdTemporary or clientKey}"
}