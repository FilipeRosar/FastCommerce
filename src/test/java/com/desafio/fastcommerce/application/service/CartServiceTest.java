package com.desafio.fastcommerce.application.service;


import com.desafio.fastcommerce.domain.DTOs.cartDTOs.AddItemCartDTO;
import com.desafio.fastcommerce.domain.DTOs.cartDTOs.CartResponseDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderResponseDTO;
import com.desafio.fastcommerce.domain.entities.Cart;
import com.desafio.fastcommerce.domain.entities.CartItem;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.repository.CartRepository;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductService productService;
    @Mock
    private AuthService authService;
    @Mock
    private OrderService orderService;
    @InjectMocks
    private CartService cartService;
    @Mock
    private ProductsRepository productsRepository;

    @Test
    void shouldAddItemToCart(){
        User user = new User();
        user.setId(UUID.randomUUID());

        Products product = new Products();
        product.setId(UUID.randomUUID());
        product.setName("RTX 5060");
        product.setPrice(BigDecimal.valueOf(3000));

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);

        AddItemCartDTO dto =
                new AddItemCartDTO(
                        product.getId(),
                        2
                );

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        when(productsRepository.findById(product.getId()))
                .thenReturn(Optional.of(product));

        when(cartRepository.save(any(Cart.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CartResponseDTO response =
                cartService.addItem(dto);

        assertNotNull(response);

        assertEquals(
                1,
                cart.getItems().size()
        );

        verify(cartRepository)
                .save(cart);
    }
    @Test
    void shouldThrowWhenProductNotFound() {

        User user = new User();

        AddItemCartDTO dto =
                new AddItemCartDTO(
                        UUID.randomUUID(),
                        1
                );

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(new Cart()));

        when(productsRepository.findById(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                CustomException.class,
                () -> cartService.addItem(dto)
        );
    }
    @Test
    void shouldRemoveItemFromCart() {

        UUID productId = UUID.randomUUID();

        User user = new User();

        Products product = new Products();
        product.setId(productId);

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.getItems().add(item);

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        cartService.removeItem(productId);

        assertTrue(cart.getItems().isEmpty());

        verify(cartRepository)
                .save(cart);
    }
    @Test
    void shouldReturnCart() {

        User user = new User();

        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUser(user);

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        CartResponseDTO response =
                cartService.getCart();

        assertNotNull(response);

        assertEquals(
                cart.getId(),
                response.cartId()
        );
    }
    @Test
    void shouldCheckoutSuccessfully() {

        User user = new User();

        Products product = new Products();
        product.setId(UUID.randomUUID());

        CartItem item = new CartItem();
        item.setProduct(product);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUser(user);
        cart.getItems().add(item);

        OrderResponseDTO orderResponse =
                mock(OrderResponseDTO.class);

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        when(orderService.createOrder(any()))
                .thenReturn(orderResponse);

        OrderResponseDTO response =
                cartService.checkout();

        assertNotNull(response);

        assertTrue(cart.getItems().isEmpty());

        verify(cartRepository)
                .save(cart);
    }
    @Test
    void shouldThrowWhenCartIsEmpty() {

        User user = new User();

        Cart cart = new Cart();
        cart.setUser(user);

        when(authService.getAuthenticatedUser())
                .thenReturn(user);

        when(cartRepository.findByUser(user))
                .thenReturn(Optional.of(cart));

        assertThrows(
                CustomException.class,
                () -> cartService.checkout()
        );
    }

}
