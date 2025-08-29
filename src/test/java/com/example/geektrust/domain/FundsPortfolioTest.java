package com.example.geektrust.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static com.example.geektrust.util.TestData.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class FundsPortfolioTest {

    private FundsPortfolio fundsPortfolio;

    @BeforeEach
    void setUp() {
        fundsPortfolio = new FundsPortfolio();

        Fund fund1 = new Fund(TEST_FUND_1, new HashSet<>(asList(TEST_STOCK_1, TEST_STOCK_2)));
        Fund fund2 = new Fund(TEST_FUND_2, new HashSet<>(asList(TEST_STOCK_3, TEST_STOCK_4)));
        List<Fund> funds = asList(fund1, fund2);
        fundsPortfolio.setFunds(funds);
    }

    @Test
    void testAddStockToAFundShouldAddStockIfFundExists() {
        fundsPortfolio.addStockToAFund(TEST_FUND_1, TEST_STOCK_5);
        Optional<Fund> fund = fundsPortfolio.findFundByName(TEST_FUND_1);
        assertTrue(fund.isPresent());
        assertTrue(fund.get().getStocks().contains(TEST_STOCK_5));
    }

    @Test
    void testIsFundExistsShouldReturnTrueIfFundExists() {
        assertTrue(fundsPortfolio.isFundExists(TEST_FUND_1));
    }

    @Test
    void testIsFundExistsShouldReturnFalseIfFundDoesNotExist() {
        assertFalse(fundsPortfolio.isFundExists(TEST_FUND_UNKNOWN));
    }

    @Test
    void testFindFundByNameShouldReturnOptionalOfFundIfExists() {
        Optional<Fund> fund = fundsPortfolio.findFundByName(TEST_FUND_1);
        assertTrue(fund.isPresent());
        assertEquals(TEST_FUND_1, fund.get().getName());
    }

    @Test
    void testFindFundByNameShouldReturnEmptyOptionalIfFundDoesNotExist() {
        Optional<Fund> fund = fundsPortfolio.findFundByName(TEST_FUND_UNKNOWN);
        assertFalse(fund.isPresent());
    }

    @Test
    void testGetFundsShouldReturnAllFunds() {
        List<Fund> allFunds = fundsPortfolio.getFunds();
        assertEquals(2, allFunds.size());
    }
}
