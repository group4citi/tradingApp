package com.rest.trade.models;

import java.math.BigDecimal;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company_portfolio")
public class CompanyPortfolio {
	@Id
	private String ticker;
	private String name;
	private String stock_exchange;
	private String currency;
	private BigDecimal price;
	private BigDecimal dayHigh;
	private BigDecimal dayLow;
	private BigDecimal dayOpen;
	private Long avgVol;
	public String getTicker() {
		return ticker;
	}
	public void setTicker(String ticker) {
		this.ticker = ticker;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStock_exchange() {
		return stock_exchange;
	}
	public void setStock_exchange(String stock_exchange) {
		this.stock_exchange = stock_exchange;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public BigDecimal getDayHigh() {
		return dayHigh;
	}
	public void setDayHigh(BigDecimal dayHigh) {
		this.dayHigh = dayHigh;
	}
	public BigDecimal getDayLow() {
		return dayLow;
	}
	public void setDayLow(BigDecimal dayLow) {
		this.dayLow = dayLow;
	}
	public BigDecimal getDayOpen() {
		return dayOpen;
	}
	public void setDayOpen(BigDecimal dayOpen) {
		this.dayOpen = dayOpen;
	}
	public Long getAvgVol() {
		return avgVol;
	}
	public void setAvgVol(Long avgVol) {
		this.avgVol = avgVol;
	}
	
}
