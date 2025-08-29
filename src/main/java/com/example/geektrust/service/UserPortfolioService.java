package com.example.geektrust.service;

import com.example.geektrust.domain.UserPortfolio;

import java.util.List;

public interface UserPortfolioService {

    UserPortfolio getUserPortfolioByUserName(String userName);

    void addFundsToUserCurrentPortfolio(List<String> fundNamesToAdd, UserPortfolio userPortfolio);

    void calculateOverlapForUser(String inputFundName, UserPortfolio userPortfolio);
}
