package com.rest.trade.controllers;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.trade.models.UserCredentials;
import com.rest.trade.repositories.UserCredentialsRepo;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class LoginVerfication {
	@Autowired 
	private UserCredentialsRepo userCredentialsRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginVerfication.class);
	
	@RequestMapping(value="/signup", method=RequestMethod.POST)
	public UserCredentials signUp(@RequestBody UserCredentials userCredentials) {
		userCredentialsRepo.save(userCredentials);
		LOG.debug("User signed up successfully");
		return userCredentials;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String verifyCredentials(@RequestBody Map<String, String> data) {
		String email = data.get("email");
		String password = data.get("password");

		UserCredentials userCredentials = userCredentialsRepo.findByEmail(email);
		
		if(userCredentials != null) {
			if((userCredentials.getPassword()).equals(password)) {
				LOG.debug("Login success");
				return "Login success";
			} else {
				LOG.debug("Email id and password mismatch. Try again");
				return "Login failed";
			}
		} else {
			LOG.debug("Not an existing user. Please sign up to proceed");
			return "Not an existing user";
		}
	}
	
}
