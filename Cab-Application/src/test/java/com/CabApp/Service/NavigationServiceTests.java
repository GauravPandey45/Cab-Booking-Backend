package com.CabApp.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class NavigationServiceTests {
	
	@Autowired
	private RestTemplate restTemplate;
		
	@Test
	public void getCoordinates() {
		String url = "https://api.openrouteservice.org/geocode/search?api_key=5b3ce3597851110001cf624804d34ee433ea4cfb8737e51b89b399d6&text=Kengeri,Bangalore";
		JsonNode response = restTemplate.getForObject(url, JsonNode.class);
		assertNotNull(response, "response should not be null");
		
		JsonNode features = response.path("features");
		System.out.println(features);
		assertTrue(features.isArray() || features.size() == 0, "features should not be null");
		
	    JsonNode coordinates = features.path(0).path("geometry").path("coordinates");
	    assertTrue(coordinates.isArray() || coordinates.size() < 2, "Invalid coordinate structure returned by API.");
	    
		assertTrue(coordinates.get(0).asDouble()!=0 && coordinates.get(1).asDouble()!=0 , "Coordinates should not be zero(0)");
	}
	
	@Test
	public void getDistance() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "5b3ce3597851110001cf624804d34ee433ea4cfb8737e51b89b399d6");
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("coordinates", List.of(List.of(72.84022,19.054979), List.of(72.846421,19.119698)));
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody,headers);
		ResponseEntity<JsonNode> response = restTemplate.postForEntity("https://api.openrouteservice.org/v2/directions/driving-car", entity, JsonNode.class);
		JsonNode routes = response.getBody();
		assertNotNull(routes, "routes should not be null") ;
		double distanceInMeters = routes.path("routes").get(0)
                    						.path("summary")
                    						.path("distance")
                    						.asDouble();
		double distanceInKM =  distanceInMeters / 1000.0;
		assertTrue(distanceInKM > 0);
		}
}

