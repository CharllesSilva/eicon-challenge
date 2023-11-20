package com.eicon.demo.dto;

import com.eicon.demo.entities.Product;

import java.math.BigDecimal;
public class ProductDTO{

    private Long id;
    private String name;
    private BigDecimal price;

    public ProductDTO() {
    }

    public ProductDTO(Long idProduct, String name, BigDecimal price) {
        this.id = idProduct;
        this.name = name;
        this.price = price;
    }

    public ProductDTO(Product product) {
        id = product.getId();
        name = product.getName();
        price = product.getPrice();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

}
