package com.lintang.netflik.orderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderServiceApplication.class, args);
	}

	@Bean
	@Primary
	public ObjectMapper scmsObjectMapper() {
		com.fasterxml.jackson.databind.ObjectMapper responseMapper = new com.fasterxml.jackson.databind.ObjectMapper();
		return responseMapper;
	}
}
