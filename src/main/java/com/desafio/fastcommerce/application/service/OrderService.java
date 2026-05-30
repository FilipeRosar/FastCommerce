package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.domain.DTOs.events.OrderEventDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.CreateOrderRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderItemRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderItemResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderResponseDTO;
import com.desafio.fastcommerce.domain.entities.OrderItems;
import com.desafio.fastcommerce.domain.entities.Orders;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.enums.OrderStatus;
import com.desafio.fastcommerce.domain.repository.OrderRepository;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.config.RabbitMqConfig;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import com.desafio.fastcommerce.infrastructure.messaging.OrderEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductsRepository productsRepository;
    private final OrderEventProducer producer;
    private final UserRepository userRepository;

    @Transactional
    public OrderResponseDTO createOrder(CreateOrderRequestDTO dto) {
        Orders order = new Orders();
        order.setStatus(OrderStatus.PENDING);
        BigDecimal total = BigDecimal.ZERO;

        List<UUID> requestedProductIds = dto.items().stream()
                .map(OrderItemRequestDTO::productId)
                .toList();
        List<Products> availableProducts = productsRepository.findAllById(requestedProductIds);

        Map<UUID, Products> productsMap = availableProducts.stream()
                .collect(Collectors.toMap(Products::getId, p -> p));

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()) {
            throw new CustomException("Usuário não autenticado ou sessão inválida.");
        }
        String userEmail = authentication.getName();

        User currentUser = userRepository.getUserByEmail(userEmail)
                .orElseThrow(() -> new CustomException("Usuário não autenticado ou sessão inválida."));
        for (OrderItemRequestDTO itemRequest : dto.items()) {
            Products products = productsMap.get(itemRequest.productId());
            if (products == null) {
                throw new CustomException("Produto não encontrado: ID " + itemRequest.productId());
            }

            if (products.getStockQuantity() < itemRequest.quantity()) {
                throw new CustomException("Estoque insuficiente para o produto: " + products.getName());
            }

            products.setStockQuantity(products.getStockQuantity() - itemRequest.quantity());
            BigDecimal subtotal = products.getPrice().multiply(new BigDecimal(itemRequest.quantity()));

            OrderItems orderItems = new OrderItems();
            orderItems.setOrder(order);
            orderItems.setProduct(products);
            orderItems.setQuantity(itemRequest.quantity());
            orderItems.setUnitPrice(products.getPrice());
            orderItems.setSubTotal(subtotal);


            order.getItems().add(orderItems);
            total = total.add(subtotal);
        }

        order.setUserId(currentUser);
        order.setTotalAmount(total);
        Orders savedOrder = orderRepository.save(order);

        return mapToDTO(savedOrder);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponseDTO confirmOrder(UUID orderId) {
        Orders order = findOrderFetchItems(orderId);

        if (order.getStatus() != OrderStatus.PENDING){
            throw new CustomException("Apenas pedidos pendentes podem ser confirmados");
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Orders savedOrder = orderRepository.save(order);

        publishOrderEvent(RabbitMqConfig.ORDER_CONFIRMED_QUEUE, savedOrder);

        return mapToDTO(savedOrder);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponseDTO shipOrder(UUID orderId) {
        Orders order = findOrderFetchItems(orderId);

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new CustomException("Apenas pedidos CONFIRMED podem ser enviados");
        }

        order.setStatus(OrderStatus.SHIPPED);
        Orders savedOrder = orderRepository.save(order);

        publishOrderEvent(RabbitMqConfig.ORDER_SHIPPED_QUEUE, savedOrder);

        return mapToDTO(savedOrder);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponseDTO deliverOrder(UUID orderId) {
        Orders order = findOrderFetchItems(orderId);

        if (order.getStatus() != OrderStatus.SHIPPED) {
            throw new CustomException("Apenas pedidos SHIPPED podem ser entregues");
        }

        order.setStatus(OrderStatus.DELIVERED);
        Orders updatedOrder = orderRepository.save(order);

        publishOrderEvent(RabbitMqConfig.ORDER_DELIVERED_QUEUE, updatedOrder);

        return mapToDTO(updatedOrder);
    }

    @Transactional
    @CacheEvict(value = "orders", key = "#orderId")
    public OrderResponseDTO cancelOrder(UUID orderId) {
        Orders order = findOrderFetchItems(orderId);

        if (order.getStatus() == OrderStatus.DELIVERED) {
            throw new CustomException("Pedidos entregues não podem ser cancelados");
        }

        for (OrderItems item : order.getItems()) {
            Products product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Orders updatedOrder = orderRepository.save(order);

        publishOrderEvent(RabbitMqConfig.ORDER_CANCELLED_QUEUE, updatedOrder);

        return mapToDTO(updatedOrder);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "orders")
    public OrderResponseDTO getOrderById(UUID orderId) {
        return mapToDTO(findOrderFetchItems(orderId));
    }

    private Orders findOrderFetchItems(UUID orderId) {
        return orderRepository.findByIdWithItems(orderId)
                .orElseThrow(() -> new CustomException("Pedido não encontrado"));
    }

    private OrderResponseDTO mapToDTO(Orders order) {
        List<OrderItemResponseDTO> itemsDto = order.getItems().stream()
                .map(OrderItemResponseDTO::fromEntity)
                .toList();

        return new OrderResponseDTO(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getCreatedAt(),
                itemsDto
        );
    }

    private void publishOrderEvent(String queue, Orders orders){
        OrderEventDTO event = new OrderEventDTO(
                orders.getId(),
                orders.getStatus(),
                orders.getTotalAmount(),
                orders.getCreatedAt()
        );
        producer.publish(queue, event);
    }
}