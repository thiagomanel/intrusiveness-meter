package analysis;

import static commons.Preconditions.checkNotNull;
import static commons.Preconditions.check;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;
import analysis.data.HadoopMachineUsage;

public class Clustering {
	
	private Hadoop hadoop;
	private Discomfort discomfort;
	private List<Execution> executions;
	private long totalMemory;
	private int numberOfCPUs;
	
	public Clustering(Hadoop hadoop, Discomfort discomfort, List<Execution> executions, long totalMemory, int numberOfCPUs) {
		checkNotNull(hadoop, "hadoop must not be null.");
		checkNotNull(discomfort, "discomfort must not be null.");
		checkNotNull(executions, "executions must not be null.");
		check(totalMemory > 0, "totalMemory must be positive.");
		check(numberOfCPUs > 0, "numberOfCPUs must be positive.");
		this.hadoop = hadoop;
		this.discomfort = discomfort;
		this.executions = executions;
		this.totalMemory = totalMemory;
		this.numberOfCPUs = numberOfCPUs;
	}

	public Map<Double, Double> getHadoopCPUUsageDiscomfortProbability() {
		Map<Double, Integer> countOccurrences = new HashMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		
		for (Execution execution : executions) {
			Double cpuUsage = getFirstDiscomfortReportCPUUsage(execution);
			if (cpuUsage != -1) {
				count(countOccurrences, cpuUsage);						
			}
		}
		probabilities = divideBy(countOccurrences, executions.size());
		
		return probabilities;
	}
	
	public Map<Double, Double> getMemoryUsageDiscomfortProbability() {
		Map<Double, Integer> countOccurrences = new HashMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		
		for (Execution execution : executions) {
			Double memoryUsage = getFirstDiscomfortReportMemoryUsage(execution);
			if (memoryUsage != -1) {
				count(countOccurrences, memoryUsage);
			}
		}
		probabilities = divideBy(countOccurrences, executions.size());
		
		return probabilities;
	}

	private Double getFirstDiscomfortReportMemoryUsage(Execution execution) {
		List<Long> executionDiscomfortTimes = discomfort.getDiscomfortTimes(execution);
		if (executionDiscomfortTimes.isEmpty()) {
			return -1.0;
		}
		Long firstDiscomfortTime = executionDiscomfortTimes.get(0);
		return getNearestMemoryValue(execution, firstDiscomfortTime);
	}

	private Double getNearestMemoryValue(Execution range,
			Long value) {
		HadoopMachineUsage usageInRange = hadoop.getMachineUsage(range);
		TreeMap<Long, Double> memoryInRange = new TreeMap<Long, Double>(usageInRange.getMemory());
		long previousTime = memoryInRange.floorKey(value);
		double memoryUsed = memoryInRange.get(previousTime); 
		return memoryUsed*100/totalMemory;
	}

	private void setUpCount(Map<Double, Integer> countOccurrences) {
		for (int i = 0; i <= 100; i += 10) {
			countOccurrences.put(new Double(i), 0);
		}
	}

	private Double getFirstDiscomfortReportCPUUsage(Execution execution) {
		List<Long> executionDiscomfortTimes = discomfort.getDiscomfortTimes(execution);
		if (executionDiscomfortTimes.isEmpty()) {
			return -1.0;
		}
		Long firstDiscomfortTime = executionDiscomfortTimes.get(0);
		return getNearestCPUValue(execution, firstDiscomfortTime); 
	}

	private Double getNearestCPUValue(Execution range,
			Long value) {
		HadoopMachineUsage usageInRange = hadoop.getMachineUsage(range);
		TreeMap<Long, Double> cpuInRange = new TreeMap<Long, Double>(usageInRange.getCPU());
		long previousTime = cpuInRange.floorKey(value);
		double cpuUsed = cpuInRange.get(previousTime); 
		return cpuUsed/numberOfCPUs;
	}

	private Map<Double, Double> divideBy(Map<Double, Integer> countOccurrences,
			double size) {
		Map<Double, Double> result = new HashMap<Double, Double>();
		for (Double key : countOccurrences.keySet()) {
			result.put(key, countOccurrences.get(key)/size);
		}
		return result;
	}

	private void count(Map<Double, Integer> countOccurrences, Double value) {
		for (Double key : countOccurrences.keySet()) {
			if (key >= value) {
				countOccurrences.put(key, countOccurrences.get(key) + 1);
			}
		}
	}
}
