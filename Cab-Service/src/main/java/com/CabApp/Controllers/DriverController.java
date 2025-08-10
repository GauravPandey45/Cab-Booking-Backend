package com.CabApp.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CabApp.DTO.DriverClientDTO;
import com.CabApp.Entities.Drivers;
import com.CabApp.Service.DriversService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/driverinfo")
@Tag(name="Driver APIs")
public class DriverController {
	
	@Autowired
	DriversService driversService;
	
	@PostMapping("/register")
	public ResponseEntity<Drivers> addDrivers(@RequestBody DriverClientDTO driverClientDTO) {
		try {
			log.info("Received driver registration request: {}",driverClientDTO);
			Drivers driverRegisterReq = driversService.driverDetails(driverClientDTO);
		if(driverRegisterReq == null) {
			log.error("Driver registration failed as driverRegisterReq is null: {}",driverRegisterReq);
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("Driver registration completed for: {}", driverRegisterReq);
		return new ResponseEntity<>(driverRegisterReq,HttpStatus.OK);
		} catch(Exception e) {
			log.error("Exception occured during driver registration", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	

}
