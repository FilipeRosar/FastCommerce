package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.cartDTOs.AddItemCartDTO;
import com.desafio.fastcommerce.domain.DTOs.cartDTOs.CartItemResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.cartDTOs.CartResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.CreateOrderRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderItemRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderResponseDTO;
import com.desafio.fastcommerce.domain.entities.Cart;
import com.desafio.fastcommerce.domain.entities.CartItem;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.repository.CartRepository;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final ProductsRepository productsRepository;
    private final AuthService authService;
    private final OrderService orderService;

    @Transactional
    @CacheEvict(value = "cart",
                key = "@cacheKey.currentUser()")
    public CartResponseDTO addItem(AddItemCartDTO dto){
        User user = authService.getAuthenticatedUser();

        Cart cart = getOrCreateCart(user);

        Products products =  productsRepository.findById(dto.productId())
                .orElseThrow(() -> new CustomException("Produto não encontrado"));

        Optional<CartItem> existingItem = cart.getItems()
                .stream()
                .filter(i -> i.getProduct().getId()
                        .equals(products.getId())).findFirst();
        if (existingItem.isPresent()) {
            int newQuantity = existingItem.get().getQuantity() + dto.quantity();
            if (newQuantity > products.getStockQuantity()){
                throw new CustomException("Quantidade maior que o estoque disponivel");
            }
            existingItem.get().setQuantity(newQuantity);
        } else{
            CartItem item = new CartItem();
            if(dto.quantity() <= 0){
                throw new CustomException("Quantidade invalida");
            }
            if (products.getStockQuantity() < dto.quantity()) {
                throw new CustomException("Estoque insuficiente");
            }
            item.setCart(cart);
            item.setProduct(products);
            item.setQuantity(dto.quantity());
            cart.getItems().add(item);
        }
        cartRepository.save(cart);
        return toDto(cart);
    }

    @Transactional
    @CacheEvict(
            value = "cart",
            key = "@cacheKey.currentUser()"
    )
    public void removeItem(UUID productId){
        User user = authService.getAuthenticatedUser();
        Cart cart = getOrCreateCart(user);

        cart.getItems().removeIf(
                item -> item.getProduct()
                        .getId().equals(productId)
        );
        cartRepository.save(cart);
    }

    @Transactional
    @CacheEvict(
            value = "cart",
            key = "@cacheKey.currentUser()"
    )
    public OrderResponseDTO checkout(){
        User user = authService.getAuthenticatedUser();
        Cart cart = cartRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Carrinho não encontrado"));
        if(cart.getItems().isEmpty()){
            throw new CustomException("Carrinho vazio");
        }
        List<OrderItemRequestDTO> items = cart.getItems()
                .stream()
                .map(item -> new OrderItemRequestDTO(
                        item.getProduct().getId(),
                        item.getQuantity()
                )).toList();

        CreateOrderRequestDTO request = new CreateOrderRequestDTO(items);
        OrderResponseDTO order = orderService.createOrder(request);

        cart.getItems().clear();
        cartRepository.save(cart);

        return order;
    }
    @Cacheable(value = "cart",
                key = "@cacheKey.currentUser()")
    public CartResponseDTO getCart(){
        User user = authService.getAuthenticatedUser();

        Cart cart = getOrCreateCart(user);

        return toDto(cart);
    }

    private Cart getOrCreateCart(User user) {
        return cartRepository.findByUser(user)
                .orElseGet(() ->{
                   Cart cart = new Cart();

                   cart.setUser(user);
                   cart.setCreatedAt(LocalDateTime.now());
                   return cartRepository.save(cart);
                });
    }
    private CartResponseDTO toDto(Cart cart){
        List<CartItemResponseDTO> items = cart.getItems().stream()
                .map(item -> new CartItemResponseDTO(
                        item.getId(),
                        item.getProduct().getId(),
                        item.getProduct().getName(),
                        item.getQuantity(),
                        item.getProduct().getPrice(),
                        item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                )).toList();
        BigDecimal total = items.stream()
                .map(CartItemResponseDTO::subTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new CartResponseDTO(
            cart.getId(),
                cart.getUser().getId(),
                total,
                items
        );
    }
}
