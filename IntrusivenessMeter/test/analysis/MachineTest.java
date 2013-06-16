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
import analysis.data.MachineUsage;

public class MachineTest {

	private static final String IDLE_CPU_INFO_FILENAME = "idlecpu_test.log";
	private static final String USER_CPU_INFO_FILENAME = "usercpu_test.log";
	private static final String MEMORY_INFO_FILENAME = "mem_test.log";
	private static final String READ_INFO_FILENAME = "read_test.log";
	private static final String WRITE_INFO_FILENAME = "write_test.log";
		
	private static final Long WRITE_ATTEMPTS_1 = 500L;
	private static final Long WRITE_ATTEMPTS_2 = WRITE_ATTEMPTS_1 + 100;
	private static final Long WRITE_ATTEMPTS_3 = WRITE_ATTEMPTS_2 + 100;
	private static final Long WRITE_ATTEMPTS_4 = WRITE_ATTEMPTS_3 + 100;
	private static final Long WRITE_ATTEMPTS_5 = WRITE_ATTEMPTS_4 + 100;
	
	private static final Long WRITE_NUM_1 = 300L;
	private static final Long WRITE_NUM_2 = WRITE_NUM_1 + 100L;
	private static final Long WRITE_NUM_3 = WRITE_NUM_2 + 100L;
	private static final Long WRITE_NUM_4 = WRITE_NUM_3 + 100L;
	private static final Long WRITE_NUM_5 = WRITE_NUM_4 + 100L;
	
	private static final Long READ_NUM_1 = 900L;
	private static final Long READ_NUM_2 = READ_NUM_1 + 100;
	private static final Long READ_NUM_3 = READ_NUM_2 + 100;
	private static final Long READ_NUM_4 = READ_NUM_3 + 100;
	private static final Long READ_NUM_5 = READ_NUM_4 + 100;
	
	private static final Long READ_SECTORS_1 = 900L;
	private static final Long READ_SECTORS_2 = READ_SECTORS_1 + 100;
	private static final Long READ_SECTORS_3 = READ_SECTORS_2 + 100;
	private static final Long READ_SECTORS_4 = READ_SECTORS_3 + 100;
	private static final Long READ_SECTORS_5 = READ_SECTORS_4 + 100;
	
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
	
	private static final Double USER_CPU_1 = 0.0;
	private static final Double USER_CPU_2 = USER_CPU_1 + 20;
	private static final Double USER_CPU_3 = USER_CPU_2 + 20;
	private static final Double USER_CPU_4 = USER_CPU_3 + 20;
	private static final Double USER_CPU_5 = USER_CPU_4 + 20;
	
	private static long time1 = 1000;
	private static long time2 = time1 + 1;
	private static long time3 = time2 + 1;
	private static long time4 = time3 + 1;
	private static long time5 = time4 + 1;
	
	private static final String MACHINE_NAME = "localhost";
	private Machine machine;
	
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

	private void writeBasicWriteInfoFile() throws FileNotFoundException {
		PrintStream writeInfoStream = new PrintStream(WRITE_INFO_FILENAME);
		
		writeInfoStream.printf("<time> %d %d %d\n", time1, WRITE_NUM_1, WRITE_ATTEMPTS_1);
		writeInfoStream.printf("<time> %d %d %d\n", time2, WRITE_NUM_2, WRITE_ATTEMPTS_2);
		writeInfoStream.printf("<time> %d %d %d\n", time3, WRITE_NUM_3, WRITE_ATTEMPTS_3);
		writeInfoStream.printf("<time> %d %d %d\n", time4, WRITE_NUM_4, WRITE_ATTEMPTS_4);
		writeInfoStream.printf("<time> %d %d %d\n", time5, WRITE_NUM_5, WRITE_ATTEMPTS_5);
		
		writeInfoStream.close();
	}

	private void writeBasicReadInfoFile() throws FileNotFoundException {
		PrintStream readInfoStream = new PrintStream(READ_INFO_FILENAME);
		
		readInfoStream.printf("<time> %d %d %d\n", time1, READ_NUM_1, READ_SECTORS_1);
		readInfoStream.printf("<time> %d %d %d\n", time2, READ_NUM_2, READ_SECTORS_2);
		readInfoStream.printf("<time> %d %d %d\n", time3, READ_NUM_3, READ_SECTORS_3);
		readInfoStream.printf("<time> %d %d %d\n", time4, READ_NUM_4, READ_SECTORS_4);
		readInfoStream.printf("<time> %d %d %d\n", time5, READ_NUM_5, READ_SECTORS_5);
		
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
