package analysis;

import static commons.Preconditions.checkNotNull;
import static commons.Preconditions.checkFileExists;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

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
	public void variationDegree(String inputFileName, String outputFileName, double ratio) throws IOException {
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

	private void writeToFile(double variationDegree,
			RandomAccessFile outputFileWriter) throws IOException {	
		outputFileWriter.writeBytes(String.valueOf(variationDegree));
		outputFileWriter.writeChar('\n');
	}

	private double calculateVariationDegree(double number1, double number2,
			double ratio) {
		return ratio == 0 ? 0 : (number2 - number1)/ratio;
	}

	private double getDouble(String firstLine) throws IOException {
		double result;
		
		try {
			result = Double.parseDouble(firstLine.substring(0, firstLine.length() - 1));			
		} catch (NumberFormatException e) {
			throw new IOException("Invalid data format.");
		}
		
		return result;
	}
}
