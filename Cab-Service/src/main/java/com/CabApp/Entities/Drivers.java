package com.CabApp.Entities;

import com.CabApp.Enums.DriverStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "drivers_info")
public class Drivers {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	@JsonIgnore
	private int driverId;
	
	@Column(nullable = false)
	private String drivername;
	
	@Column(nullable = false)
	private String username;
	
	@Column(nullable = false)
	private String car_details;
	
	@Column(nullable = false)
	private String location;
	
	@Column(nullable = false)
	private String role;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	@JsonIgnore
	private DriverStatus status;
	
	
}
