package com.desafio.fastcommerce.application.service;

import com.desafio.fastcommerce.application.service.OrderService;
import com.desafio.fastcommerce.application.service.ProductService;
import com.desafio.fastcommerce.application.service.UserService;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.CreateOrderRequestDTO;
import com.desafio.fastcommerce.domain.DTOs.ordersDTOs.OrderItemRequestDTO;
import com.desafio.fastcommerce.domain.entities.Products;
import com.desafio.fastcommerce.domain.entities.User;
import com.desafio.fastcommerce.domain.repository.ProductsRepository;
import com.desafio.fastcommerce.domain.repository.UserRepository;
import com.desafio.fastcommerce.infrastructure.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private ProductsRepository productsRepository;
    @Mock
    private UserRepository  userRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    void deveLacarExcecaoQuandoEstoqueForInsuficiente() {
        Authentication authentication = Mockito.mock(org.springframework.security.core.Authentication.class);
        Mockito.when(authentication.isAuthenticated()).thenReturn(true);
        Mockito.when(authentication.getName()).thenReturn("fulano@gmail.com");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        User usuarioFalso = new User();
        usuarioFalso.setEmail("fulano@gmail.com");
        Mockito.when(userRepository.getUserByEmail("fulano@gmail.com"))
                .thenReturn(Optional.of(usuarioFalso));

        UUID productId = UUID.randomUUID();
        Products produtoMock = new Products();
        produtoMock.setId(productId);
        produtoMock.setName("Produto");
        produtoMock.setStockQuantity(2);

        CreateOrderRequestDTO pedidoDto = new CreateOrderRequestDTO(
                List.of(new OrderItemRequestDTO(productId, 10))
        );
        Mockito.when(productsRepository.findAllById(Mockito.anyList())).thenReturn(List.of(produtoMock));

        CustomException exception = assertThrows(CustomException.class, () -> {
           orderService.createOrder(pedidoDto);
        });
        assertEquals("Estoque insuficiente para o produto: "+ produtoMock.getName(), exception.getMessage());
    }
}
