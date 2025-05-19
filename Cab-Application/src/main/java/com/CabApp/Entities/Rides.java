 package com.CabApp.Entities;

import com.CabApp.Enums.RideStatus;
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
@Table(name = "rides_info")
public class Rides {
	
	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rideId;
	
	@JsonIgnore
	@Column(nullable = false)
	private int customerId;
	
	@Column(nullable = false)
	private String customerMail;
	
	@Column(nullable = false)
	private String customerPhone;
	
	@JsonIgnore
	@Column(nullable = false)
	private int driverId;
	
	@Column(nullable = false)
	private String driverName;
	
	@Column(nullable = false)
	private String rideDetails;
	
	@Column(nullable = false)
	private int otp;
	
	@Column(nullable = false)
	private String source;
	
	@Column(nullable = false)
	private String destination;
	
	@Column(nullable = false)
	private double fare;
	
	@JsonIgnore
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private RideStatus status;
}
