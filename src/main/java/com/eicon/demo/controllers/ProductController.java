package com.eicon.demo.controllers;


import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import com.eicon.demo.dto.ProductDTO;
import com.eicon.demo.entities.Product;
import com.eicon.demo.services.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;


@RestController
@RequestMapping(value = "/products")
@Tag(name = "Products", description = "Endpoints for Managing products")
public class ProductController {

    private final ModelMapper modelMapper;
    private final ProductService productService;

    public ProductController(ModelMapper modelMapper, ProductService productService) {
        this.modelMapper = modelMapper;
        this.productService = productService;
    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Create a new product",
            description = "Creates a new product with the provided details",
            tags = {"Products"},
            responses = {
                    @ApiResponse(responseCode = "201", description = "Product created successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProductDTO.class))}),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product createdProduct = productService.createProduct(product);

        ProductDTO createdProductDTO = modelMapper.map(createdProduct, ProductDTO.class);

        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdProductDTO.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdProductDTO);
    }

    @PatchMapping(value = "/{productId}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @Operation(summary = "Update a product",
            description = "Partially update a product with the provided details",
            tags = {"Products"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Product updated successfully",
                            content = {
                                    @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = ProductDTO.class)
                                    )
                            }),
                    @ApiResponse(responseCode = "404", description = "Product not found with ID: X", content = @Content),
                    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content),
            }
    )
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @RequestBody ProductDTO productDTO) {
        Product product = modelMapper.map(productDTO, Product.class);
        Product updatedProduct = productService.updateProduct(productId, product);

        ProductDTO updatedProductDTO = modelMapper.map(updatedProduct, ProductDTO.class);

        return ResponseEntity.ok(updatedProductDTO);
    }

}
