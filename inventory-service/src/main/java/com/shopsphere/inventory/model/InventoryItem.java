package com.shopsphere.inventory.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "inventory_item",
        uniqueConstraints = @UniqueConstraint(columnNames = {"sku"}))
public class InventoryItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String sku;          // unique product code

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private Integer quantity;    // available stock

    private String location;     // warehouse location, optional
}
