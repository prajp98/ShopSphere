package com.shopsphere.inventory.repository;

import com.shopsphere.inventory.model.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    boolean existsBySku(String sku);
    Optional<InventoryItem> findBySku(String sku);
}
