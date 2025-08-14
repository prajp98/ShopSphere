package com.shopsphere.product.service;

import com.shopsphere.product.entity.Product;
import com.shopsphere.product.exception.DuplicateProductNameException;
import com.shopsphere.product.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {
    private final ProductRepository repo;
    public ProductService(ProductRepository repo){ this.repo = repo; }

    public List<Product> getAll() { return repo.findAll(); }

    public Product getById(Long id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Product not found: " + id));
    }

    public List<Product> getByIds(List<Long> ids) {
        List<Product> list = repo.findAllById(ids);
        if (list.size() != ids.size()) throw new EntityNotFoundException("Some products not found");
        return list;
    }

    @Transactional
    public Product create(Product p) {
        if (repo.existsByNameIgnoreCase(p.getName()))
            throw new DuplicateProductNameException(p.getName());
        return repo.save(p);
    }

    @Transactional
    public Product update(Long id, Product p) {
        Product existing = getById(id);
        if (!existing.getName().equalsIgnoreCase(p.getName()) && repo.existsByNameIgnoreCase(p.getName()))
            throw new DuplicateProductNameException(p.getName());
        existing.setName(p.getName());
        existing.setDescription(p.getDescription());
        existing.setPrice(p.getPrice());
        existing.setCategory(p.getCategory());
        existing.setImageUrl(p.getImageUrl());
        existing.setStock(p.getStock()); // optional: product’s “default” stock field (not authoritative)
        return repo.save(existing);
    }

    public void delete(Long id) {
        Product p = getById(id);
        repo.delete(p);
    }
}
