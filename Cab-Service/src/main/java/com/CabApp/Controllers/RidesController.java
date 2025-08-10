package com.CabApp.Controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CabApp.Entities.Rides;
import com.CabApp.Service.RidesService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rideinfo")
@Tag(name="Rides APIs")
public class RidesController {

	@Autowired
	RidesService ridesService;
	
	@PostMapping("/book")
	public ResponseEntity<Rides> rideBooked(@RequestBody Rides rides, @RequestHeader Map<String, String> headers) throws Exception {
		try {
		 log.info("Received Ride booking request: {}",rides);
		 Rides bookedRide = ridesService.bookRide(rides, headers);
		if(bookedRide == null) {
			log.warn("Ride booking failed: service returned null");
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		log.info("Ride booked successfully: {}", bookedRide);
		return new ResponseEntity<>(bookedRide,HttpStatus.OK);
		}catch(Exception e) {
			log.error("Exception occured while booking ride", e);
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	@PutMapping("/complete")
	public ResponseEntity<String> rideCompleted(@RequestHeader Map<String, String> headers) throws Exception {
		try {
			log.info("Received Ride completion request");
			String completeRide = ridesService.completeRide(headers);
			if(completeRide == null || completeRide.isEmpty()) {
				log.warn("Ride completion failed: service returned null or empty");
				return new ResponseEntity<>("Ride could not be completed. please try again.",HttpStatus.BAD_REQUEST);
			}
			log.info("Ride completed successfully");
		return new ResponseEntity<>(completeRide,HttpStatus.OK);
		} catch(Exception e) {
			log.error("Exception occured while completing ride",e);
			return new ResponseEntity<>("An error occured while completing the ride.",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/cancel")
	public ResponseEntity<String> rideCancelled(@RequestHeader Map<String, String> headers) throws Exception {
		try {
			log.info("Received Ride cancellation request");
			String cancelRide = ridesService.cancelRide(headers);
			if(cancelRide == null || cancelRide.isEmpty()) {
				log.warn("Ride cancellation failed: service returned null or empty");
				return new ResponseEntity<>("Ride could not be cancelled. Please try again.",HttpStatus.BAD_REQUEST);
			}
			log.info("Ride cancelled successfully");
		return new ResponseEntity<>(cancelRide,HttpStatus.OK);
		} catch(Exception e) {
			log.error("Exception occured while cancelling ride",e);
			return new ResponseEntity<>("An error occured while cancelling the ride.",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
