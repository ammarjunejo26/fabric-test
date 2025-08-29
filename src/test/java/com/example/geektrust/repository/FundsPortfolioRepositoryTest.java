package com.example.geektrust.repository;

import com.example.geektrust.repository.impl.FundsPortfolioRepositoryImpl;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class FundsPortfolioRepositoryTest {
    private static final String STOCK_JSON_DATA_FILE_NAME = "stock_data.json";

    @Test
    public void shouldLoadStockJsonFromResources() {
        FundsPortfolioRepository repo = new FundsPortfolioRepositoryImpl(STOCK_JSON_DATA_FILE_NAME);

        String expectedFundName = "ICICI_PRU_NIFTY_NEXT_50_INDEX";
        assertNotNull(repo.getFundsPortfolio());
        assertEquals(2, repo.getFundsPortfolio().getFunds().size());
        assertEquals(expectedFundName, repo.getFundsPortfolio().getFunds().get(0).getName());

        Set<String> stocks = repo.getFundsPortfolio().getFunds().get(0).getStocks();

        String expectedStockName1 = "INDRAPRASTHA GAS LIMITED";
        String expectedStockName2 = "BIOCON LIMITED";
        assertTrue(stocks.contains(expectedStockName1));
        assertTrue(stocks.contains(expectedStockName2));
        assertEquals(2, stocks.size());
    }

    @Test
    public void constructorShouldThrowIfJsonNotFound() {
        String invalidDataSourceName = "invalid_name.json";
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> new FundsPortfolioRepositoryImpl(invalidDataSourceName)
        );
        assertEquals(ex.getMessage(), "Error reading stock data: " + invalidDataSourceName + " not found in resources.");
    }
}
