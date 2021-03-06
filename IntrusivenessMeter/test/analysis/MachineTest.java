package analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class MachineTest {

	private static final String IDLE_CPU_INFO_FILENAME = "idlecpu_test.log";
	private static final String USER_CPU_INFO_FILENAME = "usercpu_test.log";
	private static final String MEMORY_INFO_FILENAME = "mem_test.log";
	private static final String READ_INFO_FILENAME = "read_test.log";
	private static final String WRITE_INFO_FILENAME = "write_test.log";
		
	private static final Double WRITE_ATTEMPTS_1 = 500.0;
	private static final Double WRITE_ATTEMPTS_2 = WRITE_ATTEMPTS_1 + 100;
	private static final Double WRITE_ATTEMPTS_3 = WRITE_ATTEMPTS_2 + 100;
	private static final Double WRITE_ATTEMPTS_4 = WRITE_ATTEMPTS_3 + 100;
	private static final Double WRITE_ATTEMPTS_5 = WRITE_ATTEMPTS_4 + 100;
	
	private static final Double WRITE_NUM_1 = 300.0;
	private static final Double WRITE_NUM_2 = WRITE_NUM_1 + 100L;
	private static final Double WRITE_NUM_3 = WRITE_NUM_2 + 100L;
	private static final Double WRITE_NUM_4 = WRITE_NUM_3 + 100L;
	private static final Double WRITE_NUM_5 = WRITE_NUM_4 + 100L;
	
	private static final Double READ_NUM_1 = 900.0;
	private static final Double READ_NUM_2 = READ_NUM_1 + 100;
	private static final Double READ_NUM_3 = READ_NUM_2 + 100;
	private static final Double READ_NUM_4 = READ_NUM_3 + 100;
	private static final Double READ_NUM_5 = READ_NUM_4 + 100;
	
	private static final Double READ_SECTORS_1 = 900.0;
	private static final Double READ_SECTORS_2 = READ_SECTORS_1 + 100;
	private static final Double READ_SECTORS_3 = READ_SECTORS_2 + 100;
	private static final Double READ_SECTORS_4 = READ_SECTORS_3 + 100;
	private static final Double READ_SECTORS_5 = READ_SECTORS_4 + 100;
	
	private static final Double MEMORY_USAGE_1 = 1300.0;
	private static final Double MEMORY_USAGE_2 = MEMORY_USAGE_1 + 100;
	private static final Double MEMORY_USAGE_3 = MEMORY_USAGE_2 + 100;
	private static final Double MEMORY_USAGE_4 = MEMORY_USAGE_3 + 100;
	private static final Double MEMORY_USAGE_5 = MEMORY_USAGE_4 + 100;
	
	private static final Double IDLE_CPU_1 = 0.0;
	private static final Double IDLE_CPU_2 = IDLE_CPU_1 + 20;
	private static final Double IDLE_CPU_3 = IDLE_CPU_2 + 20;
	private static final Double IDLE_CPU_4 = IDLE_CPU_3 + 20;
	private static final Double IDLE_CPU_5 = IDLE_CPU_4 + 20;
	private static final Double IDLE_CPU_6 = IDLE_CPU_5 + 20;
	
	private static final Double USER_CPU_1 = 0.0;
	private static final Double USER_CPU_2 = USER_CPU_1 + 20;
	private static final Double USER_CPU_3 = USER_CPU_2 + 20;
	private static final Double USER_CPU_4 = USER_CPU_3 + 20;
	private static final Double USER_CPU_5 = USER_CPU_4 + 20;
	
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
	
	private static final String MACHINE_NAME = "localhost";
	private static final String HEADER_1 = "HEADER 1";
	private static final String HEADER_2 = "HEADER 2";
	private static final String HEADER_3 = "HEADER 3";
	private static final String HEADER_4 = "HEADER 4";
	private static final String HEADER_5 = "HEADER 5";
	private static final String HEADER_6 = "HEADER 6";
	
	private Machine machine;
	private IdleUser idle;
	private Hadoop hadoop; 
	
	private class DummyHadoop extends Hadoop {
		public DummyHadoop() {
			super(new HadoopMachineUsage(new HashMap<Long, Double>(), new HashMap<Long, Double>(), 
							new HashMap<Long, List<Integer>>()), new HadoopInformation(new HashMap<Long, String>()));
		}
		
		@Override
		public boolean thereAreRunningTasks(Execution execution) {
			return true;
		}
	}
	
	private class DummyIdleUser extends IdleUser {
		public DummyIdleUser() {
			super(new TreeMap<Long, Long>());
		}
		
		@Override
		public boolean idle(Execution execution) {
			return false;
		}
	}
	
	@Before
	public void setUp() throws Exception {
		File idleCpuInfoFile = new File(IDLE_CPU_INFO_FILENAME);
		File userCpuInfoFile = new File(USER_CPU_INFO_FILENAME);
		File memoryInfoFile = new File(MEMORY_INFO_FILENAME);
		File readInfoFile = new File(READ_INFO_FILENAME);
		File writeInfoFile = new File(WRITE_INFO_FILENAME);
		
		idleCpuInfoFile.createNewFile();
		userCpuInfoFile.createNewFile();
		memoryInfoFile.createNewFile();
		readInfoFile.createNewFile();
		writeInfoFile.createNewFile();
		
		idle = new DummyIdleUser();
		hadoop = new DummyHadoop();
	}

	@After
	public void tearDown() throws Exception {
		new File(IDLE_CPU_INFO_FILENAME).delete();
		new File(USER_CPU_INFO_FILENAME).delete();
		new File(MEMORY_INFO_FILENAME).delete();
		new File(READ_INFO_FILENAME).delete();
		new File(WRITE_INFO_FILENAME).delete();
	}
	
	@Test
	public void testGetUsage() throws IOException {
		writeBasicIdleCPUInfoFile();
		writeBasicUserCPUInfoFile();
		writeBasicMemoryInfoFile();
		writeBasicReadInfoFile();
		writeBasicWriteInfoFile();
		writeBasicMemoryInfoFile();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
							MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		MachineUsage result1 = machine.getUsage(new Execution(time1, time2));
		
		assertEquals(2, result1.getWriteNumber().size());
		assertEquals(WRITE_NUM_1, result1.getWriteNumber().get(time1));
		assertEquals(WRITE_NUM_2, result1.getWriteNumber().get(time2));
		assertEquals(2, result1.getWriteAttempts().size());
		assertEquals(WRITE_ATTEMPTS_1, result1.getWriteAttempts().get(time1));
		assertEquals(WRITE_ATTEMPTS_2, result1.getWriteAttempts().get(time2));
		assertEquals(2, result1.getReadNumber().size());
		assertEquals(READ_NUM_1, result1.getReadNumber().get(time1));
		assertEquals(READ_NUM_2, result1.getReadNumber().get(time2));
		assertEquals(2, result1.getReadNumber().size());
		assertEquals(READ_SECTORS_1, result1.getReadSectors().get(time1));
		assertEquals(READ_SECTORS_2, result1.getReadSectors().get(time2));
		assertEquals(2, result1.getIdleCPU().size());
		assertEquals(IDLE_CPU_1, result1.getIdleCPU().get(time1));
		assertEquals(IDLE_CPU_2, result1.getIdleCPU().get(time2));
		assertEquals(2, result1.getUserCPU().size());
		assertEquals(USER_CPU_1, result1.getUserCPU().get(time1));
		assertEquals(USER_CPU_2, result1.getUserCPU().get(time2));
		assertEquals(2, result1.getMemory().size());
		assertEquals(MEMORY_USAGE_1, result1.getMemory().get(time1));
		assertEquals(MEMORY_USAGE_2, result1.getMemory().get(time2));
		
		MachineUsage result2 = machine.getUsage(new Execution(time5, time5));
		
		assertEquals(1, result2.getWriteNumber().size());
		assertEquals(WRITE_NUM_5, result2.getWriteNumber().get(time5));
		assertEquals(1, result2.getWriteAttempts().size());
		assertEquals(WRITE_ATTEMPTS_5, result2.getWriteAttempts().get(time5));
		assertEquals(1, result2.getReadNumber().size());
		assertEquals(READ_NUM_5, result2.getReadNumber().get(time5));
		assertEquals(1, result2.getReadNumber().size());
		assertEquals(READ_SECTORS_5, result2.getReadSectors().get(time5));
		assertEquals(1, result2.getIdleCPU().size());
		assertEquals(IDLE_CPU_5, result2.getIdleCPU().get(time5));
		assertEquals(1, result2.getUserCPU().size());
		assertEquals(USER_CPU_5, result2.getUserCPU().get(time5));
		assertEquals(1, result2.getMemory().size());
		assertEquals(MEMORY_USAGE_5, result2.getMemory().get(time5));
		
		MachineUsage result3 = machine.getUsage(new Execution(time1, time5));
		
		assertEquals(5, result3.getWriteNumber().size());
		assertEquals(WRITE_NUM_1, result3.getWriteNumber().get(time1));
		assertEquals(WRITE_NUM_2, result3.getWriteNumber().get(time2));
		assertEquals(WRITE_NUM_3, result3.getWriteNumber().get(time3));
		assertEquals(WRITE_NUM_4, result3.getWriteNumber().get(time4));
		assertEquals(WRITE_NUM_5, result3.getWriteNumber().get(time5));
		assertEquals(5, result3.getWriteAttempts().size());
		assertEquals(WRITE_ATTEMPTS_1, result3.getWriteAttempts().get(time1));
		assertEquals(WRITE_ATTEMPTS_2, result3.getWriteAttempts().get(time2));
		assertEquals(WRITE_ATTEMPTS_3, result3.getWriteAttempts().get(time3));
		assertEquals(WRITE_ATTEMPTS_4, result3.getWriteAttempts().get(time4));
		assertEquals(WRITE_ATTEMPTS_5, result3.getWriteAttempts().get(time5));
		assertEquals(5, result3.getReadNumber().size());
		assertEquals(READ_NUM_1, result3.getReadNumber().get(time1));
		assertEquals(READ_NUM_2, result3.getReadNumber().get(time2));
		assertEquals(READ_NUM_3, result3.getReadNumber().get(time3));
		assertEquals(READ_NUM_4, result3.getReadNumber().get(time4));
		assertEquals(READ_NUM_5, result3.getReadNumber().get(time5));		
		assertEquals(5, result3.getReadNumber().size());
		assertEquals(READ_SECTORS_1, result3.getReadSectors().get(time1));
		assertEquals(READ_SECTORS_2, result3.getReadSectors().get(time2));
		assertEquals(READ_SECTORS_3, result3.getReadSectors().get(time3));
		assertEquals(READ_SECTORS_4, result3.getReadSectors().get(time4));
		assertEquals(READ_SECTORS_5, result3.getReadSectors().get(time5));
		assertEquals(5, result3.getIdleCPU().size());
		assertEquals(IDLE_CPU_1, result3.getIdleCPU().get(time1));
		assertEquals(IDLE_CPU_2, result3.getIdleCPU().get(time2));
		assertEquals(IDLE_CPU_3, result3.getIdleCPU().get(time3));
		assertEquals(IDLE_CPU_4, result3.getIdleCPU().get(time4));
		assertEquals(IDLE_CPU_5, result3.getIdleCPU().get(time5));
		assertEquals(5, result3.getUserCPU().size());
		assertEquals(USER_CPU_1, result3.getUserCPU().get(time1));
		assertEquals(USER_CPU_2, result3.getUserCPU().get(time2));
		assertEquals(USER_CPU_3, result3.getUserCPU().get(time3));
		assertEquals(USER_CPU_4, result3.getUserCPU().get(time4));
		assertEquals(USER_CPU_5, result3.getUserCPU().get(time5));
		assertEquals(5, result3.getMemory().size());
		assertEquals(MEMORY_USAGE_1, result3.getMemory().get(time1));
		assertEquals(MEMORY_USAGE_2, result3.getMemory().get(time2));
		assertEquals(MEMORY_USAGE_3, result3.getMemory().get(time3));
		assertEquals(MEMORY_USAGE_4, result3.getMemory().get(time4));
		assertEquals(MEMORY_USAGE_5, result3.getMemory().get(time5));
	}

	@Test
	public void getGetUsageDifferentTimes() throws IOException {
		writeDifferentTimesFiles();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
				MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		MachineUsage result1 = machine.getUsage(new Execution(time1, time20));
		
		assertEquals(5, result1.getWriteNumber().size());
		assertEquals(WRITE_NUM_1, result1.getWriteNumber().get(time1));
		assertEquals(WRITE_NUM_2, result1.getWriteNumber().get(time2));
		assertEquals(WRITE_NUM_3, result1.getWriteNumber().get(time3));
		assertEquals(WRITE_NUM_4, result1.getWriteNumber().get(time4));
		assertEquals(WRITE_NUM_5, result1.getWriteNumber().get(time5));
		assertEquals(5, result1.getWriteAttempts().size());
		assertEquals(WRITE_ATTEMPTS_1, result1.getWriteAttempts().get(time1));
		assertEquals(WRITE_ATTEMPTS_2, result1.getWriteAttempts().get(time2));
		assertEquals(WRITE_ATTEMPTS_3, result1.getWriteAttempts().get(time3));
		assertEquals(WRITE_ATTEMPTS_4, result1.getWriteAttempts().get(time4));
		assertEquals(WRITE_ATTEMPTS_5, result1.getWriteAttempts().get(time5));
		assertEquals(5, result1.getReadNumber().size());
		assertEquals(READ_NUM_1, result1.getReadNumber().get(time4));
		assertEquals(READ_NUM_2, result1.getReadNumber().get(time5));
		assertEquals(READ_NUM_3, result1.getReadNumber().get(time6));
		assertEquals(READ_NUM_4, result1.getReadNumber().get(time7));
		assertEquals(READ_NUM_5, result1.getReadNumber().get(time8));		
		assertEquals(5, result1.getReadNumber().size());
		assertEquals(READ_SECTORS_1, result1.getReadSectors().get(time4));
		assertEquals(READ_SECTORS_2, result1.getReadSectors().get(time5));
		assertEquals(READ_SECTORS_3, result1.getReadSectors().get(time6));
		assertEquals(READ_SECTORS_4, result1.getReadSectors().get(time7));
		assertEquals(READ_SECTORS_5, result1.getReadSectors().get(time8));
		assertEquals(5, result1.getIdleCPU().size());
		assertEquals(IDLE_CPU_1, result1.getIdleCPU().get(time16));
		assertEquals(IDLE_CPU_2, result1.getIdleCPU().get(time17));
		assertEquals(IDLE_CPU_3, result1.getIdleCPU().get(time18));
		assertEquals(IDLE_CPU_4, result1.getIdleCPU().get(time19));
		assertEquals(IDLE_CPU_5, result1.getIdleCPU().get(time20));
		assertEquals(5, result1.getUserCPU().size());
		assertEquals(USER_CPU_1, result1.getUserCPU().get(time12));
		assertEquals(USER_CPU_2, result1.getUserCPU().get(time13));
		assertEquals(USER_CPU_3, result1.getUserCPU().get(time14));
		assertEquals(USER_CPU_4, result1.getUserCPU().get(time15));
		assertEquals(USER_CPU_5, result1.getUserCPU().get(time16));
		assertEquals(5, result1.getMemory().size());
		assertEquals(MEMORY_USAGE_1, result1.getMemory().get(time7));
		assertEquals(MEMORY_USAGE_2, result1.getMemory().get(time8));
		assertEquals(MEMORY_USAGE_3, result1.getMemory().get(time9));
		assertEquals(MEMORY_USAGE_4, result1.getMemory().get(time10));
		assertEquals(MEMORY_USAGE_5, result1.getMemory().get(time11));
	}
	
	@Test
	public void testFilesWithHeaders() throws IOException {
		writeFilesWithHeaders();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
				MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		MachineUsage result1 = machine.getUsage(new Execution(time1, time20));
		
		assertEquals(5, result1.getWriteNumber().size());
		assertEquals(WRITE_NUM_1, result1.getWriteNumber().get(time1));
		assertEquals(WRITE_NUM_2, result1.getWriteNumber().get(time2));
		assertEquals(WRITE_NUM_3, result1.getWriteNumber().get(time3));
		assertEquals(WRITE_NUM_4, result1.getWriteNumber().get(time4));
		assertEquals(WRITE_NUM_5, result1.getWriteNumber().get(time5));
		assertEquals(5, result1.getWriteAttempts().size());
		assertEquals(WRITE_ATTEMPTS_1, result1.getWriteAttempts().get(time1));
		assertEquals(WRITE_ATTEMPTS_2, result1.getWriteAttempts().get(time2));
		assertEquals(WRITE_ATTEMPTS_3, result1.getWriteAttempts().get(time3));
		assertEquals(WRITE_ATTEMPTS_4, result1.getWriteAttempts().get(time4));
		assertEquals(WRITE_ATTEMPTS_5, result1.getWriteAttempts().get(time5));
		assertEquals(5, result1.getReadNumber().size());
		assertEquals(READ_NUM_1, result1.getReadNumber().get(time4));
		assertEquals(READ_NUM_2, result1.getReadNumber().get(time5));
		assertEquals(READ_NUM_3, result1.getReadNumber().get(time6));
		assertEquals(READ_NUM_4, result1.getReadNumber().get(time7));
		assertEquals(READ_NUM_5, result1.getReadNumber().get(time8));		
		assertEquals(5, result1.getReadNumber().size());
		assertEquals(READ_SECTORS_1, result1.getReadSectors().get(time4));
		assertEquals(READ_SECTORS_2, result1.getReadSectors().get(time5));
		assertEquals(READ_SECTORS_3, result1.getReadSectors().get(time6));
		assertEquals(READ_SECTORS_4, result1.getReadSectors().get(time7));
		assertEquals(READ_SECTORS_5, result1.getReadSectors().get(time8));
		assertEquals(5, result1.getIdleCPU().size());
		assertEquals(IDLE_CPU_1, result1.getIdleCPU().get(time16));
		assertEquals(IDLE_CPU_2, result1.getIdleCPU().get(time17));
		assertEquals(IDLE_CPU_3, result1.getIdleCPU().get(time18));
		assertEquals(IDLE_CPU_4, result1.getIdleCPU().get(time19));
		assertEquals(IDLE_CPU_5, result1.getIdleCPU().get(time20));
		assertEquals(5, result1.getUserCPU().size());
		assertEquals(USER_CPU_1, result1.getUserCPU().get(time12));
		assertEquals(USER_CPU_2, result1.getUserCPU().get(time13));
		assertEquals(USER_CPU_3, result1.getUserCPU().get(time14));
		assertEquals(USER_CPU_4, result1.getUserCPU().get(time15));
		assertEquals(USER_CPU_5, result1.getUserCPU().get(time16));
		assertEquals(5, result1.getMemory().size());
		assertEquals(MEMORY_USAGE_1, result1.getMemory().get(time7));
		assertEquals(MEMORY_USAGE_2, result1.getMemory().get(time8));
		assertEquals(MEMORY_USAGE_3, result1.getMemory().get(time9));
		assertEquals(MEMORY_USAGE_4, result1.getMemory().get(time10));
		assertEquals(MEMORY_USAGE_5, result1.getMemory().get(time11));
	}
	
	@Test
	public void testGetCPUDiscomfortProbabilities1() throws IOException {
		writeBasicIdleCPUInfoFile();
		writeBasicUserCPUInfoFile();
		writeBasicMemoryInfoFile();
		writeBasicReadInfoFile();
		writeBasicWriteInfoFile();
		writeBasicMemoryInfoFile();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
				MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		LinkedList<Long> discomfortTimes = new LinkedList<Long>();
		discomfortTimes.add(time1);
		discomfortTimes.add(time2);
		discomfortTimes.add(time4);
		Discomfort discomfort = new Discomfort(discomfortTimes);
		
		Map<Double, Double> result = machine.getCPUDiscomfortProbabilities(discomfort, 1, idle, hadoop);
		
		assertEquals(new Double(0.0), result.get(0.0));
		assertEquals(new Double(0.0), result.get(10.0));
		assertEquals(new Double(0.0), result.get(20.0));
		assertEquals(new Double(0.0), result.get(30.0));
		assertEquals(new Double(1.0), result.get(40.0));
		assertEquals(new Double(0.0), result.get(50.0));
		assertEquals(new Double(0.0), result.get(60.0));
		assertEquals(new Double(0.0), result.get(70.0));
		assertEquals(new Double(1.0), result.get(80.0));
		assertEquals(new Double(0.0), result.get(90.0));
		assertEquals(new Double(1.0), result.get(100.0));
	}
	
	@Test
	public void testGetCPUDiscomfortProbabilities2() throws IOException {
		writeComplexIdleCPUInfoFile();
		writeBasicUserCPUInfoFile();
		writeBasicMemoryInfoFile();
		writeBasicReadInfoFile();
		writeBasicWriteInfoFile();
		writeBasicMemoryInfoFile();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
				MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		LinkedList<Long> discomfortTimes = new LinkedList<Long>();
		discomfortTimes.add(time2);
		discomfortTimes.add(time5);
		discomfortTimes.add(time9);
		discomfortTimes.add(time14);
		Discomfort discomfort = new Discomfort(discomfortTimes);
		
		Map<Double, Double> result1 = machine.getCPUDiscomfortProbabilities(discomfort, 1, idle, hadoop);
		assertEquals(new Double(0.0), result1.get(0.0));
		assertEquals(new Double(0.0), result1.get(10.0));
		assertEquals(new Double(1/3.0), result1.get(20.0));
		assertEquals(new Double(0.0), result1.get(30.0));
		assertEquals(new Double(2/3.0), result1.get(40.0));
		assertEquals(new Double(0.0), result1.get(50.0));
		assertEquals(new Double(0.0), result1.get(60.0));
		assertEquals(new Double(0.0), result1.get(70.0));
		assertEquals(new Double(1/3.0), result1.get(80.0));
		assertEquals(new Double(0.0), result1.get(90.0));
		assertEquals(new Double(0.0), result1.get(100.0));
		
		LinkedList<Long> discomfortTimes2 = new LinkedList<Long>();
		discomfortTimes2.add(time2);
		discomfortTimes2.add(time5);
		discomfortTimes2.add(time9);
		discomfortTimes2.add(time14);
		discomfortTimes2.add(time16);
		discomfortTimes2.add(time18);
		discomfort = new Discomfort(discomfortTimes2);
		
		Map<Double, Double> result2 = machine.getCPUDiscomfortProbabilities(discomfort, 1, idle, hadoop);
		assertEquals(new Double(2/3.0), result2.get(0.0));
		assertEquals(new Double(0.0), result1.get(10.0));
		assertEquals(new Double(1/3.0), result2.get(20.0));
		assertEquals(new Double(0.0), result1.get(30.0));
		assertEquals(new Double(2/3.0), result2.get(40.0));
		assertEquals(new Double(0.0), result1.get(50.0));
		assertEquals(new Double(0.0), result2.get(60.0));
		assertEquals(new Double(0.0), result1.get(70.0));
		assertEquals(new Double(1/3.0), result2.get(80.0));
		assertEquals(new Double(0.0), result1.get(90.0));
		assertEquals(new Double(0.0), result2.get(100.0));
	}
	
	@Test
	public void testGetCPUDiscomfortProbabilities3() throws IOException {
		writeIdleCPUFileWithGaps();
		writeBasicUserCPUInfoFile();
		writeBasicMemoryInfoFile();
		writeBasicReadInfoFile();
		writeBasicWriteInfoFile();
		writeBasicMemoryInfoFile();
		
		machine = new Machine(MACHINE_NAME, IDLE_CPU_INFO_FILENAME, USER_CPU_INFO_FILENAME, 
				MEMORY_INFO_FILENAME, READ_INFO_FILENAME, WRITE_INFO_FILENAME);
		
		LinkedList<Long> discomfortTimes = new LinkedList<Long>();
		discomfortTimes.add(time2);
		discomfortTimes.add(time7);
		discomfortTimes.add(time10);
		discomfortTimes.add(time15);
		Discomfort discomfort = new Discomfort(discomfortTimes);
	
		Map<Double, Double> result1 = machine.getCPUDiscomfortProbabilities(discomfort, 1, idle, hadoop);
		assertEquals(new Double(0.5), result1.get(0.0));
		assertEquals(new Double(0.0), result1.get(10.0));
		assertEquals(new Double(0.0), result1.get(20.0));
		assertEquals(new Double(0.0), result1.get(30.0));
		assertEquals(new Double(0.5), result1.get(40.0));
		assertEquals(new Double(0.0), result1.get(50.0));
		assertEquals(new Double(0.0), result1.get(60.0));
		assertEquals(new Double(0.0), result1.get(70.0));
		assertEquals(new Double(0.0), result1.get(80.0));
		assertEquals(new Double(0.0), result1.get(90.0));
		assertEquals(new Double(2/3.0), result1.get(100.0));
	}
	
	private void writeIdleCPUFileWithGaps() throws FileNotFoundException {
		PrintStream cpuStream = new PrintStream(IDLE_CPU_INFO_FILENAME);

		cpuStream.printf("<time> %d %f\n", time1, IDLE_CPU_1);
		cpuStream.printf("<time> %d %f\n", time3, IDLE_CPU_3);
		cpuStream.printf("<time> %d %f\n", time5, IDLE_CPU_5);
		cpuStream.printf("<time> %d %f\n", time6, IDLE_CPU_1);
		cpuStream.printf("<time> %d %f\n", time8, IDLE_CPU_3);
		cpuStream.printf("<time> %d %f\n", time9, IDLE_CPU_4);
		cpuStream.printf("<time> %d %f\n", time11, IDLE_CPU_1);
		cpuStream.printf("<time> %d %f\n", time14, IDLE_CPU_6);
		cpuStream.printf("<time> %d %f\n", time17, IDLE_CPU_4);
		cpuStream.printf("<time> %d %f\n", time18, IDLE_CPU_6);
		
		cpuStream.close();
	}

	private void writeComplexIdleCPUInfoFile() throws FileNotFoundException {
		PrintStream idleCPUStream = new PrintStream(IDLE_CPU_INFO_FILENAME);

		idleCPUStream.printf("<time> %d %f\n", time1, IDLE_CPU_1);
		idleCPUStream.printf("<time> %d %f\n", time2, IDLE_CPU_2);
		idleCPUStream.printf("<time> %d %f\n", time3, IDLE_CPU_3);
		idleCPUStream.printf("<time> %d %f\n", time4, IDLE_CPU_4);
		idleCPUStream.printf("<time> %d %f\n", time5, IDLE_CPU_5);
		idleCPUStream.printf("<time> %d %f\n", time6, IDLE_CPU_1);
		idleCPUStream.printf("<time> %d %f\n", time7, IDLE_CPU_2);
		idleCPUStream.printf("<time> %d %f\n", time8, IDLE_CPU_3);
		idleCPUStream.printf("<time> %d %f\n", time9, IDLE_CPU_4);
		idleCPUStream.printf("<time> %d %f\n", time10, IDLE_CPU_5);
		idleCPUStream.printf("<time> %d %f\n", time11, IDLE_CPU_1);
		idleCPUStream.printf("<time> %d %f\n", time12, IDLE_CPU_2);
		idleCPUStream.printf("<time> %d %f\n", time13, IDLE_CPU_3);
		idleCPUStream.printf("<time> %d %f\n", time14, IDLE_CPU_4);
		idleCPUStream.printf("<time> %d %f\n", time15, IDLE_CPU_5);
		idleCPUStream.printf("<time> %d %f\n", time16, IDLE_CPU_6);
		idleCPUStream.printf("<time> %d %f\n", time17, IDLE_CPU_6);
		idleCPUStream.printf("<time> %d %f\n", time18, IDLE_CPU_6);
		
		idleCPUStream.close();
	}

	private void writeFilesWithHeaders() throws FileNotFoundException {
		PrintStream writeInfoStream = new PrintStream(WRITE_INFO_FILENAME);
		
		writeHeader(writeInfoStream);
		writeInfoStream.printf("<time> %d %f %f\n", time1, WRITE_NUM_1, WRITE_ATTEMPTS_1);
		writeInfoStream.printf("<time> %d %f %f\n", time2, WRITE_NUM_2, WRITE_ATTEMPTS_2);
		writeInfoStream.printf("<time> %d %f %f\n", time3, WRITE_NUM_3, WRITE_ATTEMPTS_3);
		writeInfoStream.printf("<time> %d %f %f\n", time4, WRITE_NUM_4, WRITE_ATTEMPTS_4);
		writeInfoStream.printf("<time> %d %f %f\n", time5, WRITE_NUM_5, WRITE_ATTEMPTS_5);
		
		writeInfoStream.close();
		
		PrintStream readInfoStream = new PrintStream(READ_INFO_FILENAME);
		
		writeHeader(readInfoStream);
		readInfoStream.printf("<time> %d %f %f\n", time4, READ_NUM_1, READ_SECTORS_1);
		readInfoStream.printf("<time> %d %f %f\n", time5, READ_NUM_2, READ_SECTORS_2);
		readInfoStream.printf("<time> %d %f %f\n", time6, READ_NUM_3, READ_SECTORS_3);
		readInfoStream.printf("<time> %d %f %f\n", time7, READ_NUM_4, READ_SECTORS_4);
		readInfoStream.printf("<time> %d %f %f\n", time8, READ_NUM_5, READ_SECTORS_5);
		
		readInfoStream.close();
		
		PrintStream memoryInfoStream = new PrintStream(MEMORY_INFO_FILENAME);
		
		writeHeader(memoryInfoStream);
		memoryInfoStream.printf("<time> %d %.2f\n", time7, MEMORY_USAGE_1);
		memoryInfoStream.printf("<time> %d %.2f\n", time8, MEMORY_USAGE_2);
		memoryInfoStream.printf("<time> %d %.2f\n", time9, MEMORY_USAGE_3);
		memoryInfoStream.printf("<time> %d %.2f\n", time10, MEMORY_USAGE_4);
		memoryInfoStream.printf("<time> %d %.2f\n", time11, MEMORY_USAGE_5);
		
		memoryInfoStream.close();
		
		PrintStream userCPUInfoStream = new PrintStream(USER_CPU_INFO_FILENAME);
		
		writeHeader(userCPUInfoStream);
		userCPUInfoStream.printf("<time> %d %.2f\n", time12, USER_CPU_1);
		userCPUInfoStream.printf("<time> %d %.2f\n", time13, USER_CPU_2);
		userCPUInfoStream.printf("<time> %d %.2f\n", time14, USER_CPU_3);
		userCPUInfoStream.printf("<time> %d %.2f\n", time15, USER_CPU_4);
		userCPUInfoStream.printf("<time> %d %.2f\n", time16, USER_CPU_5);
		
		userCPUInfoStream.close();
		
		PrintStream idleCPUInfoStream = new PrintStream(IDLE_CPU_INFO_FILENAME);
		
		writeHeader(idleCPUInfoStream);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time16, IDLE_CPU_1);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time17, IDLE_CPU_2);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time18, IDLE_CPU_3);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time19, IDLE_CPU_4);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time20, IDLE_CPU_5);
		
		idleCPUInfoStream.close();	
	}

	private void writeHeader(PrintStream printStream) {
		printStream.printf("%s\n", HEADER_1);
		printStream.printf("%s\n", HEADER_2);
		printStream.printf("%s\n", HEADER_3);
		printStream.printf("%s\n", HEADER_4);
		printStream.printf("%s\n", HEADER_5);
		printStream.printf("%s\n", HEADER_6);
	}

	private void writeDifferentTimesFiles() throws FileNotFoundException {
		PrintStream writeInfoStream = new PrintStream(WRITE_INFO_FILENAME);
		
		writeInfoStream.printf("<time> %d %f %f\n", time1, WRITE_NUM_1, WRITE_ATTEMPTS_1);
		writeInfoStream.printf("<time> %d %f %f\n", time2, WRITE_NUM_2, WRITE_ATTEMPTS_2);
		writeInfoStream.printf("<time> %d %f %f\n", time3, WRITE_NUM_3, WRITE_ATTEMPTS_3);
		writeInfoStream.printf("<time> %d %f %f\n", time4, WRITE_NUM_4, WRITE_ATTEMPTS_4);
		writeInfoStream.printf("<time> %d %f %f\n", time5, WRITE_NUM_5, WRITE_ATTEMPTS_5);
		
		writeInfoStream.close();
		
		PrintStream readInfoStream = new PrintStream(READ_INFO_FILENAME);
		
		readInfoStream.printf("<time> %d %f %f\n", time4, READ_NUM_1, READ_SECTORS_1);
		readInfoStream.printf("<time> %d %f %f\n", time5, READ_NUM_2, READ_SECTORS_2);
		readInfoStream.printf("<time> %d %f %f\n", time6, READ_NUM_3, READ_SECTORS_3);
		readInfoStream.printf("<time> %d %f %f\n", time7, READ_NUM_4, READ_SECTORS_4);
		readInfoStream.printf("<time> %d %f %f\n", time8, READ_NUM_5, READ_SECTORS_5);
		
		readInfoStream.close();
		
		PrintStream memoryInfoStream = new PrintStream(MEMORY_INFO_FILENAME);
		
		memoryInfoStream.printf("<time> %d %.2f\n", time7, MEMORY_USAGE_1);
		memoryInfoStream.printf("<time> %d %.2f\n", time8, MEMORY_USAGE_2);
		memoryInfoStream.printf("<time> %d %.2f\n", time9, MEMORY_USAGE_3);
		memoryInfoStream.printf("<time> %d %.2f\n", time10, MEMORY_USAGE_4);
		memoryInfoStream.printf("<time> %d %.2f\n", time11, MEMORY_USAGE_5);
		
		memoryInfoStream.close();
		
		PrintStream userCPUInfoStream = new PrintStream(USER_CPU_INFO_FILENAME);
		
		userCPUInfoStream.printf("<time> %d %.2f\n", time12, USER_CPU_1);
		userCPUInfoStream.printf("<time> %d %.2f\n", time13, USER_CPU_2);
		userCPUInfoStream.printf("<time> %d %.2f\n", time14, USER_CPU_3);
		userCPUInfoStream.printf("<time> %d %.2f\n", time15, USER_CPU_4);
		userCPUInfoStream.printf("<time> %d %.2f\n", time16, USER_CPU_5);
		
		userCPUInfoStream.close();
		
		PrintStream idleCPUInfoStream = new PrintStream(IDLE_CPU_INFO_FILENAME);
		
		idleCPUInfoStream.printf("<time> %d %.2f\n", time16, IDLE_CPU_1);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time17, IDLE_CPU_2);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time18, IDLE_CPU_3);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time19, IDLE_CPU_4);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time20, IDLE_CPU_5);
		
		idleCPUInfoStream.close();	
	}

	private void writeBasicWriteInfoFile() throws FileNotFoundException {
		PrintStream writeInfoStream = new PrintStream(WRITE_INFO_FILENAME);
		
		writeInfoStream.printf("<time> %d %f %f\n", time1, WRITE_NUM_1, WRITE_ATTEMPTS_1);
		writeInfoStream.printf("<time> %d %f %f\n", time2, WRITE_NUM_2, WRITE_ATTEMPTS_2);
		writeInfoStream.printf("<time> %d %f %f\n", time3, WRITE_NUM_3, WRITE_ATTEMPTS_3);
		writeInfoStream.printf("<time> %d %f %f\n", time4, WRITE_NUM_4, WRITE_ATTEMPTS_4);
		writeInfoStream.printf("<time> %d %f %f\n", time5, WRITE_NUM_5, WRITE_ATTEMPTS_5);
		
		writeInfoStream.close();
	}

	private void writeBasicReadInfoFile() throws FileNotFoundException {
		PrintStream readInfoStream = new PrintStream(READ_INFO_FILENAME);
		
		readInfoStream.printf("<time> %d %f %f\n", time1, READ_NUM_1, READ_SECTORS_1);
		readInfoStream.printf("<time> %d %f %f\n", time2, READ_NUM_2, READ_SECTORS_2);
		readInfoStream.printf("<time> %d %f %f\n", time3, READ_NUM_3, READ_SECTORS_3);
		readInfoStream.printf("<time> %d %f %f\n", time4, READ_NUM_4, READ_SECTORS_4);
		readInfoStream.printf("<time> %d %f %f\n", time5, READ_NUM_5, READ_SECTORS_5);
		
		readInfoStream.close();
	}

	private void writeBasicMemoryInfoFile() throws FileNotFoundException {
		PrintStream memoryInfoStream = new PrintStream(MEMORY_INFO_FILENAME);
		
		memoryInfoStream.printf("<time> %d %.2f\n", time1, MEMORY_USAGE_1);
		memoryInfoStream.printf("<time> %d %.2f\n", time2, MEMORY_USAGE_2);
		memoryInfoStream.printf("<time> %d %.2f\n", time3, MEMORY_USAGE_3);
		memoryInfoStream.printf("<time> %d %.2f\n", time4, MEMORY_USAGE_4);
		memoryInfoStream.printf("<time> %d %.2f\n", time5, MEMORY_USAGE_5);
		
		memoryInfoStream.close();
	}

	private void writeBasicUserCPUInfoFile() throws FileNotFoundException {
		PrintStream userCPUInfoStream = new PrintStream(USER_CPU_INFO_FILENAME);
		
		userCPUInfoStream.printf("<time> %d %.2f\n", time1, USER_CPU_1);
		userCPUInfoStream.printf("<time> %d %.2f\n", time2, USER_CPU_2);
		userCPUInfoStream.printf("<time> %d %.2f\n", time3, USER_CPU_3);
		userCPUInfoStream.printf("<time> %d %.2f\n", time4, USER_CPU_4);
		userCPUInfoStream.printf("<time> %d %.2f\n", time5, USER_CPU_5);
		
		userCPUInfoStream.close();
	}

	private void writeBasicIdleCPUInfoFile() throws FileNotFoundException {
		PrintStream idleCPUInfoStream = new PrintStream(IDLE_CPU_INFO_FILENAME);
		
		idleCPUInfoStream.printf("<time> %d %.2f\n", time1, IDLE_CPU_1);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time2, IDLE_CPU_2);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time3, IDLE_CPU_3);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time4, IDLE_CPU_4);
		idleCPUInfoStream.printf("<time> %d %.2f\n", time5, IDLE_CPU_5);
		
		idleCPUInfoStream.close();	
	}
}
