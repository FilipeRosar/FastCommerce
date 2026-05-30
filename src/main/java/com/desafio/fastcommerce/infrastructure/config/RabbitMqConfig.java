package com.desafio.fastcommerce.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String ORDER_CONFIRMED_QUEUE = "order.confirmed";
    public static final String ORDER_SHIPPED_QUEUE = "order.shipped";
    public static final String ORDER_DELIVERED_QUEUE = "order.delivered";
    public static final String ORDER_CANCELLED_QUEUE = "order.cancelled";

    @Bean
    public Queue orderConfirmedQueue() {
        return new Queue(ORDER_CONFIRMED_QUEUE);
    }
    @Bean
    public Queue orderShippedQueue() {
        return new Queue(ORDER_SHIPPED_QUEUE);
    }
    @Bean
    public Queue orderDeliveredQueue() {
        return new Queue(ORDER_DELIVERED_QUEUE);
    }
    @Bean
    public Queue orderCancelledQueue() {
        return new Queue(ORDER_CANCELLED_QUEUE);
    }
}
