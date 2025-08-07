package com.shopsphere.cart.controller;

import com.shopsphere.cart.entity.CartItem;
import com.shopsphere.cart.service.CartService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public ResponseEntity<List<CartItem>> getCart(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<CartItem> items = cartService.getCartItemsByUser(userId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItem> addToCart(@RequestBody CartItem item, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        item.setUserId(userId);
        CartItem added = cartService.addToCart(item);
        return ResponseEntity.status(201).body(added);
    }

    @PutMapping("/update")
    public ResponseEntity<CartItem> updateQuantity(@RequestBody CartItem item, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        item.setUserId(userId);
        CartItem updated = cartService.updateQuantity(item);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> removeFromCart(@PathVariable Long productId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        cartService.removeFromCart(userId, productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/test")
    public ResponseEntity<String> testJwt(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String username = (String) request.getAttribute("username");
        return ResponseEntity.ok("User ID: " + userId + ", Username: " + username);
    }

    @GetMapping("/ping")
    public ResponseEntity<String> ping() {
        return ResponseEntity.ok("pong");
    }
}
