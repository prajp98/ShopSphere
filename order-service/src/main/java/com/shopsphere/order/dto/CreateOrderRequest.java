package com.shopsphere.order.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateOrderRequest {
    private List<Item> items; // productId + qty

    @Data
    public static class Item {
        private Long productId;
        private Integer quantity;
    }
}
