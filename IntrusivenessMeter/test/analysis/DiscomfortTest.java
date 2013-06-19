package analysis;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.Execution;

public class DiscomfortTest {

	private static final String DISCOMFORT_LOG_FILE_NAME = "discomfort_test.log";

	private static final String BLANK_LINE = "\n";
	private static final String ERROR_LINE = "error\n";
	
	private static long time1 = 1000;
	private static long time2 = time1 + 1;
	private static long time3 = time2 + 1;
	private static long time4 = time3 + 1;
	private static long time5 = time4 + 1;
	private static long time6 = time5 + 1;
	private static long time7 = time6 + 1;
	private static long time8 = time7 + 1;
	private static long time9 = time8 + 1;
	private static long time10 = time9 + 1;
	private static long time11 = time10 + 1;
	private static long time12 = time11 + 1;
	private static long time13 = time12 + 1;
	private static long time14 = time13 + 1;
	private static long time15 = time14 + 1;
	private static long time16 = time15 + 1;
	private static long time17 = time16 + 1;
	private static long time18 = time17 + 1;
	private static long time19 = time18 + 1;
	private static long time20 = time19 + 1;
	
	private Discomfort discomfort;
	
	@Before
	public void setUp() throws Exception {
		File discomfortLogFile = new File(DISCOMFORT_LOG_FILE_NAME);
		discomfortLogFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(DISCOMFORT_LOG_FILE_NAME).delete();
	}
	
	@Test
	public void testBasicDiscomfortFile() throws IOException {
		writeBasicDiscomfortFile();
		
		discomfort = new Discomfort(DISCOMFORT_LOG_FILE_NAME);
		
		assertTrue(discomfort.reportedDiscomfort(new Execution(time1, time5)));
		assertTrue(discomfort.reportedDiscomfort(new Execution(time15, time15)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time8, time11)));
		assertTrue(discomfort.reportedDiscomfort(new Execution(time1, time20)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time16, time19)));
	}
	
	@Test
	public void testNoDiscomfortFile() throws IOException {
		writeNoDiscomfortFile();
		
		discomfort = new Discomfort(DISCOMFORT_LOG_FILE_NAME);
		
		assertFalse(discomfort.reportedDiscomfort(new Execution(time1, time5)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time15, time15)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time8, time11)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time1, time20)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time16, time19)));
	}
	
	@Test
	public void testDiscomfortFileWithErrors() throws IOException {
		writeDiscomfortFileWithErrors();
		
		discomfort = new Discomfort(DISCOMFORT_LOG_FILE_NAME);
		
		assertTrue(discomfort.reportedDiscomfort(new Execution(time1, time5)));
		assertTrue(discomfort.reportedDiscomfort(new Execution(time15, time15)));
		assertTrue(discomfort.reportedDiscomfort(new Execution(time8, time11)));
		assertTrue(discomfort.reportedDiscomfort(new Execution(time1, time20)));
		assertFalse(discomfort.reportedDiscomfort(new Execution(time16, time19)));
	}
	
	private void writeDiscomfortFileWithErrors() throws FileNotFoundException {
		PrintStream discomfortStream = new PrintStream(DISCOMFORT_LOG_FILE_NAME);
		
		discomfortStream.printf("<time> %d\n", time3);
		discomfortStream.printf(BLANK_LINE);
		discomfortStream.printf("<time> %d\n", time10);
		discomfortStream.printf(ERROR_LINE);
		discomfortStream.printf("<time> %d\n", time15);
		
		discomfortStream.close();	
	}

	private void writeBasicDiscomfortFile() throws FileNotFoundException {
		PrintStream discomfortStream = new PrintStream(DISCOMFORT_LOG_FILE_NAME);
		
		discomfortStream.printf("<time> %d\n", time3);
		discomfortStream.printf("<time> %d\n", time15);
		
		discomfortStream.close();
	}
	
	private void writeNoDiscomfortFile() {
		
	}
}
