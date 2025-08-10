package com.CabApp.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.CabApp.Entities.Users;

@Repository
public interface CustomerRepository extends JpaRepository<Users, Integer> {

	Users findByUsername(String username);

	Optional<Users> findByEmail(String email);

}
