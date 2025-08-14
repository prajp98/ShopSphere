package com.shopsphere.inventory.controller;

import com.shopsphere.inventory.entity.InventoryItem;
import com.shopsphere.inventory.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/inventory")
public class InventoryController {
    private final InventoryService svc;
    public InventoryController(InventoryService svc){ this.svc = svc; }

    // Anyone authenticated can view
    @GetMapping("/{productId}")
    public ResponseEntity<InventoryItem> get(@PathVariable Long productId) {
        return ResponseEntity.ok(svc.getByProductId(productId));
    }

    // ADMIN adjusts stock
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{productId}/adjust")
    public ResponseEntity<InventoryItem> adjust(@PathVariable Long productId, @RequestParam int delta) {
        return ResponseEntity.ok(svc.upsert(productId, delta));
    }

    // Order-service uses these three in a transaction-like flow
    @PostMapping("/{productId}/reserve")
    public ResponseEntity<Void> reserve(@PathVariable Long productId, @RequestParam int qty) {
        svc.reserve(productId, qty); return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/release")
    public ResponseEntity<Void> release(@PathVariable Long productId, @RequestParam int qty) {
        svc.release(productId, qty); return ResponseEntity.noContent().build();
    }

    @PostMapping("/{productId}/commit")
    public ResponseEntity<Void> commit(@PathVariable Long productId, @RequestParam int qty) {
        svc.commit(productId, qty); return ResponseEntity.noContent().build();
    }
}
