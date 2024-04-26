package com.lintang.netflik.movieQueryService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
// @EnableDiscoveryClient
public class MovieQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieQueryServiceApplication.class, args);
	}

	@Bean
	public ObjectMapper scmsObjectMapper() {
		com.fasterxml.jackson.databind.ObjectMapper responseMapper = new com.fasterxml.jackson.databind.ObjectMapper();
		responseMapper.findAndRegisterModules();
		return responseMapper;
	}

}
