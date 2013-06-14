package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
		} while (file.advance());
		
		return map;	
	}
	
	private Map<Long, Double> getLongDoubleMapFromFile(String filename, int indexFromMessage) throws IOException {
		LogFile file = new LogFile(filename);
		Map<Long, Double> map = new HashMap<Long, Double>();
		
		do {
			map.put(file.getLineTime(), Double.parseDouble(file.getMessage().split(" ")[indexFromMessage]));
		} while (file.advance());
		
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
		Map<Long, Long> newWriteNumber = new HashMap<Long, Long>();
		Map<Long, Long> newWriteAttempt = new HashMap<Long, Long>();
		Map<Long, Long> newReadNumber = new HashMap<Long, Long>();
		Map<Long, Double> newIdleCPU = new HashMap<Long, Double>();
		Map<Long, Double> newUserCPU = new HashMap<Long, Double>();
		Map<Long, Long> newReadSectors = new HashMap<Long, Long>();
		Map<Long, Double> newReadMemory = new HashMap<Long, Double>();
		
		for (Long key : usage.getWriteNumber().keySet()) {
			if (execution.getStartTime() <= key && key <= execution.getFinishTime()) {
				newWriteNumber.put(key, usage.getWriteNumber().get(key));
				newReadNumber.put(key, usage.getReadNumber().get(key));
				newIdleCPU.put(key, usage.getIdleCPU().get(key));
				newUserCPU.put(key, usage.getUserCPU().get(key));
				newWriteAttempt.put(key, usage.getWriteAttempts().get(key));
				newReadSectors.put(key, usage.getReadSectors().get(key));
				newReadMemory.put(key, usage.getMemory().get(key));
			}
		}
		
		return new MachineUsage(newIdleCPU, newUserCPU, newReadMemory, newReadNumber, 
					newReadSectors, newWriteNumber, newWriteAttempt);
	}
}