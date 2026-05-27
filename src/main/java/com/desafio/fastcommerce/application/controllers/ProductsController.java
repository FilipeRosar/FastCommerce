package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.ProductService;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.CreateProdutsDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.ProductResponseDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.UpdateProductsDto;
import com.desafio.fastcommerce.domain.entities.Products;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductService _productService;

    public ProductsController(ProductService _productService) {
        this._productService = _productService;
    }
    @GetMapping
    public Page<ProductResponseDto> getProducts(Pageable pageable) {
        return _productService.getProducts(pageable);
    }
    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable("productId") UUID productId) {
        var id = _productService.getProdutById(productId);
        return ResponseEntity.ok(id);
    }
    @PostMapping
    public ResponseEntity<UUID> createProduct(@RequestBody CreateProdutsDto dto){
        UUID productId = _productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") UUID productId) {
        _productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable("productId") UUID productId,
                                             @RequestBody UpdateProductsDto dto){
        _productService.updateProduct(productId, dto);
        return ResponseEntity.noContent().build();
    }
}
