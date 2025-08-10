package com.AuthenticationService.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.AuthenticationService.Client.DriverFeignClient;
import com.AuthenticationService.DTO.DriverClientDTO;
import com.AuthenticationService.Entity.DriversAuth;
import com.AuthenticationService.Repository.DriverRepository;
import com.AuthenticationService.Security.SecurityConfig;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DriverService {
	
	@Autowired
	SecurityConfig securityConfig;
	
	@Autowired
	DriverRepository driverRepository;
	
	@Autowired
	DriverFeignClient driverFeignClient;
	
	@Transactional
	public DriversAuth saveDriver(DriversAuth drivers) {
		log.info("Entering Driver SignUp method");
		drivers.setPassword(securityConfig.passwordEncoder().encode(drivers.getPassword()));
		DriversAuth driversAuth = driverRepository.save(drivers);
		DriverClientDTO driverClientDTO = new DriverClientDTO();
		driverClientDTO.setDrivername(drivers.getDrivername());
		driverClientDTO.setUsername(drivers.getUsername());
		driverClientDTO.setCar_details(drivers.getCar_details());
		driverClientDTO.setLocation(drivers.getLocation());
		driverClientDTO.setRole(drivers.getRole());
	    driverFeignClient.registerDriver(driverClientDTO);
		log.info("Exiting Driver SignUp method");
		return driversAuth;

	}
	
	public Optional<DriversAuth> getDriver(String username) {
		log.info("Retrieving Driver profile");
		return driverRepository.findByUsername(username);
	}
	
	public DriversAuth updateDriver(DriversAuth drivers) {
		log.info("Entering Driver update method");
		drivers.setPassword(securityConfig.passwordEncoder().encode(drivers.getPassword()));
		log.info("Exiting Driver update method");
		return driverRepository.save(drivers);
	}

}
