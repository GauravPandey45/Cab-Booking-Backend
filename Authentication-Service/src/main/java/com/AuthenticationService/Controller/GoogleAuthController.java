package com.AuthenticationService.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.AuthenticationService.Service.GoogleAuthService;


@RestController
@RequestMapping("/googleauth")
public class GoogleAuthController {

	@Autowired
	private GoogleAuthService googleAuthService;
	
	@PostMapping("/callback")
	public ResponseEntity<String> handleGoogleCallback(@RequestParam("code") String code) throws Exception {
		String token = googleAuthService.handleGoogleAuth(code);
		if(token == null || token.isEmpty()) {
			return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<String>(token, HttpStatus.OK);
	}
}
