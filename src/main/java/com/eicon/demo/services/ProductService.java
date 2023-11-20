package com.eicon.demo.services;

import com.eicon.demo.entities.Product;
import com.eicon.demo.repositories.ProductRepository;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long productId, Product product){
        Product existingClient = productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with ID: " + productId));

        if (product.getName() != null) {
            existingClient.setName(product.getName());
        }

        if (product.getPrice() != null) {
            existingClient.setPrice(product.getPrice());
        }

        return productRepository.save(existingClient);
    }
}
