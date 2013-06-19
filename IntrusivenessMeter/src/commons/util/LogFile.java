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
	private String currentLine;
	
	public LogFile(String logFileName) throws IOException {
		file = new RandomAccessFile(logFileName, "r");
		currentMessage = null;
		lineTime = 0;
		currentLine = null;
		
		advance();
	}
	
	public long getLineTime() {
		return lineTime;
	}

	public void advance() throws IOException {
		currentLine = file.readLine();
		
		if (currentLine != null) {
			String[] tokens = currentLine.split("\\s+");
			if (tokens.length < 2) {
				throw new IOException("Not data file");
			}
			
			try {
				lineTime = Long.parseLong(tokens[TIME_INDEX]);
				getMessageFromTokens(tokens);
			} catch (NumberFormatException e) {
				// FIXME should change in other lines too.
				currentMessage = null;
				lineTime = -1;
				throw new IOException("Not data line");
			}
		}
	}
	
	public boolean reachedEnd() {
		return currentLine == null;
	}
	
	private void getMessageFromTokens(String[] tokens) {
		if (tokens.length > 2) {
			currentMessage = concat(" ", tokens, MESSAGE_START_INDEX, tokens.length - 1);			
		} else {
			currentMessage = null;
		}
	}

	public String getMessage() {
		return currentMessage;
	}
	
	public void close() throws IOException {
		file.close();
	}
}
