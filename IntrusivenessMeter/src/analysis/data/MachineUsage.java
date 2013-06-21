package analysis.data;

import java.util.Map;

public class MachineUsage {
	private Map<Long, Double> idleCPU;
	private Map<Long, Double> userCPU;
	private Map<Long, Double> memoryUsage;
	private Map<Long, Double> readNumber;
	private Map<Long, Double> readSectors;
	private Map<Long, Double> writeNumber;
	private Map<Long, Double> writeAttemptNumber;
	
	public MachineUsage(Map<Long, Double> idleCPU, Map<Long, Double> userCPU, Map<Long, Double> memoryUsage,
			Map<Long, Double> readNumber, Map<Long, Double> readSectors,
			Map<Long, Double> writeNumber, Map<Long, Double> writeAttemptNumber) {
		this.idleCPU = idleCPU;
		this.userCPU = userCPU;
		this.memoryUsage = memoryUsage;
		this.readNumber = readNumber;
		this.readSectors = readSectors;
		this.writeNumber = writeNumber;
		this.writeAttemptNumber = writeAttemptNumber;
	}
	
	public Map<Long, Double> getWriteNumber() {
		return writeNumber;
	}

	public Map<Long, Double> getReadNumber() {
		return readNumber;
	}

	public Map<Long, Double> getIdleCPU() {
		return idleCPU;
	}

	public Map<Long, Double> getUserCPU() {
		return userCPU;
	}

	public Map<Long, Double> getWriteAttempts() {
		return writeAttemptNumber;
	}

	public Map<Long, Double> getReadSectors() {
		return readSectors;
	}

	public Map<Long, Double> getMemory() {
		return memoryUsage;
	}
}
