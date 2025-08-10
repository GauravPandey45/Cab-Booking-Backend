package com.AuthenticationService.Controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.AuthenticationService.Entity.DriversAuth;
import com.AuthenticationService.Service.DriverService;

@RestController
@RequestMapping("/driver")
public class DriverController {

	@Autowired
	DriverService driverService;
	
	@PostMapping("/signup")
	public DriversAuth addNewDriver(@RequestBody DriversAuth driver) {
		return driverService.saveDriver(driver);
	}
	
	@GetMapping("/profile")
	public Optional<DriversAuth> getExistingDriver(@RequestBody String username) {
		return driverService.getDriver(username);
	}
	
	@PutMapping("/update")
	public DriversAuth updatExistingDriver(@RequestBody DriversAuth driver) {
		return driverService.saveDriver(driver);
	}

}
