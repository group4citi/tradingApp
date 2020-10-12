package com.rest.trade.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.rest.trade.models.CompanyPortfolio;

public interface CompanyPortfolioRepo extends MongoRepository<CompanyPortfolio, String>{

}
