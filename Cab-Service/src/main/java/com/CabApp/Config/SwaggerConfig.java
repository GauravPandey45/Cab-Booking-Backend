package com.CabApp.Config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;

@Configuration
public class SwaggerConfig {
	
	@Bean
	public OpenAPI swaggerCustomConfig() {
		return new OpenAPI().info(new Info().title("Cab-Applicaion APIs").description("By Gaurav"))
							.servers(List.of(new Server().url("http://localhost:8005").description("local"),
									new Server().url("http://localhost:8007").description("live")))
							.tags(List.of(new Tag().name("Customer APIs"),new Tag().name("Driver APIs"), new Tag().name("Google Auth APIs"),
									new Tag().name("Rides APIs")))
							.addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
							.components(new Components().addSecuritySchemes("bearerAuth", new SecurityScheme()
									.type(SecurityScheme.Type.HTTP)
									.scheme("bearer")
									.bearerFormat("JWT")
									.in(SecurityScheme.In.HEADER)
									.name("Authorizaion")));
		
	}

}
