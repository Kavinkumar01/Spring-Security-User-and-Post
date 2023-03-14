package com.springsecurityaccess.demo.controller;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springsecurityaccess.demo.common.Constants;
import com.springsecurityaccess.demo.model.User;
import com.springsecurityaccess.demo.repository.UserRepository;

@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	public BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	public UserRepository userRepository;
	
	@PostMapping("/register")
	public String registerUser(@RequestBody User user) {
		String pwd=user.getPassword();
		user.setPassword(bCryptPasswordEncoder.encode(pwd));
		user.setRoles(Constants.ROLE_USER);
		user.setActive(true);
		userRepository.save(user);
		return "The registration was successfully done for the user: "+user.getUserName();
	}
	
	@GetMapping("/access/{userId}/{userRole}")
	@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR')")
	public String giveAccessToUser(@PathVariable int userId,@PathVariable String userRole, Principal principal) {
		User user=userRepository.findById(userId).get();
		List<String> activeRoles=getRolesOfLoggedInUser(principal);//getting the roles of the loggedin user who is providing access to the other user
		String newRole="";
		if(activeRoles.contains(userRole)) {
			newRole=user.getRoles()+","+userRole;
			user.setRoles(newRole);
		}
		userRepository.save(user);
		return "Hi "+user.getUserName()+" you have been assigned a new role by "+principal.getName();
	}
	
	public User getLoggedInUser(Principal principal) {
		return userRepository.findByUserName(principal.getName()).get();
	}
	
	public List<String> getRolesOfLoggedInUser(Principal principal){
		String roles=getLoggedInUser(principal).getRoles();
		List<String> assignRoles=Arrays.asList(roles.split(","));
		if(assignRoles.contains("ROLE_ADMIN")) {
			return Arrays.asList(Constants.ADMIN_ACCESS);
		}else if(assignRoles.contains("ROLE_MODERATOR")) {
			return Arrays.asList(Constants.MODERATOR_ACCESS);
		}
		return Collections.EMPTY_LIST;
	}
	
	@GetMapping
	@PreAuthorize("hasAuthority('ROLE_ADMIN')")
	public List<User> loadUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("/test")	
	@PreAuthorize("hasAuthority('ROLE_USER')")
	public String testUserAccess(){
		return "This url can be accessed only by user";
	}
	
}
