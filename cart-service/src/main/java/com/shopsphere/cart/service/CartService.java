package com.shopsphere.cart.service;

import com.shopsphere.cart.entity.CartItem;
import com.shopsphere.cart.repository.CartRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public List<CartItem> getCartItemsByUser(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    public CartItem addToCart(CartItem item) {
        try {
            return cartRepository.save(item);
        } catch (DataIntegrityViolationException ex) {
            throw new IllegalArgumentException("Item already exists in cart or violates constraints");
        }
    }

    public CartItem updateQuantity(CartItem item) {
        CartItem existingItem = cartRepository.findByUserIdAndProductId(item.getUserId(), item.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found for update"));

        existingItem.setQuantity(item.getQuantity());

        return cartRepository.save(existingItem); // now updating the correct item by ID
    }

    public void removeFromCart(Long userId, Long productId) {
        cartRepository.deleteByUserIdAndProductId(userId, productId);
    }
}
