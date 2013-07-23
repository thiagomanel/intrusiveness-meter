package analysis;

import static commons.Preconditions.check;
import static commons.Preconditions.checkNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;

public class Clustering {
	
	private Hadoop hadoop;
	private Discomfort discomfort;
	private List<Execution> executions;
	private long totalMemory;
	private int numberOfCPUs;
	private IdleUser idle;
	private long intervalTime;
	
	public Clustering(Hadoop hadoop, Discomfort discomfort, List<Execution> executions, IdleUser idle, long totalMemory,
						int numberOfCPUs, long intervalTime) {
		checkNotNull(hadoop, "hadoop must not be null.");
		checkNotNull(discomfort, "discomfort must not be null.");
		checkNotNull(executions, "executions must not be null.");
		checkNotNull(idle, "idle must not be null.");
		check(totalMemory > 0, "totalMemory must be positive.");
		check(numberOfCPUs > 0, "numberOfCPUs must be positive.");
		check(intervalTime >= 0, "intervalTime must not be negative.");
		this.hadoop = hadoop;
		this.discomfort = discomfort;
		this.executions = executions;
		this.totalMemory = totalMemory;
		this.numberOfCPUs = numberOfCPUs;
		this.idle = idle;
		this.intervalTime = intervalTime;
	}

	public Map<Double, Double> getHadoopCPUUsageDiscomfortProbability() {
		TreeMap<Double, Integer> countOccurrences = new TreeMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		int validExecutions = 0;
		
		for (Execution execution : executions) {
			if (isValid(execution)) {
				validExecutions++;
				
				List<Long> discomfortTimes = discomfort.getDiscomfortTimes(execution);
				if (!discomfortTimes.isEmpty()) {
					double cpuUsage = getDiscomfortReportCPUUsage(discomfortTimes.get(0), intervalTime);
					count(countOccurrences, cpuUsage);
				}			
			}
		}
		probabilities = divideBy(countOccurrences, validExecutions);
		
		return probabilities;
	}
	
	private double getDiscomfortReportCPUUsage(Long time, long interval) {
		return hadoop.getNearestCPUUsage(time, interval)/numberOfCPUs;
	}

	public Map<Double, Double> getMemoryUsageDiscomfortProbability() {
		TreeMap<Double, Integer> countOccurrences = new TreeMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		int validExecutions = 0;
		
		for (Execution execution : executions) {
			if (isValid(execution)) {
				validExecutions++;
				
				List<Long> discomfortTimes = discomfort.getDiscomfortTimes(execution);
				if (!discomfortTimes.isEmpty()) {
					double memoryUsage = getDiscomfortReportMemoryUsage(discomfortTimes.get(0), intervalTime);
					count(countOccurrences, memoryUsage);
				}		
			}
		}
		probabilities = divideBy(countOccurrences, validExecutions);
		
		return probabilities;
	}

	private void count(TreeMap<Double, Integer> countOccurrences, double value) {
		Double key = countOccurrences.floorKey(value);
		Integer oldValue = countOccurrences.get(key);
		countOccurrences.put(key, oldValue + 1);
	}

	private double getDiscomfortReportMemoryUsage(long discomfortTime, long intervalSize) {
		return hadoop.getNearestMemoryUsage(discomfortTime, intervalSize)*100/totalMemory;
	}

	private boolean isValid(Execution execution) {
		return !idle.idle(execution) && thereAreRunningTasks(execution);
	}

	private boolean thereAreRunningTasks(Execution execution) {
		return hadoop.thereAreRunningTasks(execution);
	}
	
	private void setUpCount(Map<Double, Integer> countOccurrences) {
		for (int i = 0; i <= 100; i += 10) {
			countOccurrences.put(new Double(i), 0);
		}
	}

	private Map<Double, Double> divideBy(Map<Double, Integer> countOccurrences,
			double size) {
		Map<Double, Double> result = new HashMap<Double, Double>();
		for (Double key : countOccurrences.keySet()) {
			result.put(key, countOccurrences.get(key)/size);
		}
		return result;
	}
}
