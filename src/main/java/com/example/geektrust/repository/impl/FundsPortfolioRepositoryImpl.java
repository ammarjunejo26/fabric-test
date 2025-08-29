package com.example.geektrust.repository.impl;

import com.example.geektrust.domain.FundsPortfolio;
import com.example.geektrust.repository.FundsPortfolioRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class FundsPortfolioRepositoryImpl implements FundsPortfolioRepository {

    private final FundsPortfolio fundsPortfolio;

    public FundsPortfolioRepositoryImpl(String dataSource) {
        fundsPortfolio = this.loadFundsAndStocksData(dataSource);
    }

    private FundsPortfolio loadFundsAndStocksData(String dataSource) {
        try (InputStream inputStream = FundsPortfolioRepositoryImpl.class.getClassLoader().getResourceAsStream(dataSource)) {
            if (Objects.isNull(inputStream)) {
                throw new FileNotFoundException(dataSource + " not found in resources.");
            }
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(inputStream, FundsPortfolio.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading stock data: " + e.getMessage(), e);
        }
    }

    public FundsPortfolio getFundsPortfolio() {
        return fundsPortfolio;
    }
}
