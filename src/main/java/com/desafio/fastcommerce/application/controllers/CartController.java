package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.CartService;
import com.desafio.fastcommerce.domain.DTOs.cartDTOs.AddItemCartDTO;
import com.desafio.fastcommerce.domain.DTOs.cartDTOs.CartResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public ResponseEntity<CartResponseDTO> getCart() {
        return ResponseEntity.ok(cartService.getCart());
    }
    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItem (@RequestBody AddItemCartDTO dto){
        return ResponseEntity.ok(cartService.addItem(dto));
    }
    @DeleteMapping("/items/{productId}")
    public ResponseEntity<Void> removeItem(@PathVariable UUID productId){
        cartService.removeItem(productId);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/checkout")
    public ResponseEntity<OrderResponseDTO> checkout(){
        return ResponseEntity.ok(cartService.checkout());
    }
}
