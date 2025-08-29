package com.example.geektrust.service;

public interface LogService {

    void logOverLap(String fundName, String existingUserFund, double overlapPercentage);

    void logFundNotFound();

    void logError(String errorMessage);
}
