package com.CabApp.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class NavigationService {
	
	@Value("${openroute.api.key}")
	private String API_KEY;
	
	@Value("${openroute.geocode.url}")
	private String GEOCODE_URL;
	
	@Value("${openroute.distance.url}")
	private String DISTANCE_URL;
	
	@Autowired
	private RestTemplate restTemplate = new RestTemplate();
	

	public double[] getCoordinates(String address) throws Exception {
		String url = GEOCODE_URL.replace("Api_Key", API_KEY).replace("Location", address);
		JsonNode response = restTemplate.getForObject(url, JsonNode.class);
		JsonNode features = response.get("features");
		if (!features.isArray() || features.size() == 0) {
	        log.error("No coordinates found for address: " + address);
	    }
	    JsonNode coordinates = features.path(0).path("geometry").path("coordinates");
	    if (!coordinates.isArray() || coordinates.size() < 2) {
	        log.error("Invalid coordinate structure returned by API.");
	    }
		double[] Coordinates = {coordinates.get(0).asDouble(), coordinates.get(1).asDouble()};
		return Coordinates;
	}
	
	public double getDistance(double startLon, double startLat, double endLon, double endLat) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", API_KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);
		//String body = String.format("{\"coordinates\":[[%f,%f],[%f,%f]]}",startLon,startLat,endLon,endLat);
		Map<String, Object> requestBody = new HashMap<>();
		requestBody.put("coordinates", List.of(List.of(startLon, startLat), List.of(endLon, endLat)));
		log.info("Request Sent:{}", requestBody);
		HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody,headers);
		ResponseEntity<JsonNode> response = restTemplate.postForEntity(DISTANCE_URL, entity, JsonNode.class);
		log.info("Response Status:{}", response.getStatusCode());
		JsonNode routes = response.getBody();
		log.info("Response Received:{}", response.getBody());
		if(!routes.isArray() && routes.size() > 0) {
			double distanceInMeters = routes.path("routes").get(0)
                    						.path("summary")
                    						.path("distance")
                    						.asDouble();
		double distanceInKM =  distanceInMeters / 1000.0;
		return distanceInKM;
		}else{
			log.error("No route found from response");
			return 0;
			}
		}
}
