package com.AuthenticationService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.AuthenticationService.DTO.AuthRequestDTO;
import com.AuthenticationService.Repository.UserRepository;
import com.AuthenticationService.Security.JwtUtil;
import com.AuthenticationService.Security.SecurityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthService {
	
	@Autowired
	UserRepository customersrepository;
	
	@Autowired
	SecurityConfig securityConfig;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	
	public String authenicateUser(AuthRequestDTO authRequestDTO) {
		log.info("Entering authenication method");
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(),authRequestDTO.getPassword()));
		if (authentication.isAuthenticated()==false) {
			log.error("Authenication failed due to invalid username or password");
			throw new BadCredentialsException("Authenication failed due to invalid username or password");
		}
		log.info("authentication completed successfully");
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
		log.info("Token generated post authentication");
		log.info("Exiting authentication method");
		return jwtToken;
	}

}
