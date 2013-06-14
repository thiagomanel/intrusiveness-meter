package analysis;

import java.util.List;
import java.util.Map;

public class MachineUsage {
	private Map<Long, Double> idleCPU;
	private Map<Long, Double> userCPU;
	private List<Double> memoryUsage;
	private Map<Long, Long> readNumber;
	private Map<Long, Long> readSectors;
	private Map<Long, Long> writeNumber;
	private Map<Long, Long> writeAttemptNumber;
	
	public MachineUsage(Map<Long, Double> idleCPU, Map<Long, Double> userCPU, List<Double> memoryUsage,
			Map<Long, Long> readNumber, Map<Long, Long> readSectors,
			Map<Long, Long> map, Map<Long, Long> writeAttemptNumber) {
		this.idleCPU = idleCPU;
		this.userCPU = userCPU;
		this.memoryUsage = memoryUsage;
		this.readNumber = readNumber;
		this.readSectors = readSectors;
		this.writeNumber = map;
		this.writeAttemptNumber = writeAttemptNumber;
	}
	
	public Map<Long, Long> getWriteNumber() {
		return writeNumber;
	}

	public Map<Long, Long> getReadNumber() {
		return readNumber;
	}

	public Map<Long, Double> getIdleCPU() {
		return idleCPU;
	}

	public Map<Long, Double> getUserCPU() {
		return userCPU;
	}

	public Map<Long, Long> getWriteAttempts() {
		return writeAttemptNumber;
	}

	public Map<Long, Long> getReadSectors() {
		return readSectors;
	}
}
