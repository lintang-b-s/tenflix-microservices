package com.lintang.netflik.paymentservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}


	@Bean
	public ObjectMapper scmsObjectMapper() {
		com.fasterxml.jackson.databind.ObjectMapper responseMapper = new com.fasterxml.jackson.databind.ObjectMapper();
		return responseMapper;
	}
}
