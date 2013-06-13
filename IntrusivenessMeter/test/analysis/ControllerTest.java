package analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ControllerTest {
	private static final String HADOOP_RUNNING_INFO_FILE_NAME = "hadoop_running_test.log";
	
	private static final String RUNNING_MESSAGE = "True";
	private static final String NOT_RUNNING_MESSAGE = "False";
	
	private Controller controller;	
	
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
	
	@Before
	public void setUp() throws Exception {
		File hadoopRunningFile = new File(HADOOP_RUNNING_INFO_FILE_NAME);
		hadoopRunningFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(HADOOP_RUNNING_INFO_FILE_NAME).delete();
	}
	
	@Test
	public void testGetExecutionsBasicFiles() throws IOException {
		writeBasicFiles();
		controller = new Controller(HADOOP_RUNNING_INFO_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(2, executions.size());
		assertEquals(time4, executions.get(0).getStartTime());
		assertEquals(time5, executions.get(0).getFinishTime());
		assertEquals(time7, executions.get(1).getStartTime());
		assertEquals(time10, executions.get(1).getFinishTime());		
	}
	
	@Test
	public void testGetExecutionsNoExecutionsFiles() throws IOException {
		writeNoExecutionFiles();
		
		controller = new Controller(HADOOP_RUNNING_INFO_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(0, executions.size());
	}
	
	@Test
	public void testGetExecutionsManyExecutionsFiles() throws IOException {
		writeManyExecutionsFiles();
		
		controller = new Controller(HADOOP_RUNNING_INFO_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(4, executions.size());
		assertEquals(time1, executions.get(0).getStartTime());
		assertEquals(time3, executions.get(0).getFinishTime());
		assertEquals(time5, executions.get(1).getStartTime());
		assertEquals(time6, executions.get(1).getFinishTime());
		assertEquals(time8, executions.get(2).getStartTime());
		assertEquals(time10, executions.get(2).getFinishTime());
		assertEquals(time12, executions.get(3).getStartTime());
		assertEquals(time18, executions.get(3).getFinishTime());
	}
	
	private void writeManyExecutionsFiles() throws FileNotFoundException {
		PrintStream hadoopInfoStream = new PrintStream(HADOOP_RUNNING_INFO_FILE_NAME);	
		
		hadoopInfoStream.printf("<date> %d %s\n", time1, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time2, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time3, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time4, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time5, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time6, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time7, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time8, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time9, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time10, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time11, NOT_RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time12, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time13, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time14, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time15, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time16, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time17, RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time18, NOT_RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time19, NOT_RUNNING_MESSAGE);	
		hadoopInfoStream.printf("<date> %d %s\n", time20, NOT_RUNNING_MESSAGE);	
		
		hadoopInfoStream.close();
	}

	private void writeNoExecutionFiles() throws FileNotFoundException {
		PrintStream hadoopInfoStream = new PrintStream(HADOOP_RUNNING_INFO_FILE_NAME);

		hadoopInfoStream.printf("<date> %d %s\n", time1, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time2, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time3, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time4, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time5, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time6, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time7, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time8, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time9, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time10, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time11, NOT_RUNNING_MESSAGE);	
		
		hadoopInfoStream.close();
	}

	private void writeBasicFiles() throws FileNotFoundException {
		PrintStream hadoopInfoStream = new PrintStream(HADOOP_RUNNING_INFO_FILE_NAME);
		
		hadoopInfoStream.printf("<date> %d %s\n", time1, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time2, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time3, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time4, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time5, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time6, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time7, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time8, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time9, RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time10, NOT_RUNNING_MESSAGE);
		hadoopInfoStream.printf("<date> %d %s\n", time11, NOT_RUNNING_MESSAGE);		

		hadoopInfoStream.close();
	}
}
