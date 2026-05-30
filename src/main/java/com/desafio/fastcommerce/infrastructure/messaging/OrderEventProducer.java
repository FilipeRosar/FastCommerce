package com.desafio.fastcommerce.infrastructure.messaging;


import com.desafio.fastcommerce.domain.DTOs.events.OrderEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderEventProducer {
    private final RabbitTemplate rabbitTemplate;

    public void publish(String queue, OrderEventDTO event){
        rabbitTemplate.convertAndSend(queue, event);

        System.out.println("Evento enviado para fila: " + queue + ": " + event);
    }
}
