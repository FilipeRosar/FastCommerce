package com.desafio.fastcommerce.application.controllers;


import com.desafio.fastcommerce.application.service.OrderService;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.CreateOrderRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderResponseDTO;
import com.desafio.fastcommerce.domain.entities.Orders;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        OrderResponseDTO responseDTO = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrder(@PathVariable UUID id){
        OrderResponseDTO response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/confirm")
    public ResponseEntity<OrderResponseDTO> confirmOrder(@PathVariable UUID id){
        OrderResponseDTO response = orderService.confirmOrder(id);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponseDTO> shipOrder(@PathVariable UUID id){
        OrderResponseDTO response = orderService.shipOrder(id);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/deliver")
    public ResponseEntity<OrderResponseDTO> deliverOrder(@PathVariable UUID id){
        OrderResponseDTO response = orderService.deliverOrder(id);
        return ResponseEntity.ok(response);
    }
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable UUID id){
        OrderResponseDTO response = orderService.cancelOrder(id);
        return ResponseEntity.ok(response);
    }
}
