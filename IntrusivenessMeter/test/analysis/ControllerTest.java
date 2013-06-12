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
	private static final String HADOOP_PROCESSES_LOG_FILE_NAME = "hadoop.proc";
	private static final String CONTROLLER_LOG_FILE_NAME = "controller_test.log";
	private static final Object STARTED_BENCHMARK_MESSAGE = "started benchmark:";
	private static final String NO_PROCESSES_STRING = "[]";
	private static final String SOME_PROCESS_STRING = "[100]";
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
		File controllerFile = new File(CONTROLLER_LOG_FILE_NAME);
		File hadoopProcessesFile = new File(HADOOP_PROCESSES_LOG_FILE_NAME);
	
		controllerFile.createNewFile();
		hadoopProcessesFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(CONTROLLER_LOG_FILE_NAME).delete();
		new File(HADOOP_PROCESSES_LOG_FILE_NAME).delete();
	}
	
	@Test
	public void testGetExecutionsBasicFiles() throws IOException {
		writeBasicFiles();
		controller = new Controller(CONTROLLER_LOG_FILE_NAME, HADOOP_PROCESSES_LOG_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(1, executions.size());
		assertEquals(time2, executions.get(0).getStartTime());
		assertEquals(time7, executions.get(0).getFinishTime());
	}
	
	@Test
	public void testGetExecutionsComplexFiles() throws IOException {
		writeComplexFiles();
		controller = new Controller(CONTROLLER_LOG_FILE_NAME, HADOOP_PROCESSES_LOG_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(2, executions.size());
		assertEquals(time2, executions.get(0).getStartTime());
		assertEquals(time7, executions.get(0).getFinishTime());
		assertEquals(time11, executions.get(1).getStartTime());
		assertEquals(time18, executions.get(1).getFinishTime());	
	}
	
	@Test
	public void testUnsynchronizedFiles() throws IOException {
		writeUnsynchronizedFiles();
		controller = new Controller(CONTROLLER_LOG_FILE_NAME, HADOOP_PROCESSES_LOG_FILE_NAME);
		
		List<Execution> executions = controller.getExecutions();
		
		assertEquals(1, executions.size());
		assertEquals(time11, executions.get(0).getStartTime());
		assertEquals(time16, executions.get(0).getFinishTime());
	}
	
	private void writeUnsynchronizedFiles() throws FileNotFoundException {
		PrintStream controllerStream = new PrintStream(CONTROLLER_LOG_FILE_NAME);
		PrintStream hadoopProcessesStream = new PrintStream(HADOOP_PROCESSES_LOG_FILE_NAME);
		
		controllerStream.printf("<date> %d %s\n", time1, "anything");
		controllerStream.printf("<date> %d %s\n", time2, "anything");
		controllerStream.printf("<date> %d %s\n", time3, "anything");
		controllerStream.printf("<date> %d %s\n", time4, "anything");
		controllerStream.printf("<date> %d %s\n", time5, "anything");
		controllerStream.printf("<date> %d %s\n", time6, "anything");
		controllerStream.printf("<date> %d %s\n", time7, "anything");
		controllerStream.printf("<date> %d %s\n", time8, "anything");
		controllerStream.printf("<date> %d %s\n", time9, "anything");
		controllerStream.printf("<date> %d %s\n", time10, "anything");
		controllerStream.printf("<date> %d %s\n", time11, STARTED_BENCHMARK_MESSAGE);		
		controllerStream.printf("<date> %d %s\n", time12, "anything");
		controllerStream.printf("<date> %d %s\n", time13, "anything");
		controllerStream.printf("<date> %d %s\n", time14, "anything");
		controllerStream.printf("<date> %d %s\n", time15, "anything");
		controllerStream.printf("<date> %d %s\n", time16, "anything");
		controllerStream.printf("<date> %d %s\n", time17, "anything");
		controllerStream.printf("<date> %d %s\n", time18, "anything");
		controllerStream.printf("<date> %d %s\n", time19, "anything");
		controllerStream.printf("<date> %d %s\n", time20, "anything");
		
		hadoopProcessesStream.printf("<date> %d %s\n", time8, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time9, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time10, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time11, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time12, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time13, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time14, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time15, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time16, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time17, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time18, NO_PROCESSES_STRING);
		
		controllerStream.close();
		hadoopProcessesStream.close();
	}

	private void writeBasicFiles() throws FileNotFoundException {
		PrintStream controllerStream = new PrintStream(CONTROLLER_LOG_FILE_NAME);
		PrintStream hadoopProcessesStream = new PrintStream(HADOOP_PROCESSES_LOG_FILE_NAME);
		
		controllerStream.printf("<date> %d %s\n", time1, "anything");
		controllerStream.printf("<date> %d %s\n", time2, STARTED_BENCHMARK_MESSAGE);
		controllerStream.printf("<date> %d %s\n", time3, "anything");
		controllerStream.printf("<date> %d %s\n", time4, "anything");
		controllerStream.printf("<date> %d %s\n", time5, "anything");
		controllerStream.printf("<date> %d %s\n", time6, "anything");
		controllerStream.printf("<date> %d %s\n", time7, "anything");
		controllerStream.printf("<date> %d %s\n", time8, "anything");
		controllerStream.printf("<date> %d %s\n", time9, "anything");
		controllerStream.printf("<date> %d %s\n", time10, "anything");
		controllerStream.printf("<date> %d %s\n", time11, "anything");		
		
		hadoopProcessesStream.printf("<date> %d %s\n", time1, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time2, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time3, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time4, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time5, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time6, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time7, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time8, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time9, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time10, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time11, NO_PROCESSES_STRING);
		
		controllerStream.close();
		hadoopProcessesStream.close();
	}
	
	private void writeComplexFiles() throws FileNotFoundException {
		PrintStream controllerStream = new PrintStream(CONTROLLER_LOG_FILE_NAME);
		PrintStream hadoopProcessesStream = new PrintStream(HADOOP_PROCESSES_LOG_FILE_NAME);
		
		controllerStream.printf("<date> %d %s\n", time1, "anything");
		controllerStream.printf("<date> %d %s\n", time2, STARTED_BENCHMARK_MESSAGE);
		controllerStream.printf("<date> %d %s\n", time3, "anything");
		controllerStream.printf("<date> %d %s\n", time4, "anything");
		controllerStream.printf("<date> %d %s\n", time5, "anything");
		controllerStream.printf("<date> %d %s\n", time6, "anything");
		controllerStream.printf("<date> %d %s\n", time7, "anything");
		controllerStream.printf("<date> %d %s\n", time8, "anything");
		controllerStream.printf("<date> %d %s\n", time9, "anything");
		controllerStream.printf("<date> %d %s\n", time10, "anything");
		controllerStream.printf("<date> %d %s\n", time11, STARTED_BENCHMARK_MESSAGE);		
		controllerStream.printf("<date> %d %s\n", time12, "anything");
		controllerStream.printf("<date> %d %s\n", time13, "anything");
		controllerStream.printf("<date> %d %s\n", time14, "anything");
		controllerStream.printf("<date> %d %s\n", time15, "anything");
		controllerStream.printf("<date> %d %s\n", time16, "anything");
		controllerStream.printf("<date> %d %s\n", time17, "anything");
		controllerStream.printf("<date> %d %s\n", time18, "anything");
		controllerStream.printf("<date> %d %s\n", time19, "anything");
		controllerStream.printf("<date> %d %s\n", time20, "anything");
		
		hadoopProcessesStream.printf("<date> %d %s\n", time1, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time2, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time3, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time4, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time5, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time6, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time7, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time8, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time9, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time10, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time11, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time12, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time13, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time14, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time15, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time16, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time17, SOME_PROCESS_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time18, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time19, NO_PROCESSES_STRING);
		hadoopProcessesStream.printf("<date> %d %s\n", time20, NO_PROCESSES_STRING);
		
		controllerStream.close();
		hadoopProcessesStream.close();
	} 
}
