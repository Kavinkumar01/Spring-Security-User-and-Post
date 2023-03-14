package com.springsecurityaccess.demo.model;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import com.springsecurityaccess.demo.repository.UserRepository;

@Component
public class CustomUserDetailsService implements UserDetailsService {
	
	@Autowired
	public UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> user=userRepository.findByUserName(username);
//		CustomUserDetails userDetails= new CustomUserDetails(user);
//		return userDetails;
		return user.map(CustomUserDetails::new).orElseThrow(()-> new UsernameNotFoundException("The username "+username+" was not found in the System"));
	}

}
