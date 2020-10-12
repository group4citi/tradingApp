package com.rest.trade.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.rest.trade.models.Trade;
import com.rest.trade.models.UserPortfolio;
import com.rest.trade.repositories.TradeRepo;
import com.rest.trade.repositories.UserPortfolioRepo;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@RequestMapping("/api")
public class UserPortfolioController {
	@Autowired
	private TradeRepo tradeRepo;
	private UserPortfolioRepo userPortfolioRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginVerfication.class);
	
	@RequestMapping(value="/display_user_portfolio", method=RequestMethod.GET)
	public List<Trade> displayTrades(@RequestBody String email) {
		List<Trade> tradesToDisplay = new ArrayList<Trade>();
		List<UserPortfolio> userPortfolio = userPortfolioRepo.findByEmail(email);
		List<String> tradeIDs = new ArrayList<String>();
		for(int i=0;  i!=userPortfolio.size(); i++) {
			tradeIDs.add(userPortfolio.get(i).getTradeID());
		}
		
		if(tradeIDs.size() != 0) {
			for(int i=0; i!=tradeIDs.size(); i++) {
				Trade trades = tradeRepo.findByTradeID(tradeIDs.get(i));
				tradesToDisplay.add(trades);
			}
			LOG.debug("User portfolio retrieved");
			return tradesToDisplay;
		} else {
			LOG.debug("User has made no transactions to list");
			return tradesToDisplay;
		}		
	}
	
	@RequestMapping(value="/make_trade", method=RequestMethod.POST)
	public String makeTrade(@RequestBody Trade trade, @RequestBody String email) {
		Stock stock = null;
		try {
			stock = YahooFinance.get(trade.getTicker());
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(stock != null) {
			double price = stock.getQuote().getPrice().doubleValue();
			trade.setPrice(price);
			trade.setTotalAmt(price * trade.getQty());
			tradeRepo.save(trade);
			
			UserPortfolio userPortfolio = new UserPortfolio();
			userPortfolio.setEmail(email);
			userPortfolio.setTradeID(trade.getTradeID());
			userPortfolioRepo.save(userPortfolio);
			
			LOG.debug(trade.getTicker() + " transaction details saved");
			return "Trade details saved";
		} else {
			LOG.debug("Required transaction details are not entered");
			return "Enter transaction details";
		}		
	}
}
