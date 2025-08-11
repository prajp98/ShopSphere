package com.shopsphere.inventory.service;

import com.shopsphere.inventory.model.InventoryItem;
import com.shopsphere.inventory.repository.InventoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public List<InventoryItem> getAll() {
        return repo.findAll();
    }

    public InventoryItem getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found: " + id));
    }

    public InventoryItem getBySku(String sku) {
        return repo.findBySku(sku)
                .orElseThrow(() -> new EntityNotFoundException("Inventory item not found for sku: " + sku));
    }

    public InventoryItem create(InventoryItem item) {
        if (repo.existsBySku(item.getSku())) {
            throw new IllegalArgumentException("SKU already exists: " + item.getSku());
        }
        try {
            if (item.getQuantity() == null) item.setQuantity(0);
            return repo.save(item);
        } catch (DataIntegrityViolationException ex) {
            throw new DataIntegrityViolationException("Constraint violation when creating item");
        }
    }

    @Transactional
    public InventoryItem update(Long id, InventoryItem update) {
        InventoryItem existing = getById(id);
        if (!existing.getSku().equals(update.getSku()) && repo.existsBySku(update.getSku())) {
            throw new IllegalArgumentException("Another item already uses SKU: " + update.getSku());
        }
        existing.setSku(update.getSku());
        existing.setName(update.getName());
        existing.setDescription(update.getDescription());
        existing.setLocation(update.getLocation());
        existing.setQuantity(update.getQuantity() == null ? existing.getQuantity() : update.getQuantity());
        return repo.save(existing);
    }

    @Transactional
    public InventoryItem adjustQuantityBySku(String sku, int delta) {
        InventoryItem existing = getBySku(sku);
        int newQty = (existing.getQuantity() == null ? 0 : existing.getQuantity()) + delta;
        if (newQty < 0) throw new IllegalArgumentException("Quantity cannot be negative");
        existing.setQuantity(newQty);
        return repo.save(existing);
    }

    public void delete(Long id) {
        InventoryItem item = getById(id);
        repo.delete(item);
    }
}
