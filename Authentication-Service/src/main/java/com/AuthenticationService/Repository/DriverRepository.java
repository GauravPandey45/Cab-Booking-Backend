package com.AuthenticationService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AuthenticationService.Entity.DriversAuth;

@Repository
public interface DriverRepository extends JpaRepository<DriversAuth, Integer> {
	
	Optional<DriversAuth> findByUsername(String username);

}
