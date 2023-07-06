package com.lintang.netflik.movieservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieserviceApplication {


	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		Cloudinary cloudinary =new Cloudinary(dotenv.get("CLOUDINARY_URL"));
		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();
		SpringApplication.run(MovieserviceApplication.class, args);
	}


}
