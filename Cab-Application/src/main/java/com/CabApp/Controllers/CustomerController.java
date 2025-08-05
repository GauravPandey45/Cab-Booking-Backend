package com.CabApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CabApp.DTO.LoginRequestDTO;
import com.CabApp.Entities.Customers;
import com.CabApp.Service.CustomerService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/customers")
@Tag(name = "Customer APIs")
public class CustomerController {
	
	@Autowired
	CustomerService customerService;
	
	@PostMapping("/signup")
	public  ResponseEntity<Customers> signupCustomers(@RequestBody Customers customers) {
		try {
			log.info("Received customer signup request: {}", customers);
			Customers custSignUpReq =  customerService.signUp(customers);
		if(custSignUpReq == null) {
			log.warn("Customer signup failed as cust is null: {}",custSignUpReq);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("Customer signup completed for: {}", custSignUpReq);
		return new ResponseEntity<>(custSignUpReq,HttpStatus.OK);
		} catch(Exception e) {
			log.error("Exception occured while signing up customer", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping("/login")
	public ResponseEntity<String> loginCustomers(@RequestBody LoginRequestDTO loginRequest) throws Exception {
		try {
			log.info("Received customer login request: {}", loginRequest);
			String custLoginReq =  customerService.logIn(loginRequest);
		if(custLoginReq == null) {
			log.warn("Customer login failed as custLoginReq is null: {}",custLoginReq);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("Customer login was successful for: {}", loginRequest.getUsername());
		return new ResponseEntity<>(custLoginReq,HttpStatus.OK);
		} catch(Exception e) {
			log.error("Exception occured during cusomer login", e);
			return new ResponseEntity<>("An error occured during cusomer login",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
