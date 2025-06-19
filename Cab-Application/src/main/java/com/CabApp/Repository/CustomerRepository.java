package com.CabApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CabApp.Entities.Customers;

@Repository
public interface CustomerRepository extends JpaRepository<Customers, Integer> {

	Customers findByUsername(String username);

	Optional<Customers> findByEmail(String mail);

}
