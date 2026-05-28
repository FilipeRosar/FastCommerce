package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.ProductService;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.CreateProdutsDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.ProductResponseDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.UpdateProductsDto;
import com.desafio.fastcommerce.domain.entities.Products;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductsController {
    private final ProductService _productService;

    @GetMapping
    public ResponseEntity<Page<ProductResponseDto>> getProducts(@RequestParam(required = false) String name,
                                                                @RequestParam(required = false) String category,
                                                                @RequestParam(required = false) BigDecimal minPrice,
                                                                @RequestParam(required = false) BigDecimal maxPrice,
                                                                Pageable pageable)
            {
        return ResponseEntity.ok(_productService.getProducts(name, category, minPrice, maxPrice, pageable));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponseDto> getProductById(@PathVariable UUID productId) {
        var id = _productService.getProdutById(productId);
        return ResponseEntity.ok(id);
    }
    @PostMapping
    public ResponseEntity<UUID> createProduct(@RequestBody CreateProdutsDto dto){
        UUID productId = _productService.createProduct(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(productId);
    }
    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID productId) {
        _productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }
    @PutMapping("/{productId}")
    public ResponseEntity<Void> updateProduct(@PathVariable UUID productId,
                                              @RequestBody UpdateProductsDto dto){
        _productService.updateProduct(productId, dto);
        return ResponseEntity.noContent().build();
    }
}
