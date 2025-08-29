package com.example.geektrust.service;

import com.example.geektrust.domain.Fund;
import com.example.geektrust.domain.UserPortfolio;
import com.example.geektrust.service.impl.UserPortfolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPortfolioServiceTest {

    private UserPortfolioService userPortfolioService;

    @Mock
    private FundsPortfolioService fundsPortfolioService;
    @Mock
    private OverlapCalculatorService overlapCalculatorService;
    @Mock
    private LogService logService;

    @BeforeEach
    void setUp() {
        userPortfolioService = new UserPortfolioServiceImpl(
                fundsPortfolioService,
                overlapCalculatorService,
                logService
        );
    }

    @Test
    void testGetUserPortfolioByUserName() {
        UserPortfolio userPortfolio = userPortfolioService.getUserPortfolioByUserName(TEST_USER);
        assertNotNull(userPortfolio);
        assertEquals(TEST_USER, userPortfolio.getUserName());
    }

    @Test
    void testReusesUserPortfolioIfPresent() {
        UserPortfolio userPortfolio = userPortfolioService.getUserPortfolioByUserName(TEST_USER);
        UserPortfolio userPortfolio2 = userPortfolioService.getUserPortfolioByUserName(TEST_USER);
        assertSame(userPortfolio, userPortfolio);
    }

    @Test
    void testAddFundsToUserCurrentPortfolio() {
        when(fundsPortfolioService.isFundExists(TEST_FUND_1)).thenReturn(true);

        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);
        userPortfolioService.addFundsToUserCurrentPortfolio(Collections.singletonList(TEST_FUND_1), userPortfolio);

        assertTrue(userPortfolio.getFunds().contains(TEST_FUND_1));
    }

    @Test
    void testAddExistingFundsToUserCurrentPortfolio() {
        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);

        when(fundsPortfolioService.isFundExists(TEST_FUND_1)).thenReturn(true);
        when(fundsPortfolioService.isFundExists(TEST_FUND_2)).thenReturn(false);

        userPortfolioService.addFundsToUserCurrentPortfolio(Arrays.asList(TEST_FUND_1, TEST_FUND_2), userPortfolio);

        assertTrue(userPortfolio.getFunds().contains(TEST_FUND_1));
        assertFalse(userPortfolio.getFunds().contains(TEST_FUND_2));
    }

    @Test
    void testCalculateOverlapForUserFundNotFoundLogsMessage() {
        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);

        when(fundsPortfolioService.findFundByName(TEST_FUND_UNKNOWN)).thenReturn(Optional.empty());

        userPortfolioService.calculateOverlapForUser(TEST_FUND_UNKNOWN, userPortfolio);

        verify(logService).logFundNotFound();
        verifyNoMoreInteractions(logService);
    }

    @Test
    void testCalculateOverlapForUserWithOverlapPercentageLogs() {
        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);
        userPortfolio.addFund(TEST_FUND_2);

        Fund inputFund = new Fund(TEST_FUND_1, new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2)));
        Fund userFund = new Fund(TEST_FUND_2, new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_3)));

        when(fundsPortfolioService.findFundByName(TEST_FUND_1)).thenReturn(Optional.of(inputFund));
        when(fundsPortfolioService.findFundByName(TEST_FUND_2)).thenReturn(Optional.of(userFund));
        when(overlapCalculatorService.calculateOverlap(inputFund.getStocks(), userFund.getStocks())).thenReturn(50.0);

        userPortfolioService.calculateOverlapForUser(TEST_FUND_1, userPortfolio);

        verify(logService).logOverLap(TEST_FUND_1, TEST_FUND_2, 50.0);
    }

    @Test
    void testCalculateOverlapForUserWithNoOverlapPercentageLog() {
        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);
        userPortfolio.addFund(TEST_FUND_2);

        Fund inputFund = new Fund(TEST_FUND_1, new HashSet<>(Collections.singletonList(TEST_STOCK_1)));
        Fund userFund = new Fund(TEST_FUND_2, new HashSet<>(Collections.singletonList(TEST_STOCK_3)));

        when(fundsPortfolioService.findFundByName(TEST_FUND_1)).thenReturn(Optional.of(inputFund));
        when(fundsPortfolioService.findFundByName(TEST_FUND_2)).thenReturn(Optional.of(userFund));
        when(overlapCalculatorService.calculateOverlap(anySet(), anySet())).thenReturn(0.0);

        userPortfolioService.calculateOverlapForUser(TEST_FUND_1, userPortfolio);

        verify(logService, never()).logOverLap(anyString(), anyString(), anyDouble());
    }

    @Test
    void testCalculateOverlapForUserIgnoresFundsThatDoNotExistInRepo() {
        UserPortfolio userPortfolio = new UserPortfolio(TEST_USER);
        userPortfolio.addFund(TEST_FUND_UNKNOWN);

        Fund inputFund = new Fund(TEST_FUND_1, new HashSet<>(Collections.singletonList(TEST_STOCK_2)));

        when(fundsPortfolioService.findFundByName(TEST_FUND_1)).thenReturn(Optional.of(inputFund));
        when(fundsPortfolioService.findFundByName(TEST_FUND_UNKNOWN)).thenReturn(Optional.empty());

        userPortfolioService.calculateOverlapForUser(TEST_FUND_1, userPortfolio);

        verifyNoInteractions(overlapCalculatorService);
        verifyNoMoreInteractions(logService);
    }
}
