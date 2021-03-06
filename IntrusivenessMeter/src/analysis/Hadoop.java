package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.Map.Entry;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;

import commons.util.LogFile;

public class Hadoop {
	private static final String REGEX_BETWEEN_SQUARE_BRACKETS = "(^.*?\\[|\\]\\s*$)";
	private static final String INCARNATION_ID_LOG = "Incarnation ID";
	private static final int BENCHMARK_STRING_INDEX = 1;
	private static final String MESSAGE_TOKENS_SEPARATOR = ":";
	private static final String STARTED_BENCHMARK_MARK = "started benchmark:";
	private HadoopMachineUsage usage;
	private HadoopInformation info;

	public Hadoop(String cpuFileName, String memoryFileName, String controllerFileName, String hadoopProcessesFileName) throws IOException {
		checkNotNull(cpuFileName, "cpuFileName must not be null.");
		checkNotNull(memoryFileName, "memoryFileName must not be null.");
		checkNotNull(controllerFileName, "controllerFileName must not be null.");
		checkNotNull(hadoopProcessesFileName, "hadoopProcessesFileName must not be null.");
		usage = new HadoopMachineUsage(getCPUUsage(cpuFileName), getMemoryUsage(memoryFileName), getHadoopProcesses(hadoopProcessesFileName));
		info = new HadoopInformation(readInformation(controllerFileName));
	}

	public Hadoop(HadoopMachineUsage hadoopMachineUsage, HadoopInformation hadoopInfo) {
		checkNotNull(hadoopMachineUsage, "hadoopMachineUsage must not be null.");
		checkNotNull(hadoopInfo, "hadoopInfo must not be null.");

		this.usage = hadoopMachineUsage;
		this.info = hadoopInfo;
	}

	private Map<Long, List<Integer>> getHadoopProcesses(String hadoopProcessesFileName) throws IOException {
		LogFile file = new LogFile(hadoopProcessesFileName);
		Map<Long, List<Integer>> information = new HashMap<Long, List<Integer>>();

		do {
			String message = file.getMessage();
			if (checkNotIncarnationIDLog(message)) {
				String[] processesStrings = message.replaceAll(REGEX_BETWEEN_SQUARE_BRACKETS,"").split(",");
				List<Integer> processes = getProcesses(processesStrings);
				information.put(file.getLineTime(), processes);				
			}
			file.advance();
		} while (!file.reachedEnd());
		return information;
	}

	private List<Integer> getProcesses(String[] processesStrings) {
		List<Integer> processes = new LinkedList<Integer>();

		for (String processString : processesStrings) {
			processString = processString.trim();
			processString = processString.replaceAll("'", "");
			if (!processString.isEmpty()) {
				processes.add(Integer.parseInt(processString));				
			}
		}

		return processes;
	}

	private Map<Long, String> readInformation(String controllerFileName) throws IOException {
		LogFile file = new LogFile(controllerFileName);
		Map<Long, String> information = new HashMap<Long, String>();

		do {
			if (file.getMessage().contains(STARTED_BENCHMARK_MARK)) {
				String benchmark = file.getMessage().split(MESSAGE_TOKENS_SEPARATOR)[BENCHMARK_STRING_INDEX];
				information.put(file.getLineTime(), benchmark);
			}
			file.advance();
		} while (!file.reachedEnd());

		return information;
	}

	private Map<Long, Double> getCPUUsage(String cpuFileName) throws IOException {
		LogFile file = new LogFile(cpuFileName);
		Map<Long, Double> cpu = new HashMap<Long, Double>();

		do {
			String message = file.getMessage();
			getCPULineInfo(file, cpu, message);
			file.advance();
		} while (!file.reachedEnd());

		return cpu;
	}

	private void getCPULineInfo(LogFile file, Map<Long, Double> cpu,
			String message) {
		if (checkNotIncarnationIDLog(message)) {
			cpu.put(file.getLineTime(), Double.parseDouble(message));								
		}
	}

	private boolean checkNotIncarnationIDLog(String message) {
		return !message.contains(INCARNATION_ID_LOG);
	}

	private Map<Long, Double> getMemoryUsage(String memoryFileName) throws NumberFormatException, IOException {
		LogFile file = new LogFile(memoryFileName);
		Map<Long, Double> memory = new HashMap<Long, Double>();

		do {
			String message = file.getMessage();
			getMemoryLineInfo(file, memory, message);
			file.advance();
		} while (!file.reachedEnd());

		return memory;
	}

	private void getMemoryLineInfo(LogFile file, Map<Long, Double> memory,
			String message) {
		if (checkNotIncarnationIDLog(message)) {
			memory.put(file.getLineTime(), Double.parseDouble(message));
		}
	}

	public HadoopMachineUsage getMachineUsage(Execution execution) {
		checkNotNull(execution, "execution must not be null.");
		Map<Long, Double> newCPU = new HashMap<Long, Double>();
		Map<Long, Double> newMemory = new HashMap<Long, Double>();
		Map<Long, List<Integer>> newProcesses = new HashMap<Long, List<Integer>>();

		for (Long time : usage.getCPU().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newCPU.put(time, usage.getCPU().get(time));
			}
		}

		for (Long time : usage.getMemory().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newMemory.put(time, usage.getMemory().get(time));
			}
		}

		for (Long time : usage.getProcesses().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newProcesses.put(time, usage.getProcesses().get(time));
			}
		}

		return new HadoopMachineUsage(newCPU, newMemory, newProcesses);
	}

	public HadoopInformation getInformation(Execution execution) {
		checkNotNull(execution, "execution must not be null.");
		Map<Long, String> newBenchmarks = new HashMap<Long, String>();

		TreeSet<Long> times = new TreeSet<Long>(info.getBenchmarks().keySet());
		Iterator<Long> timesIterator = times.descendingIterator();

		long time = 0;

		while (timesIterator.hasNext()) {
			time = timesIterator.next();
			// finds the first benchmark before the execution time
			if (time < execution.getStartTime()) {
				break;
			}
		}

		newBenchmarks.put(time, info.getBenchmarks().get(time));
		return new HadoopInformation(newBenchmarks);
	}

	public boolean thereAreRunningTasks(Execution execution) {
		for (Long time : usage.getProcesses().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				if (!usage.getProcesses().get(time).isEmpty()) {
					return true;
				}
			}
		}
		return false;
	}

	public double getNearestCPUUsage(long time, long intervalSize) {
		return usage.getNearestCPUUsage(time, intervalSize);	
	}

	public double getNearestMemoryUsage(long discomfortTime, long intervalSize) {
		return usage.getNearestMemoryUsage(discomfortTime, intervalSize);
	}

	public Map<Double, Double> getCPUDiscomfortProbabilities(Discomfort discomfort, long intervalSize, 
			int numberOfCPUs, IdleUser idle) {
		TreeMap<Double, Double> probabilities = new TreeMap<Double, Double>();

		setUpProbabilities(probabilities);

		Map<Double, Double> numberOfDiscomforts = getNumberOfDiscomforts(discomfort, intervalSize, numberOfCPUs, idle);
		Map<Double, Double> numberOfTimes = getNumberOfTimes(probabilities, idle, numberOfCPUs);

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

	private Map<Double, Double> getNumberOfTimes(TreeMap<Double, Double> probabilities, IdleUser idle, int numberOfCPUs) {
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		setUpProbabilities(map);

		for (Entry<Long, Double> idleCPU : usage.getCPU().entrySet()) {
			if (isValid(new Execution(idleCPU.getKey() - 5000000000L, idleCPU.getKey() + 5000000000L), idle)) {
				double cpuUsage = idleCPU.getValue()/numberOfCPUs;

				map.put(map.floorKey(cpuUsage), map.get(map.floorKey(cpuUsage)) + 1);
			}
		}

		return map;
	}

	private boolean isValid(Execution execution, IdleUser idle) {
		return !idle.idle(execution) && thereAreRunningTasks(execution);
	}

	private Map<Double, Double> getNumberOfDiscomforts(Discomfort discomfort, long intervalSize, int numberOfCPUs, 
														IdleUser idle) {
		TreeMap<Double, Double> map = new TreeMap<Double, Double>();
		setUpProbabilities(map);

		for (long time : discomfort.getDiscomfortTimes(new Execution(Long.MIN_VALUE, Long.MAX_VALUE))) {
			if (isValid(new Execution(time - 5000000000L, time + 5000000000L), idle)) {
				System.out.println("time:" + time);
				double cpuUsage = usage.getNearestCPUUsage(time, intervalSize)/numberOfCPUs;
				
				if (cpuUsage != Double.NEGATIVE_INFINITY) {
					System.out.println("map:" + map);
					System.out.println("cpuUsage:" + cpuUsage);
					double floorkey = map.floorKey(cpuUsage);
					double newValue = map.get(floorkey) + 1;
					map.put(floorkey, newValue);
				}				
			}
		}

		return map;
	}
}
