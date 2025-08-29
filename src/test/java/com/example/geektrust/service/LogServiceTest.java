package com.example.geektrust.service;

import com.example.geektrust.service.impl.LogServiceImpl;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static com.example.geektrust.util.TestData.*;
import static org.junit.jupiter.api.Assertions.*;

class LogServiceTest {

    private LogServiceImpl logService;

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    @BeforeEach
    void setUp() {
        logService = LogServiceImpl.getInstance();
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    @Test
    void testSingletonInstance() {
        LogServiceImpl instance1 = LogServiceImpl.getInstance();
        LogServiceImpl instance2 = LogServiceImpl.getInstance();
        assertSame(instance1, instance2);
    }

    @Test
    void testLogOverLapPrintsCorrectly() {
        logService.logOverLap(TEST_FUND_1, TEST_FUND_2, 12.3456);
        String expected = TEST_FUND_1 + ARG_SEPARATOR + TEST_FUND_2 + ARG_SEPARATOR + "12.35%\n";
        assertEquals(expected, outContent.toString());
    }

    @Test
    void testLogFundNotFoundPrintsCorrectMessage() {
        logService.logFundNotFound();
        assertEquals(LogServiceImpl.FUND_NOT_FOUND_MESSAGE + "\n", outContent.toString());
    }

    @Test
    void testLogErrorPrintsToErr() {
        logService.logError("Some error");
        assertEquals("Some error\n", errContent.toString());
    }
}
