package com.rest.trade.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.rest.trade.models.Trade;
import com.rest.trade.models.TradeType;
import com.rest.trade.models.UserPortfolio;
import com.rest.trade.repositories.TradeRepo;
import com.rest.trade.repositories.UserPortfolioRepo;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class UserPortfolioController {
	@Autowired
	private TradeRepo tradeRepo;
	
	@Autowired
	private UserPortfolioRepo userPortfolioRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserPortfolioController.class);
	
	@RequestMapping(value="/display_user_portfolio", method=RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<Trade> displayTrades(@RequestParam String email) {
	
		List<Trade> tradesToDisplay = new ArrayList<Trade>();
		List<UserPortfolio> userPortfolio = new ArrayList<UserPortfolio>();
	    userPortfolio = userPortfolioRepo.findAll();
		List<String> tradeIDs = new ArrayList<String>();
		for(int i=1;  i<userPortfolio.size(); i++) {
			if(userPortfolio.get(i).getEmail().equals(email)) {
				tradeIDs.add(userPortfolio.get(i).getTradeID());
			}
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
	
	@RequestMapping(value="/make_trade", method=RequestMethod.GET)
//	public Trade makeTrade(@RequestBody ObjectNode objectNode) {
	public String makeTrade(@RequestParam String ticker, 
			@RequestParam String quantity, @RequestParam String type, @RequestParam String email) {
		Random r = new Random();
		int random = r.nextInt(100);
		String tradeID = "T"+ random;
		int qty = Integer.parseInt(quantity);
		/*
		String tradeID = objectNode.get("tradeID").asText();
		String ticker = objectNode.get("ticker").asText();
		int qty = objectNode.get("qty").asInt();
		String type = objectNode.get("type").asText();
		String email = objectNode.get("email").asText(); */
		
		Stock stock = null;
		Trade trade = new Trade();
		try {
			stock = YahooFinance.get(ticker);
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(stock != null) {
			trade.setTradeID(tradeID);
			trade.setTicker(ticker);
			trade.setQty(qty);
			double price = stock.getQuote().getPrice().doubleValue();
			trade.setPrice(price);
			trade.setTotalAmt(price * trade.getQty());
		
			
			if(type.equalsIgnoreCase("buy"))
				trade.setType(TradeType.BUY);
			else trade.setType(TradeType.SELL);
			tradeRepo.save(trade);
			
			UserPortfolio userPortfolio = new UserPortfolio();
			userPortfolio.setEmail(email);
			userPortfolio.setTradeID(tradeID);
			userPortfolioRepo.save(userPortfolio);
			
			LOG.debug(trade.getTicker() + " transaction details saved");
			return "Trade Success";
		} else {
			LOG.debug("Required transaction details are not entered");
			return "Trade failed";
		}		
	}
}
