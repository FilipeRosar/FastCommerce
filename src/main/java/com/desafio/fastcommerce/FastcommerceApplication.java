package com.desafio.fastcommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.desafio.fastcommerce.domain.repository")
public class FastcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(FastcommerceApplication.class, args);
	}

}
