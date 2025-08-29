package com.example.geektrust.service.impl;

import com.example.geektrust.service.OverlapCalculatorService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;

public class OverlapCalculatorServiceImpl implements OverlapCalculatorService {

    private static final double FUND_COUNT = 2;

    public double calculateOverlap(Set<String> inputFundStocks, Set<String> userFundStocks) {
        if (inputFundStocks == null || userFundStocks == null || inputFundStocks.isEmpty() || userFundStocks.isEmpty()) return 0.0;

        Set<String> commonStocks = new HashSet<>(inputFundStocks);
        commonStocks.retainAll(userFundStocks);

        int commonStocksCount = commonStocks.size();
        int totalStocksCount = inputFundStocks.size() + userFundStocks.size();

        return roundTo2Decimals((FUND_COUNT * commonStocksCount / totalStocksCount) * 100.0);
    }

    private double roundTo2Decimals(double value) {
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
