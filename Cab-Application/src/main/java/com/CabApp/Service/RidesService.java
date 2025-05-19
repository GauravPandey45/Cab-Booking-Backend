package com.CabApp.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.CabApp.DTO.RideEventDTO;
import com.CabApp.Entities.Customers;
import com.CabApp.Entities.Drivers;
import com.CabApp.Entities.Rides;
import com.CabApp.Enums.CustomerStatus;
import com.CabApp.Enums.DriverStatus;
import com.CabApp.Enums.RideStatus;
import com.CabApp.Repository.CustomerRepository;
import com.CabApp.Repository.DriversRepository;
import com.CabApp.Repository.RidesRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RidesService {
	
	@Value("${rate.per.KM}")
	private double ratePerKm;
	
	@Autowired
	RidesRepository ridesRepository;
	
	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	DriversRepository driversRepository;
	
	@Autowired
	NavigationService navigationService;
	
	@Autowired
	KafkaEventProducer kafkaEventProducer;
	
	@Transactional
	public Rides bookRide(Rides rides) throws Exception {
		Customers customer =  customerRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(customer.getRideStatus().equals(CustomerStatus.ONGOING)) {
			log.error("Customer is already in a Ride");
			throw new Exception("Customer is already in a Ride");
		}
		List<Drivers> drivers = driversRepository.findByStatus(DriverStatus.AVAILABLE);
		if(drivers.isEmpty()) {
			log.error("Drivers Not Available at the moment");
			throw new RuntimeException("Drivers Not Available at the moment");
		}
		rides.setDriverId(drivers.get(0).getDriverId());
		
		Optional<Drivers> driverdata = driversRepository.findById(rides.getDriverId());
		if(driverdata.isEmpty()){
			log.error("Driver data Not found for driverId: {}", rides.getDriverId());
		}
		Drivers driver = driverdata.get();
		driver.setStatus(DriverStatus.BUSY);
		driversRepository.save(driver);
		
		customer.setRideStatus(CustomerStatus.ONGOING);
		customerRepository.save(customer);
		
		rides.setOtp(generateOTP());
		rides.setDriverName(driver.getDriverName());
		rides.setRideDetails(driver.getCarDetails());
		rides.setCustomerId(customer.getCustomerId());
		rides.setCustomerMail(customer.getEmail());
		rides.setCustomerPhone(customer.getPhone());
		rides.setSource(rides.getSource());
		rides.setDestination(rides.getDestination());
		rides.setFare(calculateFare(rides.getSource(), rides.getDestination()));
		rides.setStatus(RideStatus.BOOKED);
		
		log.info("Entering kafka ride booking event...");
		RideEventDTO rideEventDTO = new RideEventDTO();
		try {
		log.info("Setting kafka ride booking event...");
		rideEventDTO.setDriverName(rides.getDriverName());
		rideEventDTO.setCustomerMail(rides.getCustomerMail());
		rideEventDTO.setCustomerPhone(rides.getCustomerPhone());
		rideEventDTO.setDestination(rides.getDestination());
		rideEventDTO.setSource(rides.getSource());
		rideEventDTO.setRideDetails(rides.getRideDetails());
		rideEventDTO.setOtp(rides.getOtp());
		rideEventDTO.setFare(rides.getFare());
		rideEventDTO.setEvent(rides.getStatus());
		log.debug("Ride booking DTO populated: {}", rideEventDTO);
		}catch (Exception e) {
			log.error("Exception occured during setting kafka ride booking event", e);
		}
		try{
			log.info("Sending kafka ride booking event...");
			kafkaEventProducer.sendRideEvent(rideEventDTO);
			log.info("kafka ride booking event sent successfully");
		}catch (Exception e) {
			log.error("Exception occured while sending ride booking kafka event", e);
		}
		return ridesRepository.save(rides);
		
	}
	
	@Transactional
	public String completeRide() {
		Customers customer =  customerRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(customer == null) {
			log.error("Customer Not Found for username {}", SecurityContextHolder.getContext().getAuthentication().getName());
			return "Customer Not Found";
		}
		
		Rides rideDetails =  ridesRepository.getBookedCustomer(customer.getCustomerId(), RideStatus.BOOKED);
		if(rideDetails == null) {
			log.error("Ride details Not Found for customerId {}", customer.getCustomerId());
			return "Ride Details Not Found";
		}
		
		Optional<Rides> ride = ridesRepository.findById(rideDetails.getRideId());
		if(ride.isEmpty()) {
			log.error("Ride Not Found For rideId: {}", rideDetails.getRideId());
			return "Ride Not Found For rideId: " + rideDetails.getRideId();
		}
		
		Optional<Drivers> driver = driversRepository.findById(rideDetails.getDriverId());
		if(driver.isEmpty()) {
		log.error("Driver Not Found For DriverId: {}", rideDetails.getDriverId());
		return "Driver Not Found for DriverId: " + rideDetails.getDriverId();
		}
		Rides rides = ride.get();
		rides.setStatus(RideStatus.COMPLETED);
		ridesRepository.save(rides);
		customer.setRideStatus(CustomerStatus.FREE);
		customerRepository.save(customer);
		Drivers driverdetail = driver.get();
		driverdetail.setStatus(DriverStatus.AVAILABLE);
		driversRepository.save(driverdetail);
		
		log.info("Entering kafka ride complete event...");
		RideEventDTO rideEventDTO = new RideEventDTO();
		try {
		log.info("Setting kafka ride complete event...");
		rideEventDTO.setCustomerMail(rides.getCustomerMail());
		rideEventDTO.setCustomerPhone(rides.getCustomerPhone());
		rideEventDTO.setDestination(rides.getDestination());
		rideEventDTO.setSource(rides.getSource());
		rideEventDTO.setEvent(rides.getStatus());
		log.debug("Ride complete DTO populated: {}", rideEventDTO);
		}catch (Exception e) {
			log.error("Exception occured during setting kafka ride complete event", e);
		}
		try{
			log.info("Sending kafka ride complete event...");
			kafkaEventProducer.sendRideEvent(rideEventDTO);
			log.info("kafka ride complete event sent successfully");
		}catch (Exception e) {
			log.error("Exception occured while sending ride complete kafka event", e);
		}
		log.info("Ride {} completed successfully for customer {} and driver {}", rides.getRideId(), rides.getCustomerId(), rides.getDriverId() );
		return "Ride Completed Successfully for rideId: "+rides.getRideId();
	}
	
	@Transactional
	public String cancelRide() {
		Customers customer =  customerRepository.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
		if(customer == null) {
			log.error("Customer Not Found for username {}", SecurityContextHolder.getContext().getAuthentication().getName());
			return "Customer Not Found";
		}
		
		Rides rideDetails =  ridesRepository.getBookedCustomer(customer.getCustomerId(), RideStatus.BOOKED);
		if(rideDetails == null) {
			log.error("Ride details Not Found for customerId {}", customer.getCustomerId());
			return "Ride Details Not Found";
		}
		
		Optional<Rides> ride = ridesRepository.findById(rideDetails.getRideId());
		if(ride.isEmpty()) {
			log.error("Ride Not Found For rideId: {}", rideDetails.getRideId());
			return "Ride Not Found For rideId: " + rideDetails.getRideId();
		}
		
		Optional<Drivers> driver = driversRepository.findById(rideDetails.getDriverId());
		if(driver.isEmpty()) {
		log.error("Driver Not Found For DriverId: {}", rideDetails.getDriverId());
		return "Driver Not Found for DriverId: " + rideDetails.getDriverId();
		}
		Rides rides = ride.get();
		rides.setStatus(RideStatus.CANCELLED);
		ridesRepository.save(rides);
		customer.setRideStatus(CustomerStatus.FREE);
		customerRepository.save(customer);
		Drivers driverdetail = driver.get();
		driverdetail.setStatus(DriverStatus.AVAILABLE);
		driversRepository.save(driverdetail);
		
		log.info("Entering kafka ride cancel event...");
		RideEventDTO rideEventDTO = new RideEventDTO();
		try {
		log.info("Setting kafka ride cancel event...");
		rideEventDTO.setCustomerMail(rides.getCustomerMail());
		rideEventDTO.setCustomerPhone(rides.getCustomerPhone());
		rideEventDTO.setDestination(rides.getDestination());
		rideEventDTO.setSource(rides.getSource());
		rideEventDTO.setEvent(rides.getStatus());
		log.debug("Ride cancel DTO populated: {}", rideEventDTO);
		}catch (Exception e) {
			log.error("Exception occured during setting kafka ride cancel event", e);
		}
		try{
			log.info("Sending kafka ride cancel event...");
			kafkaEventProducer.sendRideEvent(rideEventDTO);
			log.info("kafka ride cancel event sent successfully");
		}catch (Exception e) {
			log.error("Exception occured while sending ride cancel kafka event", e);
		}
		log.info("Ride {} cancelled for customer {} and driver {}", rides.getRideId(), rides.getCustomerId(), rides.getDriverId() );
		return "Ride Cancelled Sucessfully for rideId: "+rides.getRideId();
		
	}
	
	public double calculateFare(String source, String destination) throws Exception {
		double[] src = navigationService.getCoordinates(source);
		log.info("Source geocode API response:{}",src);
		double[] des = navigationService.getCoordinates(destination);
		log.info("Destination geocode API response:{}",des);
		double distance = navigationService.getDistance(src[0],src[1], des[0], des[1]);
		log.info("Distance API response:{}",distance);
		Double farePrice = (int)distance * ratePerKm;
		return farePrice;
	}
	
	public int generateOTP() {
		Random random = new Random();
		int otp = random.nextInt(1000,9000);
		if(otp < 1000 || otp >= 9000) {
			log.error("Error in generating otp");
		}
		return otp;
	}
	

}
