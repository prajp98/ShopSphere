package com.shopsphere.inventory.controller;

import com.shopsphere.inventory.model.InventoryItem;
import com.shopsphere.inventory.service.InventoryService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/inventory")
public class InventoryController {

    private final InventoryService svc;

    public InventoryController(InventoryService svc) {
        this.svc = svc;
    }

    @GetMapping
    public ResponseEntity<List<InventoryItem>> listAll() {
        return ResponseEntity.ok(svc.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventoryItem> getById(@PathVariable Long id) {
        return ResponseEntity.ok(svc.getById(id));
    }

    @GetMapping("/sku/{sku}")
    public ResponseEntity<InventoryItem> getBySku(@PathVariable String sku) {
        return ResponseEntity.ok(svc.getBySku(sku));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<InventoryItem> create(@Valid @RequestBody InventoryItem item) {
        InventoryItem created = svc.create(item);
        return ResponseEntity.created(URI.create("/inventory/" + created.getId())).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<InventoryItem> update(@PathVariable Long id, @Valid @RequestBody InventoryItem item) {
        InventoryItem updated = svc.update(id, item);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sku/{sku}/adjust")
    public ResponseEntity<InventoryItem> adjustQty(@PathVariable String sku, @RequestParam int delta) {
        InventoryItem updated = svc.adjustQuantityBySku(sku, delta);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        svc.delete(id);
        return ResponseEntity.noContent().build();
    }
}
