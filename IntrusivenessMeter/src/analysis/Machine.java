package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import analysis.data.Execution;
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

	private Map<Long, Double> getLongLongMapFromFile(String filename, int indexFromMessage) throws IOException {
		LogFile file = new LogFile(filename);
		Map<Long, Double> map = new TreeMap<Long, Double>();
		
		do {
			String message = file.getMessage();
			if (message != null) {
				map.put(file.getLineTime(), Double.parseDouble(message.split("\\s+")[indexFromMessage]));
			}
			file.advance();				
		} while (!file.reachedEnd());
		
		return map;	
	}
	
	private Map<Long, Double> getLongDoubleMapFromFile(String filename, int indexFromMessage) throws IOException {
		LogFile file = new LogFile(filename);
		Map<Long, Double> map = new TreeMap<Long, Double>();
		
		do {
			String message = file.getMessage();
			if (message != null) {
				map.put(file.getLineTime(), Double.parseDouble(message.split("\\s+")[indexFromMessage]));				
			}
			file.advance();				
		} while (!file.reachedEnd());
		
		return map;	
	}
	
	private Map<Long, Double> getWriteAttemptNumber(String writeInfoFilename) throws IOException {
		return getLongLongMapFromFile(writeInfoFilename, 1);
	}

	private Map<Long, Double> getWriteNumber(String writeInfoFilename) throws IOException {
		return getLongLongMapFromFile(writeInfoFilename, 0);
	}

	private Map<Long, Double> getReadSectors(String readInfoFilename) throws NumberFormatException, IOException {
		return getLongLongMapFromFile(readInfoFilename, 1);
	}

	private Map<Long, Double> getReadNumber(String readInfoFilename) throws IOException {
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
		checkNotNull(execution, "execution must not be null.");
		Map<Long, Double> newWriteNumber = new TreeMap<Long, Double>();
		Map<Long, Double> newWriteAttempt = new TreeMap<Long, Double>();
		for (Long time : usage.getWriteNumber().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newWriteNumber.put(time, usage.getWriteNumber().get(time));
				newWriteAttempt.put(time, usage.getWriteAttempts().get(time));
			}
		}
		
		Map<Long, Double> newReadNumber = new TreeMap<Long, Double>();
		Map<Long, Double> newReadSectors = new TreeMap<Long, Double>();
		for (Long time : usage.getReadNumber().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newReadNumber.put(time, usage.getReadNumber().get(time));
				newReadSectors.put(time, usage.getReadSectors().get(time));
			}
		}
		
		return new MachineUsage(getNewIdleCPU(execution), getNewUserCPU(execution), getNewMemory(execution), 
				newReadNumber, newReadSectors, newWriteNumber, newWriteAttempt);
	}

	private Map<Long, Double> getNewMemory(Execution execution) {
		Map<Long, Double> newMemory = new TreeMap<Long, Double>();
		for (Long time : usage.getMemory().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newMemory.put(time, usage.getMemory().get(time));
			}
		}
		return newMemory;
	}

	private Map<Long, Double> getNewUserCPU(Execution execution) {
		Map<Long, Double> newUserCPU = new TreeMap<Long, Double>();
		for (Long time : usage.getUserCPU().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newUserCPU.put(time, usage.getUserCPU().get(time));
			}
		}
		return newUserCPU;
	}

	private Map<Long, Double> getNewIdleCPU(Execution execution) {
		Map<Long, Double> newIdleCPU = new TreeMap<Long, Double>();
		for (Long time : usage.getIdleCPU().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newIdleCPU.put(time, usage.getIdleCPU().get(time));
			}
		}
		return newIdleCPU;
	}

	public Map<Double, Double> getCPUDiscomfortProbabilities(Discomfort discomfort, long intervalSize, 
															IdleUser idle, Hadoop hadoop) {
		TreeMap<Double, Double> probabilities = new TreeMap<Double, Double>();
		
		setUpProbabilities(probabilities);
		
		Map<Double, Double> numberOfTimes = getNumberOfTimes(probabilities, idle, hadoop);
		Map<Double, Double> numberOfDiscomforts = getNumberOfDiscomforts(discomfort, intervalSize, hadoop);
		
		for (int cpuLevel = 0; cpuLevel <= 100; cpuLevel += 10) {
			if (numberOfTimes.get(new Double(cpuLevel)) != 0) {
				double probability = numberOfDiscomforts.get(new Double(cpuLevel))/numberOfTimes.get(new Double(cpuLevel));
				probabilities.put(new Double(cpuLevel), probability);
			}
		}
		
		return probabilities;
	}

	private void setUpProbabilities(Map<Double, Double> probabilities) {
		for (int i = 0; i <= 10; i++) {
			probabilities.put(i*10.0, 0.0);
		}		
	}

	private Map<Double, Double> getNumberOfTimes(TreeMap<Double, Double> probabilities, IdleUser idle, Hadoop hadoop) {
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		setUpProbabilities(map);
		
		for (Entry<Long, Double> idleCPU : usage.getIdleCPU().entrySet()) {
			if (!idle.idle(new Execution(idleCPU.getKey() - 5000000000L, idleCPU.getKey() + 5000000000L)) && 
									!thereAreRunningTasks(new Execution(idleCPU.getKey() - 5000000000L, idleCPU.getKey() + 5000000000L), hadoop)) {
				double cpuUsage = 100 - idleCPU.getValue();
				
				map.put(map.floorKey(cpuUsage), map.get(map.floorKey(cpuUsage)) + 1);
			}
			//if (isValid(new Execution(idleCPU.getKey() - 5000000000L, idleCPU.getKey() + 5000000000L), idle, hadoop)) {
				/*double cpuUsage = 100 - idleCPU.getValue();
				
				map.put(map.floorKey(cpuUsage), map.get(map.floorKey(cpuUsage)) + 1);*/
		//	}
		}
		
		return map;
	}
	
	private boolean isValid(Execution execution, IdleUser idle, Hadoop hadoop) {
		return !idle.idle(execution) && thereAreRunningTasks(execution, hadoop);
	}

	private boolean thereAreRunningTasks(Execution execution, Hadoop hadoop) {
		return hadoop.thereAreRunningTasks(execution);
	}

	private Map<Double, Double> getNumberOfDiscomforts(Discomfort discomfort, long intervalSize, Hadoop hadoop) {
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		setUpProbabilities(map);
		
		for (long time : discomfort.getDiscomfortTimes(new Execution(Long.MIN_VALUE, Long.MAX_VALUE))) {
			if (!thereAreRunningTasks(new Execution(time - 5000000000L, time + 5000000000L), hadoop)) {
				double cpuUsage = usage.getNearestCPUUsage(time, intervalSize);
				map.put(map.floorKey(cpuUsage), map.get(map.floorKey(cpuUsage)) + 1);				
			}
		}
		
		return map;
	}
}