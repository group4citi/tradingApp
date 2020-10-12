package com.rest.trade.models;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "trade")
public class Trade {
	@Id
	private String tradeID;
	private Date date;
	private String state;
	private String type;
	private String ticker;
	private int qty;
	private double price;
	private double totalAmt;
	
	public Trade(String tradeID, String state, String type, String ticker, int qty, double price) {
		super();
		this.tradeID = tradeID;
		this.date = new Date(); 
		this.state = state;
		this.type = type;
		this.ticker = ticker;
		this.qty = qty;
		this.price = price;
	}

	public String getTradeID() {
		return tradeID;
	}

	public void setTradeID(String tradeID) {
		this.tradeID = tradeID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTicker() {
		return ticker;
	}

	public void setTicker(String ticker) {
		this.ticker = ticker;
	}

	public int getQty() {
		return qty;
	}

	public void setQty(int qty) {
		this.qty = qty;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}
	
	public double getTotalAmt() {
		return totalAmt;
	}

	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}

	
}
