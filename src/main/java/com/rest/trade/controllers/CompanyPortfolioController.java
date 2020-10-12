package com.rest.trade.controllers;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.trade.models.CompanyPortfolio;
import com.rest.trade.repositories.CompanyPortfolioRepo;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/api")
public class CompanyPortfolioController {
	@Autowired 
	private CompanyPortfolioRepo companyPortfolioRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(CompanyPortfolioController.class);
	
	private List<String> tickers = new ArrayList<String>();
	
	private Stock stock = null;
	
	@RequestMapping(value="/load_data", method=RequestMethod.POST)
	public String loadData() {
		this.companyPortfolioRepo.deleteAll();
		LOG.debug("Loading fresh data into the database");
		
		tickers.addAll(Arrays.asList("AAPL", "AMZN", "GOOG", "BABA", "FB", "TWTR", "V", "TSLA",
	                        "WMT", "PG", "JNJ", "JPM", "CRM", "PFE", "NIKE", "ADBE", "PEP",
	                        "SAP", "ORCL", "ACN", "IBM"));
		
		for(int i = 0; i!=tickers.size(); i++ ) {
			try {
				stock = YahooFinance.get(tickers.get(i));
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(stock != null) {
				CompanyPortfolio companyPortfolio = new CompanyPortfolio();
				
				companyPortfolio.setTicker(tickers.get(i));
				companyPortfolio.setName(stock.getName());
				companyPortfolio.setStock_exchange(stock.getStockExchange());
				companyPortfolio.setCurrency(stock.getCurrency());
				companyPortfolio.setPrice(stock.getQuote().getPrice());
				companyPortfolio.setAvgVol(stock.getQuote().getAvgVolume());
				companyPortfolio.setDayHigh(stock.getQuote().getDayHigh());
				companyPortfolio.setDayLow(stock.getQuote().getDayLow());
				companyPortfolio.setDayOpen(stock.getQuote().getOpen());
				
				companyPortfolioRepo.save(companyPortfolio);
				
				LOG.debug(tickers.get(i) + " data refreshed");
			} 
		}
		return "Company portfolios updated";
	}
	
}
