package analysis;

import static commons.Preconditions.checkFileExists;
import static commons.Preconditions.checkNotNull;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeSet;

import analysis.data.MachineUsage;

public class RawDataProcessing {
	
	/**
	 * This method calculates the variation degree of a sequence of numbers stored in a file 
	 * and puts the generated sequence in other file. A variation degree g of an array a, a = {a1, a2, a3, ..., an}, 
	 * given the ratio r, is defined as:
	 * 
	 * g = {(a2 - a1)/r, (a3 - a2/r), ... (an - a(n-1))/r}
	 * 
	 * @param inputFileName The name of the file used as input by the method. The name must 
	 * not be null. Each line of the file must contain one number.
	 * @param outputFileName The name of the file used as output by the method. If the file does 
	 * not exist, the file is created.
	 * @param ratio The ratio used to calculate the variationDegree.
	 * @throws IOException If the input file does not exist, if an invalid number is read from 
	 * the input file or there is other type of error while reading or writing to the files
	 * @throws IllegalArgumentException If inputFileName or outputFileName is null.
	 */
	public static void variationDegree(String inputFileName, String outputFileName, double ratio) throws IOException {
		checkNotNull(inputFileName, "inputFileName must not be null.");
		checkNotNull(outputFileName, "outputFileName must not be null.");
		
		checkFileExists(inputFileName, "inputFileName must exist.");
		
		File outputFile = new File(outputFileName);
		outputFile.createNewFile();
		
		RandomAccessFile inputFileReader = new RandomAccessFile(new File(inputFileName), "rw");
		RandomAccessFile outputFileWriter = new RandomAccessFile(outputFile, "rw");
		
		String firstLine = inputFileReader.readLine();
		String secondLine = null;
		
		while ((secondLine = inputFileReader.readLine()) != null) {
			double number1 = getDouble(firstLine);
			double number2 = getDouble(secondLine);
			double variationDegree = calculateVariationDegree(number1, number2, ratio);
			writeToFile(variationDegree, outputFileWriter);
			firstLine = secondLine;
		}
		
		inputFileReader.close();
		outputFileWriter.close();
	}

	private static void writeToFile(double variationDegree,
			RandomAccessFile outputFileWriter) throws IOException {	
		outputFileWriter.writeBytes(String.valueOf(variationDegree));
		outputFileWriter.writeChar('\n');
	}

	private static double calculateVariationDegree(double number1, double number2,
			double ratio) {
		return ratio == 0 ? 0 : (number2 - number1)/ratio;
	}

	
	
	private static double getDouble(String firstLine) throws IOException {
		double result;
		
		try {
			result = Double.parseDouble(firstLine.substring(0, firstLine.length() - 1));			
		} catch (NumberFormatException e) {
			throw new IOException("Invalid data format.");
		}
		
		return result;
	}
	
	// FIXME Improve this code
	public static MachineUsage getVariationDegree(MachineUsage usage) {
		Map<Long, Double> newReadNumber = new HashMap<Long, Double>();
		Map<Long, Double> newReadSectors = new HashMap<Long, Double>();
		Map<Long, Double> newWriteNumber = new HashMap<Long, Double>();
		Map<Long, Double> newWriteAttemptNumber = new HashMap<Long, Double>();
		
		TreeSet<Long> readNumberTimes = new TreeSet<Long>(usage.getReadNumber().keySet());
		
		Iterator<Long> iteratorReadNumberTimes = readNumberTimes.iterator();
		Long timeReadNumber = iteratorReadNumberTimes.next();
		newReadNumber.put(timeReadNumber, 0.0);
		newReadSectors.put(timeReadNumber, 0.0);
		while (iteratorReadNumberTimes.hasNext()) {
			Long next = iteratorReadNumberTimes.next();
			newReadNumber.put(next, (usage.getReadNumber().get(next) - usage.getReadNumber().get(timeReadNumber))/(next - timeReadNumber));
			newReadSectors.put(next, (usage.getReadSectors().get(next) - usage.getReadSectors().get(timeReadNumber))/(next - timeReadNumber));
			timeReadNumber = next;			
		}
		
		TreeSet<Long> writeNumberTimes = new TreeSet<Long>(usage.getWriteNumber().keySet());
		
		Iterator<Long> iteratorWriteNumberTimes = writeNumberTimes.iterator();
		Long timeWriteNumber = iteratorWriteNumberTimes.next();
		newWriteNumber.put(timeWriteNumber, 0.0);
		newWriteAttemptNumber.put(timeWriteNumber, 0.0);
		while (iteratorWriteNumberTimes.hasNext()) {
			Long next = iteratorWriteNumberTimes.next();
			newWriteNumber.put(next, (usage.getWriteNumber().get(next) - usage.getWriteNumber().get(timeWriteNumber))/(next - timeWriteNumber));
			newWriteAttemptNumber.put(next, (usage.getWriteAttempts().get(next) - usage.getWriteAttempts().get(timeWriteNumber))/(next - timeWriteNumber));
			timeWriteNumber = next;			
		}
		
		return new MachineUsage(usage.getIdleCPU(), usage.getUserCPU(), usage.getMemory(), 
					newReadNumber, newReadSectors, newWriteNumber, newWriteAttemptNumber);
	}
}
