package analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.HadoopInformation;
import analysis.data.Execution;
import analysis.data.HadoopMachineUsage;

public class HadoopTest {

	private static final String CPU_FILE_NAME = "cpu_test.txt";
	private static final String MEMORY_FILE_NAME = "memory_test.txt";
	private static final String CONTROLLER_LOG_FILE = "controller_test.txt";
	
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
	
	private Hadoop hadoop;
	
	@Before
	public void setUp() throws Exception {
		File cpuFile = new File(CPU_FILE_NAME);
		File memoryFile = new File(MEMORY_FILE_NAME);
		File controllerFile = new File(CONTROLLER_LOG_FILE);
		
		cpuFile.createNewFile();
		memoryFile.createNewFile();
		controllerFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(CPU_FILE_NAME).delete();
		new File(MEMORY_FILE_NAME).delete();
		new File(CONTROLLER_LOG_FILE).delete();
	}

	@Test
	public void testGetMachineUsage() throws IOException {
		writeBasicCPUFile();
		writeBasicMemoryFile();
		writeBasicHadoopInterfaceFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE);
		
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
		
		assertEquals(5, result2.getCPU().size());
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
		
		assertEquals(3, result3.getCPU().size());
		assertEquals(MEMORY_3, result3.getMemory().get(time3));
		assertEquals(MEMORY_4, result3.getMemory().get(time4));
		assertEquals(MEMORY_5, result3.getMemory().get(time5));
		
		HadoopMachineUsage result4 = hadoop.getMachineUsage(new Execution(time0, time3));
		
		assertEquals(3, result4.getCPU().size());
		assertEquals(CPU_1, result4.getCPU().get(time1));
		assertEquals(CPU_2, result4.getCPU().get(time2));
		assertEquals(CPU_3, result4.getCPU().get(time3));
		
		assertEquals(3, result4.getCPU().size());
		assertEquals(MEMORY_1, result4.getMemory().get(time1));
		assertEquals(MEMORY_2, result4.getMemory().get(time2));
		assertEquals(MEMORY_3, result4.getMemory().get(time3));
	}

	@Test
	public void testGetinformation() throws IOException {
		writeBasicCPUFile();
		writeBasicMemoryFile();
		writeBasicHadoopInterfaceFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME, CONTROLLER_LOG_FILE);
		
		HadoopInformation result1 = hadoop.getInformation(new Execution(time1, time20));
		
		assertEquals(8, result1.getBenchmarks().size());		
		assertEquals(MR, result1.getBenchmarks().get(time3));
		assertEquals(DFWRITE, result1.getBenchmarks().get(time5));
		assertEquals(DFREAD, result1.getBenchmarks().get(time8));
		assertEquals(DFCLEAN, result1.getBenchmarks().get(time10));
		assertEquals(TERAGEN, result1.getBenchmarks().get(time13));
		assertEquals(TERASORT, result1.getBenchmarks().get(time15));
		assertEquals(TERAVALIDATE, result1.getBenchmarks().get(time16));
		assertEquals(TERACLEAN, result1.getBenchmarks().get(time19));
		
		HadoopInformation result2 = hadoop.getInformation(new Execution(time7, time15));
		
		assertEquals(4, result2.getBenchmarks().size());	
		assertEquals(DFREAD, result2.getBenchmarks().get(time8));
		assertEquals(DFCLEAN, result2.getBenchmarks().get(time10));
		assertEquals(TERAGEN, result2.getBenchmarks().get(time13));
		assertEquals(TERASORT, result2.getBenchmarks().get(time15));
		
		HadoopInformation result3 = hadoop.getInformation(new Execution(time13, time13));
		
		assertEquals(1, result3.getBenchmarks().size());
		assertEquals(TERAGEN, result3.getBenchmarks().get(time13));
		
		HadoopInformation result4 = hadoop.getInformation(new Execution(time6, time7));
		
		assertEquals(0, result4.getBenchmarks().size());
	}
	
	private void writeBasicHadoopInterfaceFile() throws FileNotFoundException {
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
