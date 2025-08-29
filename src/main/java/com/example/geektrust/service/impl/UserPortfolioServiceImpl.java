package com.example.geektrust.service.impl;

import com.example.geektrust.domain.Fund;
import com.example.geektrust.domain.UserPortfolio;
import com.example.geektrust.service.FundsPortfolioService;
import com.example.geektrust.service.LogService;
import com.example.geektrust.service.OverlapCalculatorService;
import com.example.geektrust.service.UserPortfolioService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserPortfolioServiceImpl implements UserPortfolioService {

    private final FundsPortfolioService fundsPortfolioService;
    private final OverlapCalculatorService overlapCalculatorService;

    private final LogService logService;
    private final Map<String, UserPortfolio> userPortfolios = new HashMap<>();

    public UserPortfolioServiceImpl(FundsPortfolioService fundsPortfolioService,
                                    OverlapCalculatorService overlapCalculatorService,
                                    LogService logService) {
        this.fundsPortfolioService = fundsPortfolioService;
        this.overlapCalculatorService = overlapCalculatorService;
        this.logService = logService;
    }

    public UserPortfolio getUserPortfolioByUserName(String userName) {
        return userPortfolios.computeIfAbsent(userName, UserPortfolio::new);
    }

    public void addFundsToUserCurrentPortfolio(List<String> fundNamesToAdd, UserPortfolio userPortfolio) {
        fundNamesToAdd.stream()
                .filter(fundsPortfolioService::isFundExists)
                .forEach(userPortfolio::addFund);
    }

    public void calculateOverlapForUser(String inputFundName, UserPortfolio userPortfolio) {
        Optional<Fund> inputFundOptional = fundsPortfolioService.findFundByName(inputFundName);
        if (!inputFundOptional.isPresent()) {
            logService.logFundNotFound();
            return;
        }

        Fund inputFund = inputFundOptional.get();
        userPortfolio.getFunds().stream()
                .map(fundsPortfolioService::findFundByName)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(userFund -> {
                    double overlapPercentage = overlapCalculatorService.calculateOverlap(inputFund.getStocks(), userFund.getStocks());
                    if (overlapPercentage > 0) {
                        logService.logOverLap(inputFund.getName(), userFund.getName(), overlapPercentage);
                    }
                });
    }
}
