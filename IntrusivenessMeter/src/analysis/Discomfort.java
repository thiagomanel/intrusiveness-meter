package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import analysis.data.Execution;

import commons.util.LogFile;

public class Discomfort {

	private LogFile discomfortFile;
	private List<Long> discomfortReportsTimes;
	
	public Discomfort(String discomfortLogFileName) throws IOException {
		checkNotNull(discomfortLogFileName, "discomfortLogFileName must not be null.");
		
		discomfortFile = new LogFile(discomfortLogFileName);
		discomfortReportsTimes = new LinkedList<Long>();
		
		do {
			discomfortReportsTimes.add(discomfortFile.getLineTime());
			advanceFile();
		} while (!discomfortFile.reachedEnd());
	}

	public Discomfort(List<Long> discomfortReportTimes) {
		checkNotNull(discomfortReportTimes, "discomfortReportTimes must not be null.");
		this.discomfortReportsTimes = discomfortReportTimes;
	}

	private void advanceFile() {
		try {
			discomfortFile.advance();
		} catch (IOException e) {
			
		}
	}

	public boolean reportedDiscomfort(Execution execution) {
		for (Long discomfortTime : discomfortReportsTimes) {
			if (execution.getStartTime() <= discomfortTime && execution.getFinishTime() >= discomfortTime) {
				return true;
			}
		}
		return false;
	}

	public List<Long> getDiscomfortTimes(Execution execution) {
		List<Long> discomfortTimes = new LinkedList<Long>();
		
		for (Long discomfortTime : discomfortReportsTimes) {
			if (execution.getStartTime() <= discomfortTime && execution.getFinishTime() >= discomfortTime) {
				discomfortTimes.add(discomfortTime);
			}
		}
		
		return discomfortTimes;
	}
}
