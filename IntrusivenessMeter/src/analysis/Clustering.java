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
	private IdleUser idle;
	
	public Clustering(Hadoop hadoop, Discomfort discomfort, List<Execution> executions, IdleUser idle, long totalMemory, int numberOfCPUs) {
		checkNotNull(hadoop, "hadoop must not be null.");
		checkNotNull(discomfort, "discomfort must not be null.");
		checkNotNull(executions, "executions must not be null.");
		checkNotNull(idle, "idle must not be null.");
		check(totalMemory > 0, "totalMemory must be positive.");
		check(numberOfCPUs > 0, "numberOfCPUs must be positive.");
		this.hadoop = hadoop;
		this.discomfort = discomfort;
		this.executions = executions;
		this.totalMemory = totalMemory;
		this.numberOfCPUs = numberOfCPUs;
		this.idle = idle;
	}

	public Map<Double, Double> getHadoopCPUUsageDiscomfortProbability() {
		Map<Double, Integer> countOccurrences = new TreeMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		int validExecutions = 0;
		
		for (Execution execution : executions) {
			if (isValid(execution)) {
				validExecutions++;
				Double cpuUsage = getFirstDiscomfortReportCPUUsage(execution);
				if (cpuUsage != -1) {
					countForEachGreater(countOccurrences, cpuUsage);						
				}				
			}
		}
		probabilities = divideBy(countOccurrences, validExecutions);
		
		return probabilities;
	}
	
	public Map<Double, Double> getMemoryUsageDiscomfortProbability() {
		Map<Double, Integer> countOccurrences = new TreeMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		int validExecutions = 0;
		
		for (Execution execution : executions) {
			if (isValid(execution)) {
				validExecutions++;
				Double memoryUsage = getFirstDiscomfortReportMemoryUsage(execution);
				if (memoryUsage != -1) {
					countForEachGreater(countOccurrences, memoryUsage);
				}				
			}
		}
		probabilities = divideBy(countOccurrences, validExecutions);
		
		return probabilities;
	}
	
	private boolean isValid(Execution execution) {
		return !idle.idle(execution) && thereAreRunningTasks(execution);
	}

	private boolean thereAreRunningTasks(Execution execution) {
		return hadoop.thereAreRunningTasks(execution);
	}

	private Double getFirstDiscomfortReportMemoryUsage(Execution execution) {
		List<Long> executionDiscomfortTimes = discomfort.getDiscomfortTimes(execution);
		if (executionDiscomfortTimes.isEmpty()) {
			return -1.0;
		}
		Long firstDiscomfortTime = executionDiscomfortTimes.get(0);
		return getNearestMemoryValue(execution, firstDiscomfortTime);
	}

	private Double getNearestMemoryValue(Execution range, Long value) {
		HadoopMachineUsage usageInRange = hadoop.getMachineUsage(range);
		TreeMap<Long, Double> memoryInRange = new TreeMap<Long, Double>(usageInRange.getMemory());
		double memoryUsed = getGreatestValueKeyLessThan(memoryInRange, value); 
		return memoryUsed*100/totalMemory;
	}

	private Double getGreatestValueKeyLessThan(TreeMap<Long, Double> map, long key) {
		double greatest = Double.MIN_VALUE;
		
		for (Long k : map.keySet()) {
			if (k <= key) {
				if (map.get(k) > greatest) {
					greatest = map.get(k);
				}
			}
		}
		
		return greatest;
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
		double cpuUsed = getGreatestValueKeyLessThan(cpuInRange, value);
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

	private void countForEachGreater(Map<Double, Integer> countOccurrences, Double value) {
		for (Double key : countOccurrences.keySet()) {
			if (key >= value) {
				countOccurrences.put(key, countOccurrences.get(key) + 1);
			}
		}
	}
}
