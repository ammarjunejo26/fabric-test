package com.example.geektrust.service;

import com.example.geektrust.service.impl.OverlapCalculatorServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class OverlapCalculatorServiceTest {

    private final OverlapCalculatorService service = new OverlapCalculatorServiceImpl();

    @Test
    void testSuccessCalculateOverlapWithOneCommonStock() {
        Set<String> inputFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2));
        Set<String> userFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_2, TEST_STOCK_3));

        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(50.0, result);
    }

    @Test
    void testSuccessCalculateOverlapWithTwoCommonStock() {
        Set<String> inputFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2, TEST_STOCK_3));
        Set<String> userFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_2, TEST_STOCK_3));

        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(80.00, result);
    }

    @Test
    void testSuccessCalculateOverlapWithAllStocksCommon() {
        Set<String> inputFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2));
        Set<String> userFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2));

        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(100.0, result);
    }

    @Test
    void testCalculateOverlapWithNullInputFundStocks() {
        Set<String> inputFundStocks = null;
        Set<String> userFundStocks = new HashSet<>(Collections.singletonList(TEST_STOCK_1));
        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateOverlapWithNullUserFundStocks() {
        Set<String> inputFundStocks = new HashSet<>(Collections.singletonList(TEST_STOCK_1));
        Set<String> userFundStocks = null;
        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateOverlapWithEmptyInputFundStocks() {
        Set<String> inputFundStocks = Collections.emptySet();
        Set<String> userFundStocks = new HashSet<>(Collections.singletonList(TEST_STOCK_1));
        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateOverlapWithEmptyUserFundStocks() {
        Set<String> inputFundStocks = new HashSet<>(Collections.singletonList(TEST_STOCK_1));
        Set<String> userFundStocks = Collections.emptySet();
        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(0.0, result);
    }

    @Test
    void testCalculateOverlapWithNoCommonStocks() {
        Set<String> inputFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_1, TEST_STOCK_2));
        Set<String> userFundStocks = new HashSet<>(Arrays.asList(TEST_STOCK_3, TEST_STOCK_4));
        double result = service.calculateOverlap(inputFundStocks, userFundStocks);
        assertEquals(0.0, result);
    }
}
