package analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class IdleUserTest {

	private static final String IDLE_TIME_LOG_FILE_NAME = "idle_test.log";
	private static final long ACTIVITY_THRESHOLD_1 = 3;
	private static final long ACTIVITY_THRESHOLD_2 = 6;
	private static final long ACTIVITY_THRESHOLD_3 = 10;
	private static final long ACTIVITY_THRESHOLD_4 = 0;
	
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
	
	private IdleUser idle;
	
	@Before
	public void setUp() throws Exception {
		File idleTimeFile = new File(IDLE_TIME_LOG_FILE_NAME);
		idleTimeFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(IDLE_TIME_LOG_FILE_NAME).delete();
	}

	@Test
	public void testIdle() throws IOException {
		writeBasicIdleFile();
		
		idle = new IdleUser(IDLE_TIME_LOG_FILE_NAME, ACTIVITY_THRESHOLD_1);
		
		assertFalse(idle.idle(new Execution(time1, time4)));
		assertFalse(idle.idle(new Execution(time1, time3)));
		assertFalse(idle.idle(new Execution(time8, time8)));
		assertTrue(idle.idle(new Execution(time6, time6)));
		assertFalse(idle.idle(new Execution(time1, time10)));
		assertTrue(idle.idle(new Execution(time4, time6)));
		
		idle = new IdleUser(IDLE_TIME_LOG_FILE_NAME, ACTIVITY_THRESHOLD_2);
		
		assertFalse(idle.idle(new Execution(time1, time4)));
		assertFalse(idle.idle(new Execution(time1, time3)));
		assertFalse(idle.idle(new Execution(time8, time8)));
		assertFalse(idle.idle(new Execution(time6, time6)));
		assertFalse(idle.idle(new Execution(time1, time10)));
		assertTrue(idle.idle(new Execution(time7, time7)));
		
		idle = new IdleUser(IDLE_TIME_LOG_FILE_NAME, ACTIVITY_THRESHOLD_3);
		
		assertFalse(idle.idle(new Execution(time1, time4)));
		assertFalse(idle.idle(new Execution(time1, time3)));
		assertFalse(idle.idle(new Execution(time8, time8)));
		assertFalse(idle.idle(new Execution(time6, time6)));
		assertFalse(idle.idle(new Execution(time1, time10)));
		assertFalse(idle.idle(new Execution(time7, time7)));
		
		idle = new IdleUser(IDLE_TIME_LOG_FILE_NAME, ACTIVITY_THRESHOLD_4);
		
		assertTrue(idle.idle(new Execution(time1, time4)));
		assertTrue(idle.idle(new Execution(time1, time3)));
		assertTrue(idle.idle(new Execution(time8, time8)));
		assertTrue(idle.idle(new Execution(time6, time6)));
		assertTrue(idle.idle(new Execution(time1, time10)));
		assertTrue(idle.idle(new Execution(time7, time7)));
	}

	private void writeBasicIdleFile() throws FileNotFoundException {
		PrintStream idleTimeStream = new PrintStream(IDLE_TIME_LOG_FILE_NAME);	
		
		idleTimeStream.printf("<date> %d %d\n", time1, 0);
		idleTimeStream.printf("<date> %d %d\n", time2, 1);
		idleTimeStream.printf("<date> %d %d\n", time3, 2);
		idleTimeStream.printf("<date> %d %d\n", time4, 3);
		idleTimeStream.printf("<date> %d %d\n", time5, 4);
		idleTimeStream.printf("<date> %d %d\n", time6, 5);
		idleTimeStream.printf("<date> %d %d\n", time7, 6);
		idleTimeStream.printf("<date> %d %d\n", time8, 0);
		idleTimeStream.printf("<date> %d %d\n", time9, 1);
		idleTimeStream.printf("<date> %d %d\n", time10, 2);
		
		idleTimeStream.close();	
	}
}
