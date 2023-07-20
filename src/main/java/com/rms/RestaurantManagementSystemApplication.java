package com.rms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories({"com.rms.dao"})
public class RestaurantManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantManagementSystemApplication.class, args);
	}

}
