package com.GatewayService.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.GatewayService.Utils.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter {
	
	private static final List<String> openEndPoints = List.of("/user","/driver","/auth");
	
	@Autowired
	JwtUtil jwtUtil;
	
	@Override
	public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
		log.info("Entering authenication filter");
		String path = exchange.getRequest().getURI().getPath();
		log.info("Path fetched from request body: {}", path);
		if (openEndPoints.stream().anyMatch(path::startsWith)) {
			log.info("Path is for "+path+" hence bypassing authentication");
			return chain.filter(exchange);
		}
		log.info("Path is other than openEndPoints hence proceeding for authentication");
		String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
		if(authHeader==null ||! authHeader.startsWith("Bearer ")) {
			exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
			return exchange.getResponse().setComplete();
		}
		String token = authHeader.substring(7);
			Jws<Claims> jws =  jwtUtil.validateToken(token);
			if(jws == null) {
				throw new JwtException("Unauthorized access");
		}
			Claims claims =  jws.getPayload();
			Map<String, String> claimsMap = new HashMap<>();
			claimsMap.put("username", claims.get("username").toString());
			claimsMap.put("email", claims.get("email").toString());
			claimsMap.put("phone", claims.get("phone").toString());
			claimsMap.put("role", claims.get("role").toString());
			ServerWebExchange serverWebExchange =  exchange.mutate().request(builder -> builder.headers(httpHeaders -> {
				claimsMap.forEach(httpHeaders::add);
			})).build();
			
		return chain.filter(serverWebExchange);
	}

}
