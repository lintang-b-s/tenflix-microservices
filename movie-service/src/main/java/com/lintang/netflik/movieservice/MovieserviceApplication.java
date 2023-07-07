package com.lintang.netflik.movieservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MovieserviceApplication {


	public static void main(String[] args) {
		Cloudinary cloudinary =new Cloudinary(System.getenv("CLOUDINARY_URL"));
		SingletonManager manager = new SingletonManager();
		manager.setCloudinary(cloudinary);
		manager.init();
		SpringApplication.run(MovieserviceApplication.class, args);
	}


}
