package com.desafio.fastcommerce.infrastructure.exception;

public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }
}
