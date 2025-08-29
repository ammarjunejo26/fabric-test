package com.example.geektrust.domain;

import java.util.List;
import java.util.Optional;

public class FundsPortfolio {

    private List<Fund> funds;

    public void addStockToAFund(String fundName, String stockName) {
        Optional<Fund> fundOpt = findFundByName(fundName);
        fundOpt.ifPresent(fund -> fund.addStock(stockName));
    }

    public boolean isFundExists(String fundName) {
        return findFundByName(fundName).isPresent();
    }

    public Optional<Fund> findFundByName(String fundName) {
        return funds.stream().filter(f -> f.getName().equals(fundName)).findFirst();
    }

    public void setFunds(List<Fund> funds) {
        this.funds = funds;
    }

    public List<Fund> getFunds() {
        return funds;
    }
}
