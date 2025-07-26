package com.shopsphere.cart.service;

import com.shopsphere.cart.entity.CartItem;
import com.shopsphere.cart.repository.CartItemRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartItemRepository cartRepo;

    public List<CartItem> getCartItemsByUser(Long userId) {
        return cartRepo.findByUserId(userId);
    }

    public CartItem addToCart(CartItem item) {
        return cartRepo.findByUserIdAndProductId(item.getUserId(), item.getProductId())
                .map(existing -> {
                    existing.setQuantity(existing.getQuantity() + item.getQuantity());
                    return cartRepo.save(existing);
                })
                .orElse(cartRepo.save(item));
    }

    public CartItem updateQuantity(CartItem item) {
        return cartRepo.findByUserIdAndProductId(item.getUserId(), item.getProductId())
                .map(existing -> {
                    existing.setQuantity(item.getQuantity());
                    return cartRepo.save(existing);
                })
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for update"));
    }

    public void removeFromCart(Long userId, Long productId) {
        CartItem item = cartRepo.findByUserIdAndProductId(userId, productId)
                .orElseThrow(() -> new EntityNotFoundException("Cart item not found for deletion"));
        cartRepo.delete(item);
    }
}
