package analysis.data;

import static commons.Preconditions.checkNotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class HadoopMachineUsage {
	private Map<Long, Double> cpuUsage;
	private Map<Long, Double> memoryUsage;
	private Map<Long, List<Integer>> processes;
	
	public HadoopMachineUsage(Map<Long, Double> cpuUsage, Map<Long, Double> memoryUsage, Map<Long, List<Integer>> processes) {
		checkNotNull(cpuUsage, "cpuUsage must not be null.");
		checkNotNull(memoryUsage, "memoryUsage must not be null.");
		checkNotNull(processes, "processes must not be null.");
		this.cpuUsage = cpuUsage;
		this.memoryUsage = memoryUsage;
		this.processes = processes;
	}

	public Map<Long, Double> getCPU() {
		return cpuUsage;
	}

	public Map<Long, Double> getMemory() {
		return memoryUsage;
	}

	public Map<Long, List<Integer>> getProcesses() {
		return processes;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HadoopMachineUsage [cpuUsage=");
		builder.append(cpuUsage);
		builder.append(", memoryUsage=");
		builder.append(memoryUsage);
		builder.append("]");
		return builder.toString();
	}

	public double getNearestCPUUsage(long time, long intervalSize) {
		TreeMap<Long, Double> newMap = new TreeMap<Long, Double>(cpuUsage);
		List<Long> relatedKeys = getRelatedKeys(newMap, time, intervalSize);
		double maxCPU = Double.NEGATIVE_INFINITY;
		
		System.out.println("related_keys:" + relatedKeys);
		for (Long currentTime : relatedKeys) {
			if (newMap.get(currentTime) > maxCPU) {
				maxCPU = newMap.get(currentTime);
			}
		}
		return maxCPU;
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

	public double getNearestMemoryUsage(long discomfortTime, long intervalSize) {
		TreeMap<Long, Double> newMap = new TreeMap<Long, Double>(memoryUsage);
		List<Long> relatedKeys = getRelatedKeys(newMap, discomfortTime, intervalSize);
		double maxMemory = Double.MIN_VALUE;
		
		for (Long currentTime : relatedKeys) {
			if (newMap.get(currentTime) > maxMemory) {
				maxMemory = newMap.get(currentTime);
			}
		}
		
		return maxMemory;
	}
}
