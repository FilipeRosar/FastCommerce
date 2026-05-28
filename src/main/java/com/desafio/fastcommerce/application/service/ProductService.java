package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.CreateProdutsDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.ProductResponseDto;
import com.desafio.fastcommerce.domain.DTOs.ProductsDTOs.UpdateProductsDto;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductService {
    private final ProductsRepository _productRepository;

    public ProductService(ProductsRepository _productRepository) {
        this._productRepository = _productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    public UUID createProduct(CreateProdutsDto dto){

        if(dto.preco() == null || dto.preco().compareTo(BigDecimal.ZERO) <= 0){
            throw new CustomException("Produto deve ser maior que zero");
        }
        if (dto.estoque() < 0) {
            throw new CustomException("O estoque do produto deve ser maior que zero");
        }

        Products products = new Products();
        products.setName(dto.nome());
        products.setCategory(dto.categoria());
        products.setCreatedAt(LocalDateTime.now());
        products.setPrice(dto.preco());
        products.setStockQuantity(dto.estoque());
        products.setActive(true);

        return _productRepository.save(products).getId();
    }

    public ProductResponseDto getProdutById(UUID id){
        Products product = _productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Produto não encontrado"));
                return toDto(product);
    }
    @Cacheable(value = "products")
    public Page<ProductResponseDto> getProducts(String name,
                                                String category,
                                                BigDecimal minPrice,
                                                BigDecimal maxPrice,
                                                Pageable pageable) {
        Page<Products> products;

        if (name != null){
            products = _productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name, pageable);
        } else if (category != null){
            products = _productRepository.findByCategoryContainingIgnoreCaseAndIsActiveTrue(category, pageable);
        } else if (minPrice != null && maxPrice != null){
            products = _productRepository.findByPriceBetweenAndIsActiveTrue(minPrice, maxPrice, pageable);
        } else {
            products = _productRepository.findByIsActiveTrue(pageable);
        }

        return products.map(this::toDto);
    }
    @CacheEvict(value = "products", allEntries = true)
    public void deleteProduct(UUID id){
        Products products = _productRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> new CustomException("Produto não encontrado!"));

        products.setActive(false);
        products.setUpdatedAt(LocalDateTime.now());
        _productRepository.save(products);
    }
    @CacheEvict(value = "products", allEntries = true)
    public ProductResponseDto updateProduct(UUID id, UpdateProductsDto dto){
        Products products = _productRepository.findById(id)
                .orElseThrow(() -> new CustomException("Produto não encontrado"));

        if (dto.preco() != null && dto.preco().compareTo(BigDecimal.ZERO) <= 0){
            throw new CustomException("Produto deve ser maior que zero");
        }
        if (dto.estoque() != null && dto.estoque() < 0){
            throw new CustomException("Estoque não pode ser negativo");
        }
        products.setName(dto.name());
        products.setDescription(dto.descricao());
        products.setCategory(dto.categoria());
        products.setPrice(dto.preco());
        products.setStockQuantity(dto.estoque());
        products.setUpdatedAt(LocalDateTime.now());

        Products updateProduct = _productRepository.save(products);
        return toDto(updateProduct);

    }
    private ProductResponseDto toDto(Products products){
        return new ProductResponseDto(
            products.getName(),
                products.getPrice(),
                products.getCategory(),
                products.getCreatedAt()
        );
    }

}
