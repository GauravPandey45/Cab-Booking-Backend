package com.AuthenticationService.Client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.AuthenticationService.DTO.DriverClientDTO;
import com.AuthenticationService.Entity.DriversAuth;

@FeignClient(name = "Cab-Service",url = "http://localhost:8001/driverinfo")
public interface DriverFeignClient {
	
	@PostMapping("/register")
	public DriversAuth registerDriver(@RequestBody DriverClientDTO driverClientDTO);

}
