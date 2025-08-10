package com.CabApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CabApp.DTO.DriverClientDTO;
import com.CabApp.Entities.Drivers;
import com.CabApp.Enums.DriverStatus;
import com.CabApp.Repository.DriversRepository;

@Service
public class DriversService {
	
	@Autowired
	DriversRepository driversRepository;

	
	public Drivers driverDetails(DriverClientDTO driverClientDTO) {
		Drivers driver = new Drivers();
		driver.setDrivername(driverClientDTO.getDrivername());
		driver.setCar_details(driverClientDTO.getCar_details());
		driver.setLocation(driverClientDTO.getLocation());
		driver.setUsername(driverClientDTO.getUsername());
		driver.setRole(driverClientDTO.getRole());
		driver.setStatus(DriverStatus.AVAILABLE);
		driversRepository.save(driver);
		return driver;
	}

}
