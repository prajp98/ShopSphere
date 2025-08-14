package com.shopsphere.inventory.service;

import com.shopsphere.inventory.entity.InventoryItem;
import com.shopsphere.inventory.exception.InventoryNotFoundException;
import com.shopsphere.inventory.exception.OutOfStockException;
import com.shopsphere.inventory.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InventoryService {
    private final InventoryRepository repo;
    public InventoryService(InventoryRepository repo){ this.repo = repo; }

    public InventoryItem getByProductId(Long productId) {
        return repo.findByProductId(productId)
                .orElseThrow(() -> new InventoryNotFoundException(productId));
    }

    @Transactional
    public InventoryItem upsert(Long productId, int availableDelta) {
        InventoryItem item = repo.findByProductId(productId)
                .orElse(InventoryItem.builder().productId(productId).available(0).reserved(0).build());
        item.setAvailable(Math.max(0, item.getAvailable() + availableDelta));
        return repo.save(item);
    }

    @Transactional
    public void reserve(Long productId, int qty) {
        InventoryItem item = getByProductId(productId);
        if (item.getAvailable() < qty) throw new OutOfStockException(productId, qty, item.getAvailable());
        item.setAvailable(item.getAvailable() - qty);
        item.setReserved(item.getReserved() + qty);
        repo.save(item);
    }

    @Transactional
    public void release(Long productId, int qty) {
        InventoryItem item = getByProductId(productId);
        item.setReserved(Math.max(0, item.getReserved() - qty));
        item.setAvailable(item.getAvailable() + qty);
        repo.save(item);
    }

    @Transactional
    public void commit(Long productId, int qty) {
        InventoryItem item = getByProductId(productId);
        if (item.getReserved() < qty) throw new IllegalStateException("Reserved less than commit qty");
        item.setReserved(item.getReserved() - qty);
        // available unchanged — committed stock becomes sold
        repo.save(item);
    }
}
