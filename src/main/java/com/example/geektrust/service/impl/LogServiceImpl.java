package com.example.geektrust.service.impl;

import com.example.geektrust.service.LogService;

public class LogServiceImpl implements LogService {

    public static final String FUND_NOT_FOUND_MESSAGE = "FUND_NOT_FOUND";
    private static final LogServiceImpl SINGLETON_INSTANCE = new LogServiceImpl();

    public static LogServiceImpl getInstance() {
        if (SINGLETON_INSTANCE != null) {
            return SINGLETON_INSTANCE;
        }
        return new LogServiceImpl();
    }

    private LogServiceImpl() {
    }

    public void logOverLap(String fundName, String existingUserFund, double overlapPercentage) {
        System.out.printf("%s %s %.2f%%%n", fundName, existingUserFund, overlapPercentage);
    }

    public void logFundNotFound() {
        System.out.println(FUND_NOT_FOUND_MESSAGE);
    }

    public void logError(String errorMessage) {
        System.err.println(errorMessage);
    }
}
