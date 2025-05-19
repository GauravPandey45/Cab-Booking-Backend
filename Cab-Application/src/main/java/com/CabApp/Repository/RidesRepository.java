package com.CabApp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.CabApp.Entities.Rides;
import com.CabApp.Enums.RideStatus;

@Repository
public interface RidesRepository extends JpaRepository<Rides, Integer> {

	@Query("Select r from Rides r where r.customerId= :customerId and r.status= :status")
	Rides getBookedCustomer(@Param("customerId") int customerId, @Param("status") RideStatus status);
	
}
