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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO) {
        Orders order = orderService.createOrder(createOrderRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderResponseDTO.fromEntity(order));
    }
}
