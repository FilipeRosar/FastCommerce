package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.CreateOrderRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderItemRequestDTO;
import com.desafio.fastcommerce.domain.entities.OrderItems;
import com.desafio.fastcommerce.domain.entities.Orders;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.OrderStatus;
import com.desafio.fastcommerce.domain.repository.OrderRepository;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductsRepository productsRepository;
    private final UserRepository userRepository;

    @Transactional
    public Orders createOrder(CreateOrderRequestDTO dto){
        Orders order = new Orders();

        order.setStatus(OrderStatus.PENDING);
        BigDecimal total = BigDecimal.ZERO;

        List<UUID> requestedProductIds = dto.items().stream()
                .map(OrderItemRequestDTO::productId)
                .toList();
        List<Products> availableProducts = productsRepository.findAllById(requestedProductIds);

        Map<UUID, Products> productsMap = availableProducts.stream()
                .collect(Collectors.toMap(Products::getId, p -> p));

        for (OrderItemRequestDTO itemRequest : dto.items()){
            Products products = productsMap.get(itemRequest.productId());
            if (products == null) {
                throw new CustomException("Produto não encontrado: ID " + itemRequest.productId());
            }

            if(products.getStockQuantity() < itemRequest.quantity()){
                throw new CustomException("Estoque insuficiente para o produto: " + products.getName());
            }
            products.setStockQuantity(products.getStockQuantity() - itemRequest.quantity());
            BigDecimal subtotal = products.getPrice().multiply(new BigDecimal(itemRequest.quantity()));

            OrderItems orderItems = new  OrderItems();

            orderItems.setOrder(order);
            orderItems.setProduct(products);
            orderItems.setQuantity(itemRequest.quantity());
            orderItems.setUnitPrice(products.getPrice());
            orderItems.setSubTotal(subtotal);

            User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            order.setUserId(currentUser);
            order.getItems().add(orderItems);
            total = total.add(subtotal);
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }
}
