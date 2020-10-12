package com.rest.trade.models;

public enum TradeState {
    CREATED("CREATED"),
    PROCESSING("PROCESSING"),
    FILLED("FILLED"),
    REJECTED("REJECTED");

    private String state;

    private TradeState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    } 
}
