package com.rest.trade.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rest.trade.models.UserPortfolio;

public interface UserPortfolioRepo extends MongoRepository<UserPortfolio, String> {
	List<UserPortfolio> findByEmail(String email);
}
