package com.shopsphere.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "inventory", uniqueConstraints = @UniqueConstraint(columnNames = "product_id"))
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class InventoryItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_id", nullable = false, unique = true)
    private Long productId;

    @Column(nullable = false)
    private Integer available; // on-hand available

    @Column(nullable = false)
    private Integer reserved;  // reserved (pending orders)

    public int total() {
        return available + reserved;
    }
}
