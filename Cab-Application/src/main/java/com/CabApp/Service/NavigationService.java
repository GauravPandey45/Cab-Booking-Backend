package com.CabApp.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class NavigationService {
	
	@Value("${openroute.api.key}")
	private String API_KEY;
	
	@Value("${openroute.geocode.url}")
	private String GEOCODE_URL;
	
	@Value("${openroute.distance.url}")
	private String DISTANCE_URL;
	
	private final RestTemplate restTemplate = new RestTemplate();
	
	private final ObjectMapper mapper = new ObjectMapper();

	
	
	public double[] getCoordinates(String address) throws Exception {
		String url = UriComponentsBuilder.fromUriString(GEOCODE_URL)
								.queryParam("api_key", API_KEY)
								.queryParam("text", address)
								.toUriString();
		String response = restTemplate.getForObject(url, String.class);
		JsonNode json = mapper.readTree(response);
		JsonNode features = json.path("features");
		if (!features.isArray() || features.size() == 0) {
	        log.error("No coordinates found for address: " + address);
	    }

	    JsonNode coords = features.path(0).path("geometry").path("coordinates");

	    if (!coords.isArray() || coords.size() < 2) {
	        log.error("Invalid coordinate structure returned by API.");
	    }
		double lon = coords.get(0).asDouble();
		double lat = coords.get(1).asDouble();
		return new double[] {lon,lat};
	}
	
	public double getDistance(double startLon, double startLat, double endLon, double endLat) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", API_KEY);
		headers.setContentType(MediaType.APPLICATION_JSON);
		String body = String.format("{\"coordinates\":[[%f,%f],[%f,%f]]}",startLon,startLat,endLon,endLat);
		log.info("Request Sent:{}", body);
		HttpEntity<String> entity = new HttpEntity<String>(body,headers);
		ResponseEntity<String> response = restTemplate.postForEntity(DISTANCE_URL, entity, String.class);
		log.info("Response Status:{}", response.getStatusCode());
		log.info("Response Received:{}", response.getBody());
		JsonNode root = mapper.readTree(response.getBody());
		JsonNode routes = root.path("routes");
		if(routes.isArray() && routes.size() > 0) {
			double distanceInMeters = routes.get(0)
                    						.path("segments").get(0)
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
