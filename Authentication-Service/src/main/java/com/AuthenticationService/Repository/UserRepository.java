package com.AuthenticationService.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.AuthenticationService.Entity.UsersAuth;

@Repository
public interface UserRepository extends JpaRepository<UsersAuth, Integer> {

	Optional <UsersAuth> findByUsername(String username);


}
