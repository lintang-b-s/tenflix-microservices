package com.lintang.netflik.movieservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.SingletonManager;
import com.cloudinary.utils.ObjectUtils;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
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
