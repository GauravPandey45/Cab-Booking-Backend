package com.AuthenticationService.Security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.AuthenticationService.Entity.DriversAuth;
import com.AuthenticationService.Entity.UsersAuth;
import com.AuthenticationService.Repository.DriverRepository;
import com.AuthenticationService.Repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret.key}")
	private String secretKey;
	
	@Value("${expiration.min}")
	private long expirationMin;
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DriverRepository driverRepository;
	
	
	public SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(secretKey.getBytes());
	}
	
	public String generateToken(String username) {
		Map<String, Object> claims = new HashMap<>();
		Optional<UsersAuth> user =  userRepository.findByUsername(username);
		if (user.isPresent()) {
			claims.put("username",username);
			claims.put("email", user.get().getEmail());
			claims.put("phone", user.get().getPhone());
			claims.put("role", user.get().getRole());
		}else {
				throw new UsernameNotFoundException("User not found: "+username);
		}
		return Jwts.builder()
					.claims(claims)
					.subject(username)
					.header().empty().add("typ", "JWT")
					.and()
					.issuedAt(new Date(System.currentTimeMillis()))
					.expiration(new Date(System.currentTimeMillis()+expirationMin))
					.signWith(getSecretKey())
					.compact();
		
	}
	
	public boolean validateToken(String token, UserDetails userDetails) {
		final String username = extractUsername(token);
		return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
	}

	public Claims extractAllClaims(String token) {
		return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
	}
	
	public <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
		final Claims claims = extractAllClaims(token);
		return claimResolver.apply(claims);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token, Claims::getSubject);
	}
	
	public Date extractExpiration (String token) {
		return extractClaim(token, Claims::getExpiration);
	}
	
	public boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	
}
