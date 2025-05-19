package com.CabApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.CabApp.Entities.Customers;
import com.CabApp.Repository.CustomerRepository;

@Service
public class CustomerDetailService implements UserDetailsService {
	
	@Autowired
	CustomerRepository customerRepository;
	

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customers customers = customerRepository.findByUsername(username);
		if(customers != null) {
			UserDetails userDetails = User.builder()
					.username(customers.getUsername())
					.password(customers.getPassword())
					.build();
			return userDetails;
		}
		throw new UsernameNotFoundException("Invalid Username: "+username);
	}
}
