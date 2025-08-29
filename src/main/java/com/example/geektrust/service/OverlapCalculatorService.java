package com.example.geektrust.service;

import java.util.Set;

public interface OverlapCalculatorService {

    double calculateOverlap(Set<String> inputFundStocks, Set<String> userFundStocks);
}
