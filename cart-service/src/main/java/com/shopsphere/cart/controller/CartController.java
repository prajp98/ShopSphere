package com.shopsphere.cart.controller;

import com.shopsphere.cart.entity.CartItem;
import com.shopsphere.cart.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartItemRepository cartRepo;

    @GetMapping("/{userId}")
    public List<CartItem> getCart(@PathVariable Long userId) {
        return cartRepo.findByUserId(userId);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartItem item) {
        return cartRepo.findByUserIdAndProductId(item.getUserId(), item.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return ResponseEntity.ok(cartRepo.save(existing));
                })
                .orElse(ResponseEntity.ok(cartRepo.save(item)));
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartItem item) {
        return cartRepo.findByUserIdAndProductId(item.getUserId(), item.getProductId())
                .map(existing -> {
                    existing.setQuantity(item.getQuantity());
                    return ResponseEntity.ok(cartRepo.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{userId}/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Long userId, @PathVariable Long productId) {
        return cartRepo.findByUserIdAndProductId(userId, productId)
                .map(item -> {
                    cartRepo.delete(item);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
