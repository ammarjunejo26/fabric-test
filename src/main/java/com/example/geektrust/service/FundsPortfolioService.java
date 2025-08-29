package com.example.geektrust.service;

import com.example.geektrust.domain.Fund;

import java.util.List;
import java.util.Optional;

public interface FundsPortfolioService {

    void addStockToFundsPortfolio(String fundName, String stockName);

    boolean isFundExists(String fundName);

    Optional<Fund> findFundByName(String fundName);

    List<Fund> getAllFunds();
}
