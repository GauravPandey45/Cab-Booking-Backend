package com.CabApp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DriverClientDTO {
	
	private String drivername;
	private String username;
	private String password;
	private String car_details;
	private String location;
	private String role;

}
