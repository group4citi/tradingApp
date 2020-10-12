package com.rest.trade.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rest.trade.models.Trade;
import com.rest.trade.models.TradeState;
import com.rest.trade.models.TradeType;
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
	
	@Autowired
	private UserPortfolioRepo userPortfolioRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(UserPortfolioController.class);
	
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
	public Trade makeTrade(@RequestBody ObjectNode objectNode) {
//	public Trade makeTrade(@RequestBody String tradeID, @RequestBody String ticker, 
//			@RequestBody int qty, @RequestBody String type, @RequestBody String email) {
		String tradeID = objectNode.get("tradeID").asText();
		String ticker = objectNode.get("ticker").asText();
		int qty = objectNode.get("qty").asInt();
		String type = objectNode.get("type").asText();
		String email = objectNode.get("email").asText();
		
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
			tradeRepo.save(trade);
			
			if(type.equalsIgnoreCase("buy"))
				trade.setType(TradeType.BUY);
			else trade.setType(TradeType.SELL);
			
			UserPortfolio userPortfolio = new UserPortfolio();
			userPortfolio.setEmail(email);
			userPortfolio.setTradeID(tradeID);
			userPortfolioRepo.save(userPortfolio);
			
			LOG.debug(trade.getTicker() + " transaction details saved");
			return trade;
		} else {
			LOG.debug("Required transaction details are not entered");
			return trade;
		}		
	}
}
