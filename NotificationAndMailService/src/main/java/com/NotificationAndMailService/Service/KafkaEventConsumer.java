package com.NotificationAndMailService.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.Kafka.DTO.RideEventDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KafkaEventConsumer {
	
	@Value("${booked.mail.subject}")
	private String bookedSubject;
	
	@Value("${complete.mail.subject}")
	private String completeSubject;
	
	@Value("${cancel.mail.subject}")
	private String cancelSubject;
	
	@Autowired
	MailService mailService;
	
	@Autowired
	NotificationService notificationService;
	
	
	@KafkaListener(topics = "ride-events-topic",groupId = "notification-service")
	public void receiveEvent(RideEventDTO rideEventDTO) {
		if(rideEventDTO == null || rideEventDTO.getEvent() == null) {
			log.error("Received null or incomplete RideEventDTO from Kafka");
			return;
		}
		log.debug("Received kafka event: {}", rideEventDTO);
		String subject="";
		String message="";
		
		switch(rideEventDTO.getEvent()) {
		case BOOKED:
		 subject = bookedSubject;
		 message = "Your Ride is Booked \n"
						+ "please find your ride details below. Enjoy your Ride"+"\n"+"\n"
						+ "Driver Name: " + rideEventDTO.getDriverName()+"\n"
						+ "Ride Details: "+ rideEventDTO.getRideDetails()+"\n"
						+ "OTP: " + rideEventDTO.getOtp()+"\n"
						+ "Source: " + rideEventDTO.getSource()+"\n"
						+ "Destination: " + rideEventDTO.getDestination()+"\n"
						+ "Fare: " + rideEventDTO.getFare();
		 	break;
		 
		case COMPLETED:
			subject = completeSubject;
			 message = "Your Ride from "+" "
					+ rideEventDTO.getSource()+" "
					+ rideEventDTO.getDestination()+" "
					+ "is Completed"+"\n"
					+ "Hope you had a comfortable Ride";
			 break;
	
		case CANCELLED:
			subject = cancelSubject;
			 message = "Your Ride from"+" "
					+ rideEventDTO.getSource()+" "
					+ rideEventDTO.getDestination()+" "
					+ "is Cancelled"+"\n"
					+ "We Regret For The Inconvenience Caused";
			 break;
			 
			 default:
				 log.warn("Unknown ride event type: {}", rideEventDTO);
		}
		
		String phone = rideEventDTO.getCustomerPhone();
		if(!phone.startsWith("+")) {
			phone = "+91" + phone;
		}
		
		notificationService.sendSMS(phone, message);
		
		mailService.sendMail(rideEventDTO.getCustomerMail(), subject, message);
	}
}
