package analysis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;

public class HadoopTest {

	private static final String INCARNATION_ID_INFO = "Incarnation ID: 490678415";
	private static final String CPU_FILE_NAME = "cpu_test.txt";
	private static final String MEMORY_FILE_NAME = "memory_test.txt";
	private static final String CONTROLLER_LOG_FILE = "controller_test.txt";
	private static final String HADOOP_PROCESSES_FILE_NAME = "hadoop_process_test.txt";
	
	private static long time0 = 999;
	private static long time1 = time0 + 1;
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
	
	private static final Double CPU_1 = 0.0;
	private static final Double CPU_2 = CPU_1 + 20;
	private static final Double CPU_3 = CPU_2 + 20;
	private static final Double CPU_4 = CPU_3 + 20;
	private static final Double CPU_5 = CPU_4 + 20;
	
	private static final Double MEMORY_1 = 23.0;
	private static final Double MEMORY_2 = MEMORY_1 + 20;
	private static final Double MEMORY_3 = MEMORY_2 + 20;
	private static final Double MEMORY_4 = MEMORY_3 + 20;
	private static final Double MEMORY_5 = MEMORY_4 + 20;
	
	private static final String STARTING_BENCHMARK = "started benchmark:";
	
	private static final String ANYTHING = "anything";
	
	private static final String DFREAD = "dfread";
	private static final String DFWRITE = "dfwrite";
	private static final String DFCLEAN = "dfclean";
	private static final String MR = "mr";
	private static final String TERAGEN = "teragen";
	private static final String TERASORT = "terasort";
	private static final String TERAVALIDATE = "teravalidate";
	private static final String TERACLEAN = "teraclean";
	
	private static final String DFREAD_MARK = STARTING_BENCHMARK + DFREAD;
	private static final String DFWRITE_MARK = STARTING_BENCHMARK  + DFWRITE;
	private static final String DFCLEAN_MARK = STARTING_BENCHMARK  + DFCLEAN;
	private static final String MR_MARK = STARTING_BENCHMARK  + MR;
	private static final String TERAGEN_MARK = STARTING_BENCHMARK + TERAGEN;
	private static final String TERASORT_MARK = STARTING_BENCHMARK + TERASORT;
	private static final String TERAVALIDATE_MARK = STARTING_BENCHMARK + TERAVALIDATE;
	private static final String TERACLEAN_MARK = STARTING_BENCHMARK + TERACLEAN;
	
	private static final String NO_HADOOP_PROCESS = "[]";
	private static final String HADOOP_PROCESSES = "['111', '1111']";
	
	private Hadoop hadoop;
	
	@Before
	public void setUp() throws Exception {
		File cpuFile = new File(CPU_FILE_NAME);
		File memoryFile = new File(MEMORY_FILE_NAME);
		File controllerFile = new File(CONTROLLER_LOG_FILE);
		File hadoopProcessFile = new File(HADOOP_PROCESSES_FILE_NAME);
		
		cpuFile.createNewFile();
		memoryFile.createNewFile();
		controllerFile.createNewFile();
		hadoopProcessFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(CPU_FILE_NAME).delete();
		new File(MEMORY_FILE_NAME).delete();
		new File(CONTROLLER_LOG_FILE).delete();
		new File(HADOOP_PROCESSES_FILE_NAME).delete();
	}

	@Test
	public void testGetMachineUsage() throws IOException {
		writeBasicCPUFile();
		writeBasicMemoryFile();
		writeBasicHadoopBenchmarksFile();
		writeBasicHadoopProcessesFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE, HADOOP_PROCESSES_FILE_NAME);
		
		HadoopMachineUsage result = hadoop.getMachineUsage(new Execution(time1, time1));
		
		assertEquals(1, result.getCPU().size());
		assertEquals(CPU_1, result.getCPU().get(time1));
		
		assertEquals(1, result.getMemory().size());
		assertEquals(MEMORY_1, result.getMemory().get(time1));
		
		HadoopMachineUsage result2 = hadoop.getMachineUsage(new Execution(time1, time5));
		
		assertEquals(5, result2.getCPU().size());
		assertEquals(CPU_1, result2.getCPU().get(time1));
		assertEquals(CPU_2, result2.getCPU().get(time2));
		assertEquals(CPU_3, result2.getCPU().get(time3));
		assertEquals(CPU_4, result2.getCPU().get(time4));
		assertEquals(CPU_5, result2.getCPU().get(time5));
		
		assertEquals(5, result2.getMemory().size());
		assertEquals(MEMORY_1, result2.getMemory().get(time1));
		assertEquals(MEMORY_2, result2.getMemory().get(time2));
		assertEquals(MEMORY_3, result2.getMemory().get(time3));
		assertEquals(MEMORY_4, result2.getMemory().get(time4));
		assertEquals(MEMORY_5, result2.getMemory().get(time5));
		
		HadoopMachineUsage result3 = hadoop.getMachineUsage(new Execution(time3, time6));
		
		assertEquals(3, result3.getCPU().size());
		assertEquals(CPU_3, result3.getCPU().get(time3));
		assertEquals(CPU_4, result3.getCPU().get(time4));
		assertEquals(CPU_5, result3.getCPU().get(time5));
		
		assertEquals(3, result3.getMemory().size());
		assertEquals(MEMORY_3, result3.getMemory().get(time3));
		assertEquals(MEMORY_4, result3.getMemory().get(time4));
		assertEquals(MEMORY_5, result3.getMemory().get(time5));
		
		HadoopMachineUsage result4 = hadoop.getMachineUsage(new Execution(time0, time3));
		
		assertEquals(3, result4.getCPU().size());
		assertEquals(CPU_1, result4.getCPU().get(time1));
		assertEquals(CPU_2, result4.getCPU().get(time2));
		assertEquals(CPU_3, result4.getCPU().get(time3));
		
		assertEquals(3, result4.getMemory().size());
		assertEquals(MEMORY_1, result4.getMemory().get(time1));
		assertEquals(MEMORY_2, result4.getMemory().get(time2));
		assertEquals(MEMORY_3, result4.getMemory().get(time3));
	}

	@Test
	public void testGetinformation() throws IOException {
		writeBasicCPUFile();
		writeBasicMemoryFile();
		writeBasicHadoopBenchmarksFile();
		writeBasicHadoopProcessesFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE, HADOOP_PROCESSES_FILE_NAME);
		
		HadoopInformation result1 = hadoop.getInformation(new Execution(time3, time4));
		
		assertEquals(1, result1.getBenchmarks().size());		
		assertEquals(MR, result1.getBenchmarks().get(time3));
		
		HadoopInformation result2 = hadoop.getInformation(new Execution(time16, time16));
		
		assertEquals(1, result2.getBenchmarks().size());		
		assertEquals(TERASORT, result2.getBenchmarks().get(time15));
	}
	
	@Test
	public void testGetInformationDifferentTimes() throws IOException {
		writeDifferentTimesCPUMemoryAndHadoopBenchmarksFiles();
		writeBasicHadoopProcessesFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE, HADOOP_PROCESSES_FILE_NAME);
		
		HadoopInformation result1 = hadoop.getInformation(new Execution(time9, time9));
		
		assertEquals(1, result1.getBenchmarks().size());		
		assertEquals(DFREAD, result1.getBenchmarks().get(time8));
		
		HadoopMachineUsage result2 = hadoop.getMachineUsage(new Execution(time1, time5));
		
		assertEquals(5, result2.getCPU().size());
		assertEquals(CPU_1, result2.getCPU().get(time1));
		assertEquals(CPU_2, result2.getCPU().get(time2));
		assertEquals(CPU_3, result2.getCPU().get(time3));
		assertEquals(CPU_4, result2.getCPU().get(time4));
		assertEquals(CPU_5, result2.getCPU().get(time5));
		
		assertEquals(0, result2.getMemory().size());
		
		HadoopMachineUsage result3 = hadoop.getMachineUsage(new Execution(time10, time15));
		
		assertEquals(0, result3.getCPU().size());
		
		assertEquals(5, result3.getMemory().size());
		assertEquals(MEMORY_1, result3.getMemory().get(time10));
		assertEquals(MEMORY_2, result3.getMemory().get(time11));
		assertEquals(MEMORY_3, result3.getMemory().get(time12));
		assertEquals(MEMORY_4, result3.getMemory().get(time13));
		assertEquals(MEMORY_5, result3.getMemory().get(time14));
	}
	
	@Test
	public void testHandleIncarnationIDLog() throws IOException {
		writeFilesWithIncarnationIDLog();
		writeBasicHadoopProcessesFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE, HADOOP_PROCESSES_FILE_NAME);
		
		HadoopInformation result1 = hadoop.getInformation(new Execution(time9, time9));
		
		assertEquals(1, result1.getBenchmarks().size());		
		assertEquals(DFREAD, result1.getBenchmarks().get(time8));
		
		HadoopMachineUsage result2 = hadoop.getMachineUsage(new Execution(time1, time6));
		
		assertEquals(5, result2.getCPU().size());
		assertEquals(CPU_1, result2.getCPU().get(time1));
		assertEquals(CPU_2, result2.getCPU().get(time2));
		assertEquals(CPU_3, result2.getCPU().get(time3));
		assertEquals(CPU_4, result2.getCPU().get(time5));
		assertEquals(CPU_5, result2.getCPU().get(time6));
		
		assertEquals(0, result2.getMemory().size());
		
		HadoopMachineUsage result3 = hadoop.getMachineUsage(new Execution(time10, time15));
		
		assertEquals(0, result3.getCPU().size());
		
		assertEquals(5, result3.getMemory().size());
		assertEquals(MEMORY_1, result3.getMemory().get(time10));
		assertEquals(MEMORY_2, result3.getMemory().get(time11));
		assertEquals(MEMORY_3, result3.getMemory().get(time13));
		assertEquals(MEMORY_4, result3.getMemory().get(time14));
		assertEquals(MEMORY_5, result3.getMemory().get(time15));
	}
	
	@Test
	public void testThereAreRunningTasks() throws IOException {
		writeFilesWithIncarnationIDLog();
		writeBasicHadoopProcessesFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE, HADOOP_PROCESSES_FILE_NAME);
		
		assertFalse(hadoop.thereAreRunningTasks(new Execution(time1, time3)));
		assertTrue(hadoop.thereAreRunningTasks(new Execution(time5, time6)));
		assertFalse(hadoop.thereAreRunningTasks(new Execution(time9, time10)));
		assertTrue(hadoop.thereAreRunningTasks(new Execution(time10, time11)));
		assertTrue(hadoop.thereAreRunningTasks(new Execution(time7, time7)));
		assertFalse(hadoop.thereAreRunningTasks(new Execution(time2, time2)));
	}
	
	private void writeBasicHadoopProcessesFile() throws FileNotFoundException {
		PrintStream hadoopProcessesStream = new PrintStream(HADOOP_PROCESSES_FILE_NAME);
		
		hadoopProcessesStream.printf("<time> %d %s\n", time1, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time2, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time3, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time4, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time5, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time6, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time7, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time8, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time9, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time10, NO_HADOOP_PROCESS);
		hadoopProcessesStream.printf("<time> %d %s\n", time11, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time12, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time13, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time14, HADOOP_PROCESSES);
		hadoopProcessesStream.printf("<time> %d %s\n", time15, NO_HADOOP_PROCESS);
		
		hadoopProcessesStream.close();
	}

	private void writeFilesWithIncarnationIDLog() throws FileNotFoundException {
		PrintStream controllerLogStream = new PrintStream(CONTROLLER_LOG_FILE);
		
		controllerLogStream.printf("<time> %d %s\n", time6, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time7, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time8, DFREAD_MARK);
		controllerLogStream.printf("<time> %d %s\n", time9, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time10, DFCLEAN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time11, INCARNATION_ID_INFO);
		controllerLogStream.printf("<time> %d %s\n", time12, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time13, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time14, TERAGEN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time15, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time16, TERASORT_MARK);
		controllerLogStream.printf("<time> %d %s\n", time17, TERAVALIDATE_MARK);
		controllerLogStream.printf("<time> %d %s\n", time18, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time19, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time20, TERACLEAN_MARK);
		
		controllerLogStream.close();
		
		PrintStream cpuStream = new PrintStream(CPU_FILE_NAME);
		
		cpuStream.printf("<time> %d %f\n", time1, CPU_1);
		cpuStream.printf("<time> %d %f\n", time2, CPU_2);
		cpuStream.printf("<time> %d %f\n", time3, CPU_3);
		cpuStream.printf("<time> %d %s\n", time4, INCARNATION_ID_INFO);
		cpuStream.printf("<time> %d %f\n", time5, CPU_4);
		cpuStream.printf("<time> %d %f\n", time6, CPU_5);
		
		cpuStream.close();
		
		PrintStream memoryStream = new PrintStream(MEMORY_FILE_NAME);
		
		memoryStream.printf("<time> %d %f\n", time10, MEMORY_1);
		memoryStream.printf("<time> %d %f\n", time11, MEMORY_2);
		memoryStream.printf("<time> %d %s\n", time12, INCARNATION_ID_INFO);
		memoryStream.printf("<time> %d %f\n", time13, MEMORY_3);
		memoryStream.printf("<time> %d %f\n", time14, MEMORY_4);
		memoryStream.printf("<time> %d %f\n", time15, MEMORY_5);
		
		memoryStream.close();
	}

	private void writeDifferentTimesCPUMemoryAndHadoopBenchmarksFiles() throws FileNotFoundException {
		PrintStream controllerLogStream = new PrintStream(CONTROLLER_LOG_FILE);
		
		controllerLogStream.printf("<time> %d %s\n", time6, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time7, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time8, DFREAD_MARK);
		controllerLogStream.printf("<time> %d %s\n", time9, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time10, DFCLEAN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time11, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time12, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time13, TERAGEN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time14, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time15, TERASORT_MARK);
		controllerLogStream.printf("<time> %d %s\n", time16, TERAVALIDATE_MARK);
		controllerLogStream.printf("<time> %d %s\n", time17, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time18, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time19, TERACLEAN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time20, ANYTHING);
		
		controllerLogStream.close();
		
		PrintStream cpuStream = new PrintStream(CPU_FILE_NAME);
		
		cpuStream.printf("<time> %d %f\n", time1, CPU_1);
		cpuStream.printf("<time> %d %f\n", time2, CPU_2);
		cpuStream.printf("<time> %d %f\n", time3, CPU_3);
		cpuStream.printf("<time> %d %f\n", time4, CPU_4);
		cpuStream.printf("<time> %d %f\n", time5, CPU_5);
		
		cpuStream.close();
		
		PrintStream memoryStream = new PrintStream(MEMORY_FILE_NAME);
		
		memoryStream.printf("<time> %d %f\n", time10, MEMORY_1);
		memoryStream.printf("<time> %d %f\n", time11, MEMORY_2);
		memoryStream.printf("<time> %d %f\n", time12, MEMORY_3);
		memoryStream.printf("<time> %d %f\n", time13, MEMORY_4);
		memoryStream.printf("<time> %d %f\n", time14, MEMORY_5);
		
		memoryStream.close();
	}

	private void writeBasicHadoopBenchmarksFile() throws FileNotFoundException {
		PrintStream controllerLogStream = new PrintStream(CONTROLLER_LOG_FILE);
		
		controllerLogStream.printf("<time> %d %s\n", time1, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time2, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time3, MR_MARK);
		controllerLogStream.printf("<time> %d %s\n", time4, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time5, DFWRITE_MARK);
		controllerLogStream.printf("<time> %d %s\n", time6, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time7, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time8, DFREAD_MARK);
		controllerLogStream.printf("<time> %d %s\n", time9, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time10, DFCLEAN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time11, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time12, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time13, TERAGEN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time14, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time15, TERASORT_MARK);
		controllerLogStream.printf("<time> %d %s\n", time16, TERAVALIDATE_MARK);
		controllerLogStream.printf("<time> %d %s\n", time17, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time18, ANYTHING);
		controllerLogStream.printf("<time> %d %s\n", time19, TERACLEAN_MARK);
		controllerLogStream.printf("<time> %d %s\n", time20, ANYTHING);
		
		controllerLogStream.close();
	}

	private void writeBasicCPUFile() throws FileNotFoundException {
		PrintStream cpuStream = new PrintStream(CPU_FILE_NAME);
		
		cpuStream.printf("<time> %d %f\n", time1, CPU_1);
		cpuStream.printf("<time> %d %f\n", time2, CPU_2);
		cpuStream.printf("<time> %d %f\n", time3, CPU_3);
		cpuStream.printf("<time> %d %f\n", time4, CPU_4);
		cpuStream.printf("<time> %d %f\n", time5, CPU_5);
		
		cpuStream.close();
	}
	
	private void writeBasicMemoryFile() throws FileNotFoundException {
		PrintStream memoryStream = new PrintStream(MEMORY_FILE_NAME);
		
		memoryStream.printf("<time> %d %f\n", time1, MEMORY_1);
		memoryStream.printf("<time> %d %f\n", time2, MEMORY_2);
		memoryStream.printf("<time> %d %f\n", time3, MEMORY_3);
		memoryStream.printf("<time> %d %f\n", time4, MEMORY_4);
		memoryStream.printf("<time> %d %f\n", time5, MEMORY_5);
		
		memoryStream.close();
	}
}
