package com.shopsphere.order.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name="inventory-service", url="http://localhost:8084/inventory")
public interface InventoryClient {
    @PostMapping("/inventory/{productId}/reserve")
    void reserve(@PathVariable Long productId, @RequestParam int qty);

    @PostMapping("/inventory/{productId}/release")
    void release(@PathVariable Long productId, @RequestParam int qty);

    @PostMapping("/inventory/{productId}/commit")
    void commit(@PathVariable Long productId, @RequestParam int qty);
}