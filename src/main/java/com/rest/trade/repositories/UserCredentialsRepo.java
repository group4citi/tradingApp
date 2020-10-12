package com.rest.trade.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rest.trade.models.UserCredentials;

public interface UserCredentialsRepo extends MongoRepository<UserCredentials, String> {
	UserCredentials findByEmail(String email);
}
