package analysis.data;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("MachineUsage [idleCPU=");
		builder.append(idleCPU);
		builder.append(", userCPU=");
		builder.append(userCPU);
		builder.append(", memoryUsage=");
		builder.append(memoryUsage);
		builder.append(", readNumber=");
		builder.append(readNumber);
		builder.append(", readSectors=");
		builder.append(readSectors);
		builder.append(", writeNumber=");
		builder.append(writeNumber);
		builder.append(", writeAttemptNumber=");
		builder.append(writeAttemptNumber);
		builder.append("]");
		return builder.toString();
	}

	public double getNearestCPUUsage(long time, long intervalSize) {
		TreeMap<Long, Double> newMap = new TreeMap<Long, Double>(idleCPU);
		List<Long> relatedKeys = getRelatedKeys(newMap, time, intervalSize);
		double maxCPU = Double.NEGATIVE_INFINITY;
		
		for (Long currentTime : relatedKeys) {
			if (newMap.get(currentTime) > maxCPU) {
				maxCPU = newMap.get(currentTime);
			}
		}
		
		return 100 - maxCPU;
	}
	
	private List<Long> getRelatedKeys(TreeMap<Long, Double> newMap, long time,
			long intervalSize) {
		List<Long> relatedKeys = new LinkedList<Long>();
		
		for (Long key : newMap.keySet()) {
			if (time - intervalSize <= key && key <= time) {
				relatedKeys.add(key);
			}
		}
		
		return relatedKeys;
	}
}
