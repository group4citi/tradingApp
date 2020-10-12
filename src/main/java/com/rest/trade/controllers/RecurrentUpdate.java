package com.rest.trade.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.rest.trade.models.Trade;
import com.rest.trade.models.TradeState;
import com.rest.trade.repositories.TradeRepo;

@Component
public class RecurrentUpdate {
	@Autowired TradeRepo tradeRepo;
	private static final Logger LOG = LoggerFactory.getLogger(RecurrentUpdate.class);
	
	@Transactional
    public List<Trade> findTradesForProcessing(){
		List<Trade> foundTrades = tradeRepo.findByState(TradeState.CREATED);

        for(Trade thisTrade: foundTrades) {
            thisTrade.setState(TradeState.PROCESSING);
            tradeRepo.save(thisTrade);
        }
        return foundTrades;
    }
	
	@Transactional
    public List<Trade> findTradesForFilling(){
        List<Trade> foundTrades = tradeRepo.findByState(TradeState.PROCESSING);

        for(Trade thisTrade: foundTrades) {
            if((int) (Math.random()*10) > 8) {
                thisTrade.setState(TradeState.REJECTED);
            }
            else {
                thisTrade.setState(TradeState.FILLED);
            }
            tradeRepo.save(thisTrade);
        }
        return foundTrades;
    }
	
//	@Scheduled(fixedRateString = "${scheduleRateMs:10000}")
	@Scheduled(fixedRate = 5000)
    public void runSim() {
		
        LOG.debug("Main loop running!");

        int tradesForFilling = findTradesForFilling().size();
        LOG.debug("Found " + tradesForFilling + " trades to be filled/rejected");

        int tradesForProcessing = findTradesForProcessing().size();
        LOG.debug("Found " + tradesForProcessing + " trades to be processed");

    }
}
