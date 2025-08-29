package com.example.geektrust.service;

import com.example.geektrust.domain.UserPortfolio;
import com.example.geektrust.exception.InvalidCommandException;
import com.example.geektrust.service.impl.CommandExecutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static com.example.geektrust.enums.InputCommand.*;
import static com.example.geektrust.util.TestData.*;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandExecutorServiceTest {

    private CommandExecutorService commandExecutorService;

    @Mock
    private LogService logService;
    @Mock
    private FundsPortfolioService fundsPortfolioService;
    @Mock
    private UserPortfolioService userPortfolioService;
    @Mock
    private UserPortfolio userPortfolio;


    @BeforeEach
    void setUp() {
        when(userPortfolioService.getUserPortfolioByUserName(CommandExecutorService.USER_NAME)).thenReturn(userPortfolio);
        commandExecutorService = new CommandExecutorService(logService, fundsPortfolioService, userPortfolioService);
    }

    @Test
    void testExecuteShouldHandleCurrentPortfolioCommand() throws InvalidCommandException {
        String inputCommandLine = CURRENT_PORTFOLIO.name() + ARG_SEPARATOR + TEST_FUND_1 + ARG_SEPARATOR + TEST_FUND_2;
        commandExecutorService.execute(inputCommandLine);

        verify(userPortfolioService).addFundsToUserCurrentPortfolio(eq(asList(TEST_FUND_1, TEST_FUND_2)), eq(userPortfolio));
    }

    @Test
    void testExecuteShouldLogErrorWhenCurrentPortfolioCommandHasNoFunds() throws InvalidCommandException {
        String inputCommandLine = CURRENT_PORTFOLIO.name();
        commandExecutorService.execute(inputCommandLine);

        verify(logService).logError(contains("Funds names missing in input command: " + inputCommandLine));
        verifyNoInteractions(fundsPortfolioService);
        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verify(userPortfolioService, times(0)).addFundsToUserCurrentPortfolio(Collections.emptyList(), userPortfolio);
    }

    @Test
    void testExecuteShouldHandleCalculateOverlapCommand() throws InvalidCommandException {
        String inputCommandLine = CALCULATE_OVERLAP.name() + ARG_SEPARATOR + TEST_FUND_1;
        commandExecutorService.execute(inputCommandLine);

        verify(userPortfolioService).calculateOverlapForUser(TEST_FUND_1, userPortfolio);
        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
    }

    @Test
    void testExecuteShouldLogErrorWhenCalculateOverlapHasNoFund() throws InvalidCommandException {
        String inputCommandLine = CALCULATE_OVERLAP.name();
        commandExecutorService.execute(inputCommandLine);

        verify(logService).logError(contains("Invalid parameters for: " + inputCommandLine));
        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verify(userPortfolioService, times(0)).calculateOverlapForUser(anyString(), any());
        verifyNoInteractions(fundsPortfolioService);
    }

    @Test
    void testExecuteShouldHandleAddStockCommand() throws InvalidCommandException {
        String inputCommandLine = ADD_STOCK.name() + ARG_SEPARATOR + TEST_FUND_1 + ARG_SEPARATOR + TEST_STOCK_1;
        commandExecutorService.execute(inputCommandLine);

        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verify(fundsPortfolioService).addStockToFundsPortfolio(TEST_FUND_1, TEST_STOCK_1);
        verify(logService, times(0)).logFundNotFound();
    }

    @Test
    void testExecuteShouldHandleAddStockCommandWithMultiWordStock() throws InvalidCommandException {
        String inputCommandLine = ADD_STOCK.name() + ARG_SEPARATOR + TEST_FUND_1 + ARG_SEPARATOR + TEST_STOCK_2;
        commandExecutorService.execute(inputCommandLine);

        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verify(fundsPortfolioService).addStockToFundsPortfolio(TEST_FUND_1, TEST_STOCK_2);
    }

    @Test
    void testExecuteShouldLogErrorWhenAddStockCommandHasMissingStock() throws InvalidCommandException {
        String inputCommandLine = ADD_STOCK.name() + ARG_SEPARATOR + TEST_FUND_1;
        commandExecutorService.execute(inputCommandLine);

        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verify(logService).logError(contains("Invalid parameters for: " + inputCommandLine));
        verifyNoInteractions(fundsPortfolioService);
    }

    @Test
    void testExecuteShouldThrowExceptionForUnknownCommand() {
        assertThrows(InvalidCommandException.class, () -> commandExecutorService.execute("INVALID_COMMAND FUND_1"));

        verify(userPortfolioService, times(1)).getUserPortfolioByUserName(anyString());
        verifyNoInteractions(logService);
    }
}
