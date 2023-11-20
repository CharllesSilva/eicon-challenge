package com.eicon.demo.services;

import com.eicon.demo.entities.Product;
import com.eicon.demo.repositories.ProductRepository;
import com.eicon.demo.services.exceptions.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @InjectMocks
    private ProductService productService;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void testCreateProduct() {
        Product inputProduct = new Product(1L, "Product1", new BigDecimal("50.00"));
        Product expectedProduct = new Product(1L, "Product1", new BigDecimal("50.00"));

        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        Product result = productService.createProduct(inputProduct);

        assertEquals(expectedProduct.getId(), result.getId());
        assertEquals(expectedProduct.getName(), result.getName());
        assertEquals(expectedProduct.getPrice(), result.getPrice());
    }

    @Test
    public void testUpdateProduct() {
        Long productId = 1L;
        Product inputProduct = new Product(null, "UpdatedProduct", new BigDecimal("75.00"));
        Product existingProduct = new Product(productId, "ExistingProduct", new BigDecimal("60.00"));
        Product expectedProduct = new Product(productId, "UpdatedProduct", new BigDecimal("75.00"));

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(expectedProduct);

        Product result = productService.updateProduct(productId, inputProduct);

        assertEquals(expectedProduct.getId(), result.getId());
        assertEquals(expectedProduct.getName(), result.getName());
        assertEquals(expectedProduct.getPrice(), result.getPrice());
    }

    @Test
    public void testUpdateProductWhenProductNotFound() {
        Long productId = 1L;
        Product updatedProduct = new Product();
        updatedProduct.setName("UpdatedProduct");
        updatedProduct.setPrice(new BigDecimal("100.00"));

        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productId, updatedProduct));
    }
}

