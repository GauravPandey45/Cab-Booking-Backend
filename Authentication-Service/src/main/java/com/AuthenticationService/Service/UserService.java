package com.AuthenticationService.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AuthenticationService.Entity.UsersAuth;
import com.AuthenticationService.Repository.UserRepository;
import com.AuthenticationService.Security.SecurityConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired
	UserRepository customersrepository;
	
	@Autowired
	SecurityConfig securityConfig;
	
	public UsersAuth saveUser(UsersAuth users) {
		log.info("Entering Customer SignUp method");
		users.setPassword(securityConfig.passwordEncoder().encode(users.getPassword()));
		log.info("Exiting Customer SignUp method");
		return customersrepository.save(users);
	}
	
	public Optional<UsersAuth> getUser(String username) {
		log.info("Retrieving Customer profile");
		return customersrepository.findByUsername(username);
	}
	
	public UsersAuth updateUser(UsersAuth users) {
		log.info("Entering Customer update method");
		users.setPassword(securityConfig.passwordEncoder().encode(users.getPassword()));
		log.info("Exiting Customer update method");
		return customersrepository.save(users);
	}

}
