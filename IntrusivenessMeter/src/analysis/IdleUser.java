package analysis;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;

import commons.util.LogFile;

public class IdleUser {

	private LogFile idleTimeLogFile;
	private Map<Long, Long> idleTimes;
	private long activityThreshold;
	
	public IdleUser(Map<Long, Long> idleTimes) {
		this.idleTimes = idleTimes;
	}
	
	public IdleUser(String idleTimeLogFileName, long activityThreshold) throws IOException {
		idleTimeLogFile = new LogFile(idleTimeLogFileName);
		idleTimes = new TreeMap<Long, Long>();
		this.activityThreshold = activityThreshold;
		
		do {
			long time = idleTimeLogFile.getLineTime();
			long idleTime = Long.parseLong(idleTimeLogFile.getMessage());
			idleTimes.put(time, idleTime);
			advanceFile();
		} while (!idleTimeLogFile.reachedEnd());
	}

	private void advanceFile() {
		try {
			idleTimeLogFile.advance();
		} catch (IOException e) {
			
		}
	}

	public boolean idle(Execution execution) {
		for (Long time : idleTimes.keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				if (idleTimes.get(time) < activityThreshold) {
					return false;
				}
			}
		}
		return true;
	}
}