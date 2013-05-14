package analysis;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RawDataProcessingTest {

	private static final String testInputFileName = "input.txt";
	private static final String testOutputFileName = "output.txt";
	
	private static final double number1 = 1;
	private static final double number2 = 12;
	private static final double number3 = 36;
	private static final double number4 = 40;
	
	private static final double limitError = 0.005;
	
	private RawDataProcessing dataProcessing;
	
	@Before
	public void setUp() throws IOException {
		new File(testInputFileName).createNewFile();
		new File(testOutputFileName).createNewFile();
		
		dataProcessing = new RawDataProcessing();
	}
	
	@After
	public void tearDown() {
		new File(testInputFileName).delete();
		new File(testOutputFileName).delete();
	}

	@Test
	public void testVariationDegree() throws IOException {
		writeBasicInputFile();
		
		dataProcessing.variationDegree(testInputFileName, testOutputFileName, 2);
		
		RandomAccessFile f = new RandomAccessFile(testOutputFileName, "r");
		
		assertEquals((number2 - number1)/2, Double.parseDouble(f.readLine()), limitError);
		assertEquals((number3 - number2)/2, Double.parseDouble(f.readLine()), limitError);
		assertEquals((number4 - number3)/2, Double.parseDouble(f.readLine()), limitError);
		
		f.close();
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
