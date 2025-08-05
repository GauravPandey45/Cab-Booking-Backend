package com.CabApp.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.Kafka.DTO.RideEventDTO;

@Service
public class KafkaEventProducer {
	
	@Autowired
	private KafkaTemplate<String, RideEventDTO> kafkaTemplate;
	
	public void sendRideEvent(RideEventDTO bookedDTO) {
		kafkaTemplate.send("ride-events-topic", bookedDTO);
	}

}
