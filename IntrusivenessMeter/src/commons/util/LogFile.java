package commons.util;

import java.io.IOException;
import java.io.RandomAccessFile;

import static commons.util.StringUtil.concat;

public class LogFile {
	
	private static final int MESSAGE_START_INDEX = 2;
	private static final int TIME_INDEX = 1;
	
	private RandomAccessFile file;
	private String currentMessage;
	private long lineTime;
	
	public LogFile(String logFileName) throws IOException {
		file = new RandomAccessFile(logFileName, "r");
		currentMessage = null;
		lineTime = 0;

		advance();
	}
	
	public long getLineTime() {
		return lineTime;
	}
	
	public boolean advance() throws IOException {
		String line = file.readLine();
		if (line == null) {
			return false;
		}
		
		String[] tokens = line.split("\\s+");
		lineTime = Long.parseLong(tokens[TIME_INDEX]);
		currentMessage = concat(" ", tokens, MESSAGE_START_INDEX, tokens.length - 1);
		
		return true;
	}

	public String getMessage() {
		return currentMessage;
	}
	
	public void close() throws IOException {
		file.close();
	}
}
