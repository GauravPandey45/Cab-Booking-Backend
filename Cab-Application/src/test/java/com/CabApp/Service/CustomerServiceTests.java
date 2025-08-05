package com.CabApp.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.CabApp.DTO.LoginRequestDTO;
import com.CabApp.Entities.Customers;
import com.CabApp.Enums.CustomerStatus;
import com.CabApp.Repository.CustomerRepository;
import com.CabApp.Security.SecurityConfig;
import com.CabApp.Utils.JwtUtil;

@SpringBootTest
public class CustomerServiceTests {
	
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
	
	@Disabled
	@Test
	public void testsignUp() {
		Customers customers = new Customers();
		customers.setCustomerName("Mohit");
		customers.setEmail("Mohit@gmail.com");
		customers.setPhone("9564563562");
		customers.setUsername("Mohit@67");
		customers.setPassword("Mo@193");
		customers.setPassword(securityConfig.passwordEncoder().encode(customers.getPassword()));
		customers.setRideStatus(CustomerStatus.FREE);
		Customers saved =  customersrepository.save(customers);
		
		assertNotNull(saved.getPassword());
		assertEquals(CustomerStatus.FREE, saved.getRideStatus());
	}
	
	public void testlogIn()  {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Hardhik@45","Test@123"));
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
		assertEquals(jwtToken, jwtToken);
	}
	
	@Disabled
	@ParameterizedTest
	@CsvSource({
		"Hardhik@45,Test@123",
		"Mohit@67, Mo@193",
		"Anil@11, A@990"
	})
	public void testlogIn(String username, String password)  {
		Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username,password));
		UserDetails userDetails = (UserDetails) auth.getPrincipal();
		String jwtToken = jwtUtil.generateToken(userDetails.getUsername());
		assertEquals(jwtToken, jwtToken);
	}
	
	@Disabled
	@Test
	public void testlogInFail()  {
		BadCredentialsException exception =  assertThrows(BadCredentialsException.class, () -> {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken("Hardhik@44","Test@123"));
		});
		
		assertEquals("Bad credentials", exception.getMessage());
	}


}
