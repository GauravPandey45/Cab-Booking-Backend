package com.AuthenticationService.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.AuthenticationService.Entity.DriversAuth;
import com.AuthenticationService.Entity.UsersAuth;
import com.AuthenticationService.Repository.DriverRepository;
import com.AuthenticationService.Repository.UserRepository;

@Service
public class UserAndDriverDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	UserRepository userRepository;
	
	@Autowired
	DriverRepository driverRepository;

	   @Override
	    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	        Optional<UsersAuth> userOpt = userRepository.findByUsername(username);
	        if (userOpt.isPresent()) {
	            UsersAuth user = userOpt.get();
	            return new org.springframework.security.core.userdetails.User(
	                    user.getUsername(),
	                    user.getPassword(),
	                    List.of(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
	        }
	        Optional<DriversAuth> driverOpt = driverRepository.findByUsername(username);
	        if (driverOpt.isPresent()) {
	            DriversAuth driver = driverOpt.get();
	            return new org.springframework.security.core.userdetails.User(
	                    driver.getUsername(),
	                    driver.getPassword(),
	                    List.of(new SimpleGrantedAuthority("ROLE_DRIVER")));
	        }
	        throw new UsernameNotFoundException("User not found: " + username);
	    }
	
}
