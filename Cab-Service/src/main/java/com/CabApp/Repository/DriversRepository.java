package com.CabApp.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CabApp.Entities.Drivers;
import com.CabApp.Enums.DriverStatus;

@Repository
public interface DriversRepository extends JpaRepository<Drivers, Integer>{
	
	
	public List<Drivers> findByStatus(DriverStatus status);

	public Drivers findByUsername(String username);

}
