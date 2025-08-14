package com.shopsphere.order.controller;

import com.shopsphere.order.dto.CreateOrderRequest;
import com.shopsphere.order.entity.Order;
import com.shopsphere.order.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController @RequestMapping("/orders")
public class OrderController {
    private final OrderService svc;
    public OrderController(OrderService svc){ this.svc = svc; }

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody CreateOrderRequest req, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        Order order = svc.create(userId, req);
        return ResponseEntity.created(URI.create("/orders/" + order.getId())).body(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> get(@PathVariable Long id) {
        return ResponseEntity.ok(svc.get(id));
    }
}
