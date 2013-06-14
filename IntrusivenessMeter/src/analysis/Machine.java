package analysis;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import commons.util.LogFile;

public class Machine {

	private MachineUsage usage;
	
	public Machine(String machine) {
		// TODO Auto-generated constructor stub
	}

	public Machine(String machineName, String idleCpuInfoFilename,
			String userCpuInfoFilename, String memoryInfoFilename,
			String readInfoFilename, String writeInfoFilename) throws IOException {
		
		usage = new MachineUsage(getIdleCpu(idleCpuInfoFilename), getUserCPU(userCpuInfoFilename), 
				getMemoryUsage(memoryInfoFilename), getReadNumber(readInfoFilename),
				getReadSectors(readInfoFilename), getWriteNumber(writeInfoFilename), 
				getWriteAttemptNumber(writeInfoFilename));
	}

	private Map<Long, Long> getWriteAttemptNumber(String writeInfoFilename) throws IOException {
		LogFile file = new LogFile(writeInfoFilename);
		Map<Long, Long> writeAttempt = new HashMap<Long, Long>();
		
		do {
			writeAttempt.put(file.getLineTime(), Long.parseLong(file.getMessage().split(" ")[1]));
		} while (file.advance());
		
		return writeAttempt;	
	}

	private Map<Long, Long> getWriteNumber(String writeInfoFilename) throws IOException {
		LogFile file = new LogFile(writeInfoFilename);
		Map<Long, Long> writeNumber = new HashMap<Long, Long>();
		
		do {
			writeNumber.put(file.getLineTime(), Long.parseLong(file.getMessage().split(" ")[0]));
		} while (file.advance());
		
		return writeNumber;
	}

	private Map<Long, Long> getReadSectors(String readInfoFilename) throws NumberFormatException, IOException {
		LogFile file = new LogFile(readInfoFilename);
		Map<Long, Long> readSectors = new HashMap<Long, Long>();
		
		do {
			readSectors.put(file.getLineTime(), Long.parseLong(file.getMessage().split(" ")[1]));
		} while (file.advance());
		
		return readSectors;		
	}

	private Map<Long, Long> getReadNumber(String readInfoFilename) throws IOException {
		LogFile file = new LogFile(readInfoFilename);
		Map<Long, Long> readNumber = new HashMap<Long, Long>();
		
		do {
			readNumber.put(file.getLineTime(), Long.parseLong(file.getMessage().split(" ")[0]));
		} while (file.advance());
		
		return readNumber;
	}

	private List<Double> getMemoryUsage(String memoryInfoFilename) {
		// TODO Auto-generated method stub
		return null;
	}

	private Map<Long, Double> getUserCPU(String userCpuInfoFilename) throws NumberFormatException, IOException {
		LogFile file = new LogFile(userCpuInfoFilename);
		Map<Long, Double> userCPU = new HashMap<Long, Double>();
		
		do {
			userCPU.put(file.getLineTime(), Double.parseDouble(file.getMessage().split(" ")[0]));
		} while (file.advance());
		
		return userCPU;
	}

	private Map<Long, Double> getIdleCpu(String idleCpuInfoFilename) throws IOException {
		LogFile file = new LogFile(idleCpuInfoFilename);
		Map<Long, Double> idleCPU = new HashMap<Long, Double>();
		
		do {
			idleCPU.put(file.getLineTime(), Double.parseDouble(file.getMessage().split(" ")[0]));
		} while (file.advance());
		
		return idleCPU;
	}

	public MachineUsage getUsage(Execution execution) {
		Map<Long, Long> newWriteNumber = new HashMap<Long, Long>();
		Map<Long, Long> newWriteAttempt = new HashMap<Long, Long>();
		Map<Long, Long> newReadNumber = new HashMap<Long, Long>();
		Map<Long, Double> newIdleCPU = new HashMap<Long, Double>();
		Map<Long, Double> newUserCPU = new HashMap<Long, Double>();
		Map<Long, Long> newReadSectors = new HashMap<Long, Long>();
		
		for (Long key : usage.getWriteNumber().keySet()) {
			if (execution.getStartTime() <= key && key <= execution.getFinishTime()) {
				newWriteNumber.put(key, usage.getWriteNumber().get(key));
				newReadNumber.put(key, usage.getReadNumber().get(key));
				newIdleCPU.put(key, usage.getIdleCPU().get(key));
				newUserCPU.put(key, usage.getUserCPU().get(key));
				newWriteAttempt.put(key, usage.getWriteAttempts().get(key));
				newReadSectors.put(key, usage.getReadSectors().get(key));
			}
		}
		
		return new MachineUsage(newIdleCPU, newUserCPU, null, newReadNumber, newReadSectors, newWriteNumber, newWriteAttempt);
	}
}