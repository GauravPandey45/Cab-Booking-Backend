package com.AuthenticationService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthenticaionServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthenticaionServiceApplication.class, args);
	}

}
