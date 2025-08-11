package com.AuthenticationService.Service;

import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.AuthenticationService.Entity.UsersAuth;
import com.AuthenticationService.Repository.UserRepository;
import com.AuthenticationService.Security.JwtUtil;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class GoogleAuthService {
	
	@Value("${google.auth.token.api}")
	private String googleTokenURL;
	
	@Value("${google.client.id}")
	private String CLIENT_ID;
	
	@Value("${google.client.secret}")
	private String CLIENT_SECRET;
	
	@Value("${google.redirect.url}")
	private String redirectURL;
	
	@Value("${google.userinfo.url}")
	private String googleUserInfoUrl;
	
	@Value("${google.phoneinfo.url}")
	private String googlePhoneInfoUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Transactional
	public String handleGoogleAuth(String authcode) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		MultiValueMap<String, String> formParams = new LinkedMultiValueMap<>();
		formParams.add("grant_type", "authorization_code");
		formParams.add("code", authcode);
		formParams.add("redirect_uri", redirectURL);
		formParams.add("client_id", CLIENT_ID);
		formParams.add("client_secret", CLIENT_SECRET);
		log.info("Creating auth token request with parameters: {}",formParams );
		HttpEntity<MultiValueMap<String, String>> userInfoRequest = new HttpEntity<MultiValueMap<String,String>>(formParams, headers);
		log.info("auth token request Created: {}",userInfoRequest );
		try {
		log.info("Calling google auth token URL with request:{}",userInfoRequest);
		ResponseEntity<Map> response = restTemplate.postForEntity(googleTokenURL, userInfoRequest, Map.class);
		if(response == null) {
			log.error("Null or Empty response received from auth token api:{}",response);
			throw new Exception("Null or Empty response received");
		}
		log.info("Response received from google auth token URL:{}",response);
		String accessToken = (String) response.getBody().get("access_token");
		if(accessToken==null || accessToken.isEmpty()) {
			log.error("access token is null or empty: {}", accessToken);
			throw new Exception("access token is null or empty");
		}
		log.info("Building user info URI with access token: {}",accessToken);
		String userInfoUrl = UriComponentsBuilder.fromUriString(googleUserInfoUrl).queryParam("access_token", accessToken).toUriString();
		log.info("Calling google user info URL: {}",userInfoUrl);
		ResponseEntity<Map> userInfo =  restTemplate.getForEntity(userInfoUrl, Map.class);
		if(userInfo == null) {
			log.error("Null or Empty response received from user info api:{}",userInfo);
			throw new Exception("Null or Empty response received from user info api");
		}
		log.info("Response received from user info api:{}",userInfo);
		String userMail = (String) userInfo.getBody().get("email");
		log.info("email retrieved from user info response: {}",userMail);
		Optional<UsersAuth> user = userRepository.findByEmail(userMail);
		if(user.isPresent()) {
			log.info("User present in DB, generating jwt token for: {}",user);
			String jwtToken = jwtUtil.generateToken(user.get().getUsername());
			return jwtToken;
		}
		log.info("User not present in DB, Signing up new user for : {}",userInfo);
		UsersAuth newuser = new UsersAuth();
		newuser.setName(userInfo.getBody().get("given_name").toString());
		newuser.setEmail(userMail);
		newuser.setUsername(userInfo.getBody().get("given_name").toString().concat(userInfo.getBody().get("sub").toString().substring(0,5)));
		newuser.setPassword(userMail);
		newuser.setPhone("9591392794");
		newuser.setRole("ROLE_CUSTOMER");
		userRepository.save(newuser);
		log.info("User signup completed, generating jwt token for: {}",user);
		String jwtToken = jwtUtil.generateToken(newuser.getUsername());
		return jwtToken;
		}catch (Exception e) {
			log.error("Google login failed due to", e);
			throw new Exception("Google login failed due to "+e.getMessage(),e);
		}
	}
}
