package com.example.geektrust;

import com.example.geektrust.exception.InvalidCommandException;
import com.example.geektrust.repository.FundsPortfolioRepository;
import com.example.geektrust.repository.impl.FundsPortfolioRepositoryImpl;
import com.example.geektrust.service.*;
import com.example.geektrust.service.impl.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class MyFundAndStocks {

    private static final String STOCK_JSON_DATA_FILE_NAME = "stock_data.json";

    public static void main(String[] args) {
        LogService logService = LogServiceImpl.getInstance();

        FundsPortfolioRepository fundsPortfolioRepository = new FundsPortfolioRepositoryImpl(STOCK_JSON_DATA_FILE_NAME);
        FundsPortfolioService fundsPortfolioService = new FundsPortfolioServiceImpl(fundsPortfolioRepository, logService);
        OverlapCalculatorService overlapCalculatorService = new OverlapCalculatorServiceImpl();
        UserPortfolioService userPortfolioService = new UserPortfolioServiceImpl(fundsPortfolioService, overlapCalculatorService, logService);

        if (args.length == 0) {
            logService.logError("Missing input_file path argument");
            return;
        }

        CommandExecutorService commandExecutorService = new CommandExecutorService(logService, fundsPortfolioService, userPortfolioService);
        String inputFilePath = args[0];
        runLines(inputFilePath, commandExecutorService, logService);
    }

    protected static void runLines(String inputFilePath, CommandExecutorService commandExecutorService, LogService logService) {
        try (BufferedReader br = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    commandExecutorService.execute(line);
                }
            }
        } catch (IOException | InvalidCommandException e) {
            logService.logError("Error while reading input commands. " + e.getMessage());
        }
    }
}
