package com.example.geektrust.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class UserPortfolioTest {

    private UserPortfolio userPortfolio;

    @BeforeEach
    void setUp() {
        userPortfolio = new UserPortfolio(TEST_USER);
    }

    @Test
    void userNameShouldBeSetCorrectly() {
        assertEquals(TEST_USER, userPortfolio.getUserName());
    }

    @Test
    void addFundShouldAddFundToPortfolio() {
        userPortfolio.addFund(TEST_FUND_1);
        assertTrue(userPortfolio.getFunds().contains(TEST_FUND_1));
    }

    @Test
    void addFundShouldNotAllowDuplicates() {
        userPortfolio.addFund(TEST_FUND_2);
        userPortfolio.addFund(TEST_FUND_2); // duplicate
        assertEquals(1, userPortfolio.getFunds().size());
    }

    @Test
    void getFundsShouldReturnAllAddedFundsInOrder() {
        userPortfolio.addFund(TEST_FUND_1);
        userPortfolio.addFund(TEST_FUND_2);
        Set<String> funds = userPortfolio.getFunds();
        assertArrayEquals(new String[]{TEST_FUND_1, TEST_FUND_2}, funds.toArray());
    }

    @Test
    void getFundsShouldReturnEmptySetInitially() {
        assertTrue(userPortfolio.getFunds().isEmpty());
    }
}
