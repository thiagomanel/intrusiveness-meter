package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import analysis.data.Execution;
import analysis.data.MachineUsage;

import commons.util.LogFile;

public class Machine {
	private MachineUsage usage;

	public Machine(String machineName, String idleCpuInfoFilename,
			String userCpuInfoFilename, String memoryInfoFilename,
			String readInfoFilename, String writeInfoFilename) throws IOException {
		checkNotNull(machineName, "machineName must not be null.");
		checkNotNull(idleCpuInfoFilename, "idleCpuInfoFilename must not be null.");
		checkNotNull(userCpuInfoFilename, "userCpuInfoFilename must not be null.");
		checkNotNull(memoryInfoFilename, "memoryInfoFilename must not be null.");
		checkNotNull(readInfoFilename, "readInfoFilename must not be null.");
		checkNotNull(writeInfoFilename, "writeInfoFilename must not be null.");
		
		usage = new MachineUsage(getIdleCpu(idleCpuInfoFilename), getUserCPU(userCpuInfoFilename), 
				getMemoryUsage(memoryInfoFilename), getReadNumber(readInfoFilename),
				getReadSectors(readInfoFilename), getWriteNumber(writeInfoFilename), 
				getWriteAttemptNumber(writeInfoFilename));
	}

	private Map<Long, Long> getLongLongMapFromFile(String filename, int indexFromMessage) throws IOException {
		LogFile file = new LogFile(filename);
		Map<Long, Long> map = new HashMap<Long, Long>();
		
		do {
			map.put(file.getLineTime(), Long.parseLong(file.getMessage().split(" ")[indexFromMessage]));
			file.advance();
		} while (!file.reachedEnd());
		
		return map;	
	}
	
	private Map<Long, Double> getLongDoubleMapFromFile(String filename, int indexFromMessage) throws IOException {
		LogFile file = new LogFile(filename);
		Map<Long, Double> map = new HashMap<Long, Double>();
		
		do {
			map.put(file.getLineTime(), Double.parseDouble(file.getMessage().split(" ")[indexFromMessage]));
			file.advance();
		} while (!file.reachedEnd());
		
		return map;	
	}
	
	private Map<Long, Long> getWriteAttemptNumber(String writeInfoFilename) throws IOException {
		return getLongLongMapFromFile(writeInfoFilename, 1);
	}

	private Map<Long, Long> getWriteNumber(String writeInfoFilename) throws IOException {
		return getLongLongMapFromFile(writeInfoFilename, 0);
	}

	private Map<Long, Long> getReadSectors(String readInfoFilename) throws NumberFormatException, IOException {
		return getLongLongMapFromFile(readInfoFilename, 1);
	}

	private Map<Long, Long> getReadNumber(String readInfoFilename) throws IOException {
		return getLongLongMapFromFile(readInfoFilename, 0);
	}

	private Map<Long, Double> getMemoryUsage(String memoryInfoFilename) throws IOException {
		return getLongDoubleMapFromFile(memoryInfoFilename, 0);
	}

	private Map<Long, Double> getUserCPU(String userCpuInfoFilename) throws NumberFormatException, IOException {
		return getLongDoubleMapFromFile(userCpuInfoFilename, 0);
	}

	private Map<Long, Double> getIdleCpu(String idleCpuInfoFilename) throws IOException {
		return getLongDoubleMapFromFile(idleCpuInfoFilename, 0);
	}

	public MachineUsage getUsage(Execution execution) {
		checkNotNull(execution, "execution must not be null.");
		Map<Long, Long> newWriteNumber = new HashMap<Long, Long>();
		Map<Long, Long> newWriteAttempt = new HashMap<Long, Long>();
		for (Long time : usage.getWriteNumber().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newWriteNumber.put(time, usage.getWriteNumber().get(time));
				newWriteAttempt.put(time, usage.getWriteAttempts().get(time));
			}
		}
		
		Map<Long, Long> newReadNumber = new HashMap<Long, Long>();
		Map<Long, Long> newReadSectors = new HashMap<Long, Long>();
		for (Long time : usage.getReadNumber().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newReadNumber.put(time, usage.getReadNumber().get(time));
				newReadSectors.put(time, usage.getReadSectors().get(time));
			}
		}
		
		return new MachineUsage(getNewIdleCPU(execution), getNewUserCPU(execution), getNewMemory(execution), 
				newReadNumber, newReadSectors, newWriteNumber, newWriteAttempt);
	}

	private Map<Long, Double> getNewMemory(Execution execution) {
		Map<Long, Double> newMemory = new HashMap<Long, Double>();
		for (Long time : usage.getMemory().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newMemory.put(time, usage.getMemory().get(time));
			}
		}
		return newMemory;
	}

	private Map<Long, Double> getNewUserCPU(Execution execution) {
		Map<Long, Double> newUserCPU = new HashMap<Long, Double>();
		for (Long time : usage.getUserCPU().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newUserCPU.put(time, usage.getUserCPU().get(time));
			}
		}
		return newUserCPU;
	}

	private Map<Long, Double> getNewIdleCPU(Execution execution) {
		Map<Long, Double> newIdleCPU = new HashMap<Long, Double>();
		for (Long time : usage.getIdleCPU().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newIdleCPU.put(time, usage.getIdleCPU().get(time));
			}
		}
		return newIdleCPU;
	}
}