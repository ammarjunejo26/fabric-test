package com.example.geektrust.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

public class FundTest {

    private Fund fund;

    @BeforeEach
    void setUp() {
        Set<String> initialStocks = new HashSet<>();
        initialStocks.add(TEST_STOCK_1);
        initialStocks.add(TEST_STOCK_2);
        fund = new Fund(TEST_FUND_1, initialStocks);
    }

    @Test
    void fundNameShouldBeSetCorrectly() {
        assertEquals(TEST_FUND_1, fund.getName());
    }

    @Test
    void getStocksShouldReturnAllStocks() {
        Set<String> stocks = fund.getStocks();
        assertEquals(2, stocks.size());
        assertTrue(stocks.contains(TEST_STOCK_1));
        assertTrue(stocks.contains(TEST_STOCK_2));
    }

    @Test
    void addStockShouldAddNewStock() {
        fund.addStock(TEST_STOCK_3);
        assertEquals(3, fund.getStocks().size());
        assertTrue(fund.getStocks().contains(TEST_STOCK_3));
    }

    @Test
    void addStockShouldNotAllowDuplicates() {
        fund.addStock(TEST_STOCK_1);
        assertEquals(2, fund.getStocks().size());
    }

    @Test
    void constructorWithoutStocksShouldInitializeEmptySet() {
        Fund emptyFund = new Fund(TEST_FUND_UNKNOWN, new HashSet<>());
        assertNotNull(emptyFund.getStocks());
        assertTrue(emptyFund.getStocks().isEmpty());
    }
}
