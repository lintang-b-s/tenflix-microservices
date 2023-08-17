package com.lintang.netflik.orderaggregatorservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration 
public class OrderAggregatorServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderAggregatorServiceApplication.class, args);
	}

	

}
