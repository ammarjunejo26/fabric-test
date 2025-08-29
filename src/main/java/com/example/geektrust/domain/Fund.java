package com.example.geektrust.domain;

import java.util.Set;

public class Fund {

    private String name;
    private Set<String> stocks;

    public Fund() {
    }

    public Fund(String name, Set<String> stocks) {
        this.name = name;
        this.stocks = stocks;
    }

    public String getName() {
        return name;
    }

    public Set<String> getStocks() {
        return stocks;
    }

    public void addStock(String stock) {
        stocks.add(stock);
    }
}
