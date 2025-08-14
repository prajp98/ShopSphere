package com.shopsphere.inventory.repository;
import com.shopsphere.inventory.entity.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface InventoryRepository extends JpaRepository<InventoryItem, Long> {
    Optional<InventoryItem> findByProductId(Long productId);
    boolean existsByProductId(Long productId);
}
