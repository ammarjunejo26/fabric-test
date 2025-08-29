package com.example.geektrust.service.impl;

import com.example.geektrust.domain.Fund;
import com.example.geektrust.domain.FundsPortfolio;
import com.example.geektrust.repository.FundsPortfolioRepository;
import com.example.geektrust.service.FundsPortfolioService;
import com.example.geektrust.service.LogService;

import java.util.List;
import java.util.Optional;

public class FundsPortfolioServiceImpl implements FundsPortfolioService {

    private final FundsPortfolio fundsPortfolio;
    private final LogService logService;

    public FundsPortfolioServiceImpl(FundsPortfolioRepository repository, LogService logService) {
        fundsPortfolio = repository.getFundsPortfolio();
        this.logService = logService;
    }

    public void addStockToFundsPortfolio(String fundName, String stockName) {
        if (!isFundExists(fundName)) {
            logService.logFundNotFound();
            return;
        }
        fundsPortfolio.addStockToAFund(fundName, stockName);
    }

    public boolean isFundExists(String fundName) {
        return fundsPortfolio.isFundExists(fundName);
    }

    public Optional<Fund> findFundByName(String fundName) {
        return fundsPortfolio.findFundByName(fundName);
    }

    public List<Fund> getAllFunds() {
        return fundsPortfolio.getFunds();
    }
}
