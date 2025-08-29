package com.example.geektrust.service;

import com.example.geektrust.domain.Fund;
import com.example.geektrust.domain.FundsPortfolio;
import com.example.geektrust.repository.FundsPortfolioRepository;
import com.example.geektrust.service.impl.FundsPortfolioServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FundsPortfolioServiceTest {

    @Mock
    private FundsPortfolioRepository repository;

    @Mock
    private LogService logService;

    @Mock
    private FundsPortfolio fundsPortfolio;

    private FundsPortfolioService fundsPortfolioService;

    @BeforeEach
    void setUp() {
        when(repository.getFundsPortfolio()).thenReturn(fundsPortfolio);
        fundsPortfolioService = new FundsPortfolioServiceImpl(repository, logService);
    }

    @Test
    void testAddStockToFundsPortfolioShouldLogFundNotFound() {
        when(fundsPortfolio.isFundExists(anyString())).thenReturn(false);

        fundsPortfolioService.addStockToFundsPortfolio(TEST_FUND_1, TEST_STOCK_1);

        verify(logService, times(1)).logFundNotFound();
        verify(fundsPortfolio, times(0)).addStockToAFund(TEST_FUND_1, TEST_STOCK_1);
    }

    @Test
    void testAddStockToFundsPortfolioShouldDelegateToFundsPortfolio() {
        when(fundsPortfolio.isFundExists(anyString())).thenReturn(true);
        fundsPortfolioService.addStockToFundsPortfolio(TEST_FUND_1, TEST_STOCK_1);
        verify(fundsPortfolio, times(1)).addStockToAFund(TEST_FUND_1, TEST_STOCK_1);
        verify(logService, times(0)).logFundNotFound();
    }

    @Test
    void testIsFundExistsShouldReturnTrueWhenFundExists() {
        when(fundsPortfolio.isFundExists(TEST_FUND_1)).thenReturn(true);
        assertTrue(fundsPortfolioService.isFundExists(TEST_FUND_1));
    }

    @Test
    void testIsFundExistsShouldReturnFalseWhenFundDoesNotExist() {
        when(fundsPortfolio.isFundExists(TEST_FUND_UNKNOWN)).thenReturn(false);
        assertFalse(fundsPortfolioService.isFundExists(TEST_FUND_UNKNOWN));
    }

    @Test
    void testFindFundByNameShouldReturnFundWhenPresent() {
        Fund fund = new Fund(TEST_FUND_1, Collections.emptySet());
        when(fundsPortfolio.findFundByName(TEST_FUND_1)).thenReturn(Optional.of(fund));

        Optional<Fund> result = fundsPortfolioService.findFundByName(TEST_FUND_1);

        assertTrue(result.isPresent());
        assertEquals(TEST_FUND_1, result.get().getName());
    }

    @Test
    void findFundByNameShouldReturnEmptyWhenFundNotPresent() {
        when(fundsPortfolio.findFundByName(TEST_FUND_UNKNOWN)).thenReturn(Optional.empty());

        Optional<Fund> result = fundsPortfolioService.findFundByName(TEST_FUND_UNKNOWN);

        assertFalse(result.isPresent());
    }

    @Test
    void getAllFundsShouldReturnListOfFunds() {
        Fund fund1 = new Fund(TEST_FUND_1, Collections.emptySet());
        Fund fund2 = new Fund(TEST_FUND_2, Collections.emptySet());
        List<Fund> funds = Arrays.asList(fund1, fund2);

        when(fundsPortfolio.getFunds()).thenReturn(funds);

        List<Fund> result = fundsPortfolioService.getAllFunds();

        assertEquals(2, result.size());
        assertEquals(TEST_FUND_1, result.get(0).getName());
        assertEquals(TEST_FUND_2, result.get(1).getName());
    }
}
