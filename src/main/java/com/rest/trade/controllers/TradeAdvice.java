package com.rest.trade.controllers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rest.trade.models.CompanyPortfolio;
import com.rest.trade.models.Trade;
import com.rest.trade.models.UserPortfolio;
import com.rest.trade.repositories.CompanyPortfolioRepo;
import com.rest.trade.repositories.TradeRepo;
import com.rest.trade.repositories.UserPortfolioRepo;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/api")
public class TradeAdvice {
	
	@Autowired
	private  TradeRepo tradeRepo;
	
	@Autowired
	private UserPortfolioRepo userPortfolioRepo;
	
	@Autowired
	private CompanyPortfolioRepo cmpPortfolioRepo;
	
	private static final Logger LOG = LoggerFactory.getLogger(TradeAdvice.class);
	
	@RequestMapping(value="/prediction", method=RequestMethod.GET, consumes = MediaType.APPLICATION_JSON_VALUE)
//	public String displayTrades(@RequestBody ObjectNode objectNode) {
		public String displayTrades(@RequestParam String email, @RequestParam String ticker, @RequestParam String quantity) {
		
		//String email = objectNode.get("email").asText();
		List<Trade> tradesToDisplay = new ArrayList<Trade>();
		List<String> types = new ArrayList<>();
		List<Double> amt = new ArrayList<>();
		double amount= 10000.0;
		BigDecimal tickerPrice = new BigDecimal("0.0");
		double tot = 0.0;
		String s = "";
		String ret_str = "";
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
				
				types.add(trades.getType().toString());
				amt.add(trades.getTotalAmt());
				//System.out.println(trades.getTotalAmt())
			}
			for(int i=0;i<types.size();i++) {
				if(types.get(i).equals("BUY"))
				{
					amount = amount - amt.get(i);
				}
				if(types.get(i).equals("SELL"))
				{
					amount = amount + amt.get(i);
				}
			}
			//String ticker = objectNode.get("ticker").asText();
			int qty = Integer.parseInt(quantity);
			//int qty = objectNode.get("qty").asInt();
			List<CompanyPortfolio> trade = new ArrayList<CompanyPortfolio>();
		    trade = cmpPortfolioRepo.findAll();
			// tickerPrice = 0.0;
			for(int i=1;  i<trade.size(); i++) {
				if(trade.get(i).getTicker().equals(ticker)) 
					tickerPrice = trade.get(i).getPrice();				
			}
			double tp = tickerPrice.doubleValue();
			 tot = tp* qty;
			 //String s ="";
			 double buy = amount - tot;
			 double sell = amount + tot;
			 s += "Total amount you have is" + amount +"."+"\n"+ ticker +" "+ "for" + qty + " "+ "is" + " " +tot;
			ret_str += "Balance on Buying "+ ticker + " " +"of" + " "+qty + " "+"is" +" "+ buy+ "\n" +"Balance on Selling "+" "+ticker +" "+ "of" + " "+qty + " "+"is"
+" "+ sell;
			LOG.debug("User portfolio retrieved");
			return ret_str;
		}
		 else {
			LOG.debug("User has made no transactions to list");
			return "";
			
		}		
	
	}
}
	
		