package com.shopsphere.cart.repository;

import com.shopsphere.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(Long userId);
    void deleteByUserIdAndProductId(Long userId, Long productId);
    Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);

}

