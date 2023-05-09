package com.lintang.netflik.movieQueryService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MovieQueryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieQueryServiceApplication.class, args);
	}

}
