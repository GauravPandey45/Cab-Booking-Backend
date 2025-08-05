package com.CabApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.CabApp.DTO.LoginRequestDTO;
import com.CabApp.Entities.Customers;
import com.CabApp.Enums.CustomerStatus;
import com.CabApp.Repository.CustomerRepository;
import com.CabApp.Security.SecurityConfig;
import com.CabApp.Utils.JwtUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomerService {
	
	@Autowired
	CustomerRepository customersrepository;
	
	@Autowired
	SecurityConfig securityConfig;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtUtil jwtUtil;
	
	public Customers signUp(Customers customers) {
		log.debug("Entering Customer SignUp method");
		customers.setPassword(securityConfig.passwordEncoder().encode(customers.getPassword()));
		customers.setRideStatus(CustomerStatus.FREE);
		log.debug("Exiting Customer SignUp method");
		return customersrepository.save(customers);
	}
	
	public String logIn(LoginRequestDTO loginRequest) throws Exception {
		log.debug("Entering Customer login method");
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),loginRequest.getPassword()));
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		log.debug("Jwt generation started");
		String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
		log.debug("Exiting Customer login method");
		return jwtToken;
	}

}
