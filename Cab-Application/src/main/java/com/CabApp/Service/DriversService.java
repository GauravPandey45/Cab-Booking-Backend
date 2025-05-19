package com.CabApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CabApp.Entities.Drivers;
import com.CabApp.Enums.DriverStatus;
import com.CabApp.Repository.DriversRepository;
import com.CabApp.Security.SecurityConfig;

@Service
public class DriversService {
	
	@Autowired
	DriversRepository driversRepository;
	
	@Autowired
	SecurityConfig  securityConfig;
	
	public Drivers driverDetails(Drivers drivers) {
		drivers.setStatus(DriverStatus.AVAILABLE);
		return driversRepository.save(drivers);
	}

}
