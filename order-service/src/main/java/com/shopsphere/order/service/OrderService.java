package com.shopsphere.order.service;

import com.shopsphere.order.client.InventoryClient;
import com.shopsphere.order.client.ProductClient;
import com.shopsphere.order.dto.ProductDto;
import com.shopsphere.order.dto.CreateOrderRequest;
import com.shopsphere.order.entity.*;
import com.shopsphere.order.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Service
public class OrderService {
    private final OrderRepository repo;
    private final ProductClient productClient;
    private final InventoryClient inventoryClient;

    public OrderService(OrderRepository repo, ProductClient productClient, InventoryClient inventoryClient) {
        this.repo = repo;
        this.productClient = productClient;
        this.inventoryClient = inventoryClient;
    }

    @Transactional
    public Order create(Long userId, CreateOrderRequest req) {
        if (req.getItems() == null || req.getItems().isEmpty())
            throw new IllegalArgumentException("No items provided");

        // 1) Validate products & capture current prices
        List<Long> ids = req.getItems().stream().map(CreateOrderRequest.Item::getProductId).toList();
        List<ProductDto> products = productClient.getProductsByIds(ids);
        Map<Long, ProductDto> byId = new HashMap<>();
        for (ProductDto p : products) byId.put(p.getId(), p);

        // 2) Create order draft
        Order order = Order.builder()
                .userId(userId)
                .createdAt(Instant.now())
                .status(OrderStatus.PENDING)
                .total(BigDecimal.ZERO)
                .build();

        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderRequest.Item it : req.getItems()) {
            ProductDto p = Optional.ofNullable(byId.get(it.getProductId()))
                    .orElseThrow(() -> new IllegalArgumentException("Product missing: " + it.getProductId()));
            BigDecimal line = p.getPrice().multiply(BigDecimal.valueOf(it.getQuantity()));
            total = total.add(line);
            order.getItems().add(OrderItem.builder()
                    .order(order)
                    .productId(p.getId())
                    .quantity(it.getQuantity())
                    .unitPrice(p.getPrice())
                    .lineTotal(line)
                    .build());
        }
        order.setTotal(total);

        // 3) Reserve inventory (synchronous “saga” step 1)
        try {
            for (OrderItem oi : order.getItems()) {
                inventoryClient.reserve(oi.getProductId(), oi.getQuantity());
            }
        } catch (Exception ex) {
            // reservation failed → no DB save
            throw new RuntimeException("Reservation failed: " + ex.getMessage(), ex);
        }

        // 4) Persist order as PENDING (reservation already done)
        Order saved = repo.save(order);

        // 5) Simulate payment (you’d call payment provider). Assume success.
        boolean paymentOk = true;

        if (paymentOk) {
            // 6a) Commit stock and mark order CONFIRMED
            for (OrderItem oi : saved.getItems()) {
                inventoryClient.commit(oi.getProductId(), oi.getQuantity());
            }
            saved.setStatus(OrderStatus.CONFIRMED);
            return repo.save(saved);
        } else {
            // 6b) Payment failed → release reservation and cancel
            for (OrderItem oi : saved.getItems()) {
                inventoryClient.release(oi.getProductId(), oi.getQuantity());
            }
            saved.setStatus(OrderStatus.CANCELLED);
            return repo.save(saved);
        }
    }

    public Order get(Long id) {
        return repo.findById(id).orElseThrow(() -> new NoSuchElementException("Order not found: " + id));
    }
}
