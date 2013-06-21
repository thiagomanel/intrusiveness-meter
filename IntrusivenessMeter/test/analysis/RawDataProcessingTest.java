package analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import analysis.data.MachineUsage;

public class RawDataProcessingTest {

	private static final String testInputFileName = "input.txt";
	private static final String testOutputFileName = "output.txt";
	
	private static final double number1 = 1;
	private static final double number2 = 12;
	private static final double number3 = 36;
	private static final double number4 = 40;
	
	private static final double limitError = 0.005;
	private static final Long time1 = 1L;
	private static final Long time2 = time1 + 1;
	private static final Long time3 = time2 + 1;
	private static final Long time4 = time3 + 1;
	private static final Long time5 = time4 + 1;
	private static final Long READ_NUMBER_1 = 12L;
	private static final Long READ_NUMBER_2 = 3L;
	private static final Long READ_NUMBER_3 = 1L;
	private static final Long READ_NUMBER_4 = 2L;
	private static final Long READ_NUMBER_5 = 7L;
	private static final Long READ_SECTORS_1 = 2L;
	private static final Long READ_SECTORS_2 = 1L;
	private static final Long READ_SECTORS_3 = 5L;
	private static final Long READ_SECTORS_4 = 1000L;
	private static final Long READ_SECTORS_5 = 2L;
	private static final Long WRITE_NUMBER_1 = 123L;
	private static final Long WRITE_NUMBER_2 = 11L;
	private static final Long WRITE_NUMBER_3 = 2L;
	private static final Long WRITE_NUMBER_4 = 34L;
	private static final Long WRITE_NUMBER_5 = 12314L;
	private static final Long WRITE_ATTEMPT_NUMBER_1 = 22L;
	private static final Long WRITE_ATTEMPT_NUMBER_2 = 33L;
	private static final Long WRITE_ATTEMPT_NUMBER_3 = 1213L;
	private static final Long WRITE_ATTEMPT_NUMBER_4 = 21L;
	private static final Long WRITE_ATTEMPT_NUMBER_5 = 89L;
	
	@Before
	public void setUp() throws IOException {
		new File(testInputFileName).createNewFile();
		new File(testOutputFileName).createNewFile();
	}
	
	@After
	public void tearDown() {
		new File(testInputFileName).delete();
		new File(testOutputFileName).delete();
	}

	@Test
	public void testVariationDegree() throws IOException {
		writeBasicInputFile();
		
		RawDataProcessing.variationDegree(testInputFileName, testOutputFileName, 2);
		
		RandomAccessFile f = new RandomAccessFile(testOutputFileName, "r");
		
		assertEquals((number2 - number1)/2, Double.parseDouble(f.readLine()), limitError);
		assertEquals((number3 - number2)/2, Double.parseDouble(f.readLine()), limitError);
		assertEquals((number4 - number3)/2, Double.parseDouble(f.readLine()), limitError);
		
		f.close();
	}

	@Test
	public void testVariationDegreeMachineUsage() {
		Map<Long, Double> idleCPU = new HashMap<Long, Double>();
		Map<Long, Double> userCPU = new HashMap<Long, Double>();
		Map<Long, Double> memoryUsage = new HashMap<Long, Double>();
		Map<Long, Double> readNumber = new HashMap<Long, Double>();
		Map<Long, Double> readSectors = new HashMap<Long, Double>();
		Map<Long, Double> writeNumber = new HashMap<Long, Double>();
		Map<Long, Double> writeAttemptNumber = new HashMap<Long, Double>();
		
		readNumber.put(time1, new Double(READ_NUMBER_1));
		readNumber.put(time2, new Double(READ_NUMBER_2));
		readNumber.put(time3, new Double(READ_NUMBER_3));
		readNumber.put(time4, new Double(READ_NUMBER_4));
		readNumber.put(time5, new Double(READ_NUMBER_5));
		
		readSectors.put(time1, new Double(READ_SECTORS_1));
		readSectors.put(time2, new Double(READ_SECTORS_2));
		readSectors.put(time3, new Double(READ_SECTORS_3));
		readSectors.put(time4, new Double(READ_SECTORS_4));
		readSectors.put(time5, new Double(READ_SECTORS_5));
		
		writeNumber.put(time1, new Double(WRITE_NUMBER_1));
		writeNumber.put(time2, new Double(WRITE_NUMBER_2));
		writeNumber.put(time3, new Double(WRITE_NUMBER_3));
		writeNumber.put(time4, new Double(WRITE_NUMBER_4));
		writeNumber.put(time5, new Double(WRITE_NUMBER_5));
		
		writeAttemptNumber.put(time1, new Double(WRITE_ATTEMPT_NUMBER_1));
		writeAttemptNumber.put(time2, new Double(WRITE_ATTEMPT_NUMBER_2));
		writeAttemptNumber.put(time3, new Double(WRITE_ATTEMPT_NUMBER_3));
		writeAttemptNumber.put(time4, new Double(WRITE_ATTEMPT_NUMBER_4));
		writeAttemptNumber.put(time5, new Double(WRITE_ATTEMPT_NUMBER_5));
		
		MachineUsage usage = new MachineUsage(idleCPU, userCPU, memoryUsage, readNumber, readSectors, writeNumber, writeAttemptNumber);
		
		MachineUsage result = RawDataProcessing.getVariationDegree(usage);
		
		assertEquals(5, result.getReadNumber().size());
		assertEquals(new Double(0.0), result.getReadNumber().get(time1));
		assertEquals(new Double((READ_NUMBER_2 - READ_NUMBER_1)/(time2 - time1)), result.getReadNumber().get(time2));
		assertEquals(new Double((READ_NUMBER_3 - READ_NUMBER_2)/(time3 - time2)), result.getReadNumber().get(time3));
		assertEquals(new Double((READ_NUMBER_4 - READ_NUMBER_3)/(time4 - time3)), result.getReadNumber().get(time4));
		assertEquals(new Double((READ_NUMBER_5 - READ_NUMBER_4)/(time5 - time4)), result.getReadNumber().get(time5));
		
		assertEquals(5, result.getReadSectors().size());
		assertEquals(new Double(0.0), result.getReadSectors().get(time1));
		assertEquals(new Double((READ_SECTORS_2 - READ_SECTORS_1)/(time2 - time1)), result.getReadSectors().get(time2));
		assertEquals(new Double((READ_SECTORS_3 - READ_SECTORS_2)/(time3 - time2)), result.getReadSectors().get(time3));
		assertEquals(new Double((READ_SECTORS_4 - READ_SECTORS_3)/(time4 - time3)), result.getReadSectors().get(time4));
		assertEquals(new Double((READ_SECTORS_5 - READ_SECTORS_4)/(time5 - time4)), result.getReadSectors().get(time5));
		
		assertEquals(5, result.getWriteNumber().size());
		assertEquals(new Double(0.0), result.getWriteNumber().get(time1));
		assertEquals(new Double((WRITE_NUMBER_2 - WRITE_NUMBER_1)/(time2 - time1)), result.getWriteNumber().get(time2));
		assertEquals(new Double((WRITE_NUMBER_3 - WRITE_NUMBER_2)/(time3 - time2)), result.getWriteNumber().get(time3));
		assertEquals(new Double((WRITE_NUMBER_4 - WRITE_NUMBER_3)/(time4 - time3)), result.getWriteNumber().get(time4));
		assertEquals(new Double((WRITE_NUMBER_5 - WRITE_NUMBER_4)/(time5 - time4)), result.getWriteNumber().get(time5));
		
		assertEquals(5, result.getWriteAttempts().size());
		assertEquals(new Double(0.0), result.getWriteAttempts().get(time1));
		assertEquals(new Double((WRITE_ATTEMPT_NUMBER_2 - WRITE_ATTEMPT_NUMBER_1)/(time2 - time1)), result.getWriteAttempts().get(time2));
		assertEquals(new Double((WRITE_ATTEMPT_NUMBER_3 - WRITE_ATTEMPT_NUMBER_2)/(time3 - time2)), result.getWriteAttempts().get(time3));
		assertEquals(new Double((WRITE_ATTEMPT_NUMBER_4 - WRITE_ATTEMPT_NUMBER_3)/(time4 - time3)), result.getWriteAttempts().get(time4));
		assertEquals(new Double((WRITE_ATTEMPT_NUMBER_5 - WRITE_ATTEMPT_NUMBER_4)/(time5 - time4)), result.getWriteAttempts().get(time5));
	}
	
	private void writeBasicInputFile() throws IOException {
		RandomAccessFile f = new RandomAccessFile(testInputFileName, "rw");
		
		f.writeBytes(String.valueOf(number1));
		f.writeChar('\n');
		f.writeBytes(String.valueOf(number2));
		f.writeChar('\n');
		f.writeBytes(String.valueOf(number3));
		f.writeChar('\n');
		f.writeBytes(String.valueOf(number4));
		f.writeChar('\n');
		
		f.seek(0);
		
		f.close();
	}
}
