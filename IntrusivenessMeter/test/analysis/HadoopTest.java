package analysis;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.Execution;
import analysis.data.HadoopMachineUsage;

public class HadoopTest {

	private static final String CPU_FILE_NAME = "cpu_test.txt";
	private static final String MEMORY_FILE_NAME = "memory_test.txt";
	
	private static long time0 = 999;
	private static long time1 = time0 + 1;
	private static long time2 = time1 + 1;
	private static long time3 = time2 + 1;
	private static long time4 = time3 + 1;
	private static long time5 = time4 + 1;
	private static long time6 = time5 + 1;
	
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
	
	private Hadoop hadoop;
	
	@Before
	public void setUp() throws Exception {
		File cpuFile = new File(CPU_FILE_NAME);
		File memoryFile = new File(MEMORY_FILE_NAME);
		
		cpuFile.createNewFile();
		memoryFile.createNewFile();
	}

	@After
	public void tearDown() throws Exception {
		new File(CPU_FILE_NAME).delete();
		new File(MEMORY_FILE_NAME).delete();
	}

	@Test
	public void testGetMachineUsage() throws IOException {
		writeBasicCPUFile();
		writeBasicMemoryFile();
		
		hadoop = new Hadoop(CPU_FILE_NAME, MEMORY_FILE_NAME);
		
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
