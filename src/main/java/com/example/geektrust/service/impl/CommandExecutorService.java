package com.example.geektrust.service.impl;

import com.example.geektrust.enums.InputCommand;
import com.example.geektrust.domain.UserPortfolio;
import com.example.geektrust.exception.InvalidCommandException;
import com.example.geektrust.service.FundsPortfolioService;
import com.example.geektrust.service.LogService;
import com.example.geektrust.service.UserPortfolioService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutorService {

    public static final String ARG_SEPARATOR = " ";
    public static final String USER_NAME = "userName";

    private final LogService logService;
    private final FundsPortfolioService fundsPortfolioService;
    private final UserPortfolioService userPortfolioService;
    private final UserPortfolio userPortfolio;

    public CommandExecutorService(LogService logService,
                                  FundsPortfolioService fundsPortfolioService,
                                  UserPortfolioService userPortfolioService) {
        this.logService = logService;
        this.fundsPortfolioService = fundsPortfolioService;
        this.userPortfolioService = userPortfolioService;
        this.userPortfolio = userPortfolioService.getUserPortfolioByUserName(USER_NAME);
    }

    public void execute(String line) throws InvalidCommandException {
        String[] parts = line.split(ARG_SEPARATOR);

        InputCommand command;
        try {
            command = InputCommand.valueOf(parts[0]);
        } catch (IllegalArgumentException e) {
            throw new InvalidCommandException("Invalid command input");
        }

        switch (command) {
            case CURRENT_PORTFOLIO:
                handleCurrentPortfolioCommand(parts, line);
                break;
            case CALCULATE_OVERLAP:
                handleCalculateOverlapCommand(parts, line);
                break;
            case ADD_STOCK:
                handleAddStockCommand(parts, line);
                break;
        }
    }

    private void handleCurrentPortfolioCommand(String[] parts, String line) {
        if (parts.length < 2) {
            logService.logError("Funds names missing in input command: " + line);
            return;
        }
        List<String> fundNamesToAdd = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
        userPortfolioService.addFundsToUserCurrentPortfolio(fundNamesToAdd, userPortfolio);
    }

    private void handleCalculateOverlapCommand(String[] parts, String line) {
        if (parts.length < 2) {
            logService.logError("Invalid parameters for: " + line);
            return;
        }
        String inputFundName = parts[1];
        userPortfolioService.calculateOverlapForUser(inputFundName, userPortfolio);
    }

    private void handleAddStockCommand(String[] parts, String line) {
        if (parts.length < 3) {
            logService.logError("Invalid parameters for: " + line);
            return;
        }
        String fundName = parts[1];
        String stockName = String.join(ARG_SEPARATOR, Arrays.copyOfRange(parts, 2, parts.length));
        fundsPortfolioService.addStockToFundsPortfolio(fundName, stockName);
    }
}

