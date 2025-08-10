package com.AuthenticationService.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AuthenticationService.Entity.UsersAuth;
import com.AuthenticationService.Service.UserService;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping("/signup")
	public UsersAuth addNewUser(@RequestBody UsersAuth user) {
		return userService.saveUser(user);
	}
	
	@GetMapping("/profile")
	public Optional<UsersAuth> getExistingUser(@RequestBody String username) {
		return userService.getUser(username);
	}
	
	@PutMapping("/update")
	public UsersAuth updatExistingUser(@RequestBody UsersAuth user) {
		return userService.saveUser(user);
	}

}
