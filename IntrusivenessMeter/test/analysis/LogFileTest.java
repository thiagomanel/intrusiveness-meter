package analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import commons.util.LogFile;

public class LogFileTest {
	
	private static final String testLog = "log.txt";
	
	private static final long time1 = 1;
	private static final long time2 = 2; 
	private static final long time3 = 3;
	
	private static final String message1 = "message1";
	private static final String message2 = "message2";
	private static final String message3 = "message3";
	
	private LogFile logFile;
	
	@Before
	public void setUp() throws IOException {
		File testFile = new File(testLog);
		testFile.createNewFile();
		
		writeBasicInput();
		logFile = new LogFile(testLog);
	}
	
	@After
	public void tearDown() {
		new File(testLog).delete();
	}
	
	@Test
	public void testGetMessage() throws IOException {
		assertEquals(message1, logFile.getMessage());
		assertEquals(message1, logFile.getMessage());
		
		logFile.advance();
		
		assertEquals(message2, logFile.getMessage());
		assertEquals(message2, logFile.getMessage());
		
		logFile.advance();
		
		assertEquals(message3 + " " + message2, logFile.getMessage());
		assertEquals(message3 + " " + message2, logFile.getMessage());
		
		logFile.close();
	}
	
	@Test
	public void testGetLineTime() throws IOException {
		assertEquals(time1, logFile.getLineTime());
		assertEquals(time1, logFile.getLineTime());
		
		logFile.advance();
		
		assertEquals(time2, logFile.getLineTime());
		assertEquals(time2, logFile.getLineTime());
		
		logFile.advance();
		
		assertEquals(time3, logFile.getLineTime());
		assertEquals(time3, logFile.getLineTime());
		
		logFile.close();
	}
	
	private void writeBasicInput() throws IOException {
		PrintStream writer = new PrintStream(testLog); 
		
		writer.printf("adate %d %s\n", time1, message1);
		writer.printf("adate %d %s\n", time2, message2);
		writer.printf("adate %d %s\n", time3, message3 + "    " + message2);
		
		writer.close();
	}
}