package com.kafkastreams.movieservice;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableDiscoveryClient
@SpringBootApplication
public class MovieserviceApplication {


	public static void main(String[] args) {

		SpringApplication.run(MovieserviceApplication.class, args);
	}


}
