package com.example.geektrust;

import com.example.geektrust.service.impl.CommandExecutorService;
import com.example.geektrust.service.impl.LogServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyFundAndStocksTest {

    @Mock
    private LogServiceImpl mockLogService = LogServiceImpl.getInstance();

    @Mock
    CommandExecutorService commandExecutorService;

    private static final String TEST_INPUT_FILE = "test-input1.txt";
    private static final String TEST_INPUT_EMPTY_LINES_FILE = "test-input1-empty-lines.txt";

    @Test
    void testMainShouldCallExecuteForEachLine() throws Exception {
        Path resourceFile = Paths.get(getClass().getClassLoader().getResource(TEST_INPUT_FILE).toURI());
        MyFundAndStocks.runLines(resourceFile.toString(), commandExecutorService, mockLogService);
        verify(commandExecutorService, times(6)).execute(anyString());
    }

    @Test
    void testMainShouldSkipEmptyLines() throws Exception {
        Path resourceFile = Paths.get(getClass().getClassLoader().getResource(TEST_INPUT_EMPTY_LINES_FILE).toURI());
        MyFundAndStocks.runLines(resourceFile.toString(), commandExecutorService, mockLogService);
        verify(commandExecutorService, times(2)).execute(anyString());
        assertDoesNotThrow(() -> MyFundAndStocks.main(new String[]{resourceFile.toString()}));
    }

    @Test
    void testMainShouldLogErrorWhenNoArgsProvided() {
        try (MockedStatic<LogServiceImpl> mockedLogServiceImpl = Mockito.mockStatic(LogServiceImpl.class)) {
            mockedLogServiceImpl.when(LogServiceImpl::getInstance).thenReturn(mockLogService);
            MyFundAndStocks.main(new String[]{});
            verify(mockLogService).logError(contains("Missing input_file path argument"));
        }
    }

    @Test
    void testNoErrorReadingFileFromResources() throws URISyntaxException {
        Path resourceFile = Paths.get(getClass().getClassLoader().getResource(TEST_INPUT_FILE).toURI());
        try (MockedStatic<LogServiceImpl> mockedLogServiceImpl = Mockito.mockStatic(LogServiceImpl.class)) {
            mockedLogServiceImpl.when(LogServiceImpl::getInstance).thenReturn(mockLogService);
            assertDoesNotThrow(() -> MyFundAndStocks.main(new String[]{resourceFile.toString()}));
            verify(mockLogService, never()).logError(contains("Missing input_file path argument"));
        }
    }

    @Test
    void testMainShouldLogErrorWhenFileNotFound() throws URISyntaxException {
        Path resourceFile = Paths.get(getClass().getClassLoader().getResource(TEST_INPUT_FILE).toURI());
        try (MockedStatic<LogServiceImpl> mockedLogServiceImpl = Mockito.mockStatic(LogServiceImpl.class)) {
            mockedLogServiceImpl.when(LogServiceImpl::getInstance).thenReturn(mockLogService);
            String invalidPath = "abc";
            MyFundAndStocks.main(new String[]{invalidPath + resourceFile.toString()});
            verify(mockLogService).logError(contains("Error while reading input commands."));
        }
    }
}