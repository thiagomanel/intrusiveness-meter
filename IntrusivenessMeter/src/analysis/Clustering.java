package analysis;

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
	
	public Clustering(Hadoop hadoop, Discomfort discomfort, List<Execution> executions) {
		this.hadoop = hadoop;
		this.discomfort = discomfort;
		this.executions = executions;
	}

	public Map<Double, Double> getHadoopCPUUsageDiscomfortProbability() {
		Map<Double, Integer> countOccurrences = new HashMap<Double, Integer>();
		setUpCount(countOccurrences);
		
		Map<Double, Double> probabilities = null;
		
		for (Execution execution : executions) {
			Double cpuUsage = getFirstDiscomfortReportCPUUsage(execution);
			count(countOccurrences, cpuUsage);		
		}
		probabilities = divideBy(countOccurrences, executions.size());
		
		return probabilities;
	}

	private void setUpCount(Map<Double, Integer> countOccurrences) {
		for (int i = 0; i <= 100; i += 10) {
			countOccurrences.put(new Double(i), 0);
		}
	}

	private Double getFirstDiscomfortReportCPUUsage(Execution execution) {
		List<Long> executionDiscomfortTimes = discomfort.getDiscomfortTimes(execution);
		Long firstDiscomfortTime = executionDiscomfortTimes.get(0);
		return getNearestCPUValue(execution, firstDiscomfortTime); 
	}

	private Double getNearestCPUValue(Execution range,
			Long value) {
		HadoopMachineUsage usageInRange = hadoop.getMachineUsage(range);
		TreeMap<Long, Double> cpuInRange = new TreeMap<Long, Double>(usageInRange.getCPU());
		long previousTime = cpuInRange.floorKey(value);
		return cpuInRange.get(previousTime);
	}

	private Map<Double, Double> divideBy(Map<Double, Integer> countOccurrences,
			double size) {
		Map<Double, Double> result = new HashMap<Double, Double>();
		for (Double key : countOccurrences.keySet()) {
			result.put(key, countOccurrences.get(key)/size);
		}
		return result;
	}

	private void count(Map<Double, Integer> countOccurrences, Double cpuUsage) {
		for (Double key : countOccurrences.keySet()) {
			if (key >= cpuUsage) {
				countOccurrences.put(key, countOccurrences.get(key) + 1);
			}
		}
	}
}
