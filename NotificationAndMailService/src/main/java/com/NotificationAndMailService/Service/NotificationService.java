package com.NotificationAndMailService.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

@Service
public class NotificationService {
	
	@Value("${twilio.account.sid}")
	public String ACCOUNT_SID;
	
	@Value("${twilio.auth.token}")
	public String AUTH_TOKEN;
	
	@Value("${twilio.from.phone}")
	private String fromPhone;
	
	public void sendSMS(String toPhone, String body) {
		
		Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
		Message.creator(
				new PhoneNumber(toPhone), 
				new PhoneNumber(fromPhone), 
				body)
		.create();
	}

}
