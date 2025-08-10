package com.CabApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class CabApplication {

	public static void main(String[] args) {
		SpringApplication.run(CabApplication.class, args);
	}
}
