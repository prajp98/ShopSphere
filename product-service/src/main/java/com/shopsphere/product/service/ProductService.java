package com.shopsphere.product.service;

import com.shopsphere.product.entity.Product;
import com.shopsphere.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    public List<Product> getAll() {
        return repo.findAll();
    }

    public Product getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    public Product create(Product product) {
        return repo.save(product);
    }

    public Product update(Long id, Product product) {
        Product existing = getById(id);
        existing.setName(product.getName());
        existing.setDescription(product.getDescription());
        existing.setPrice(product.getPrice());
        existing.setCategory(product.getCategory());
        existing.setImageUrl(product.getImageUrl());
        existing.setStock(product.getStock());
        return repo.save(existing);
    }

    public void delete(Long id) {
        Product product = getById(id);
        repo.delete(product);
    }
}
