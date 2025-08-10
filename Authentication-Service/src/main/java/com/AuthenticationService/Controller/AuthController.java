package com.AuthenticationService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AuthenticationService.DTO.AuthRequestDTO;
import com.AuthenticationService.Service.AuthService;

@RestController
@RequestMapping("/authservice")
public class AuthController {
	
	@Autowired
	AuthService authService;
	
	
	@PostMapping("/login")
	public String validateUser(@RequestBody AuthRequestDTO AuthRequestDTO) throws Exception {
		return authService.authenicateUser(AuthRequestDTO);
	}
	
}