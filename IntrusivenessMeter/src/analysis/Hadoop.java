package analysis;

import static commons.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;

import commons.util.LogFile;

public class Hadoop {

	private static final int BENCHMARK_STRING_INDEX = 1;
	private static final String MESSAGE_TOKENS_SEPARATOR = ":";
	private static final String STARTED_BENCHMARK_MARK = "started benchmark:";
	private HadoopMachineUsage usage;
	private HadoopInformation info;
	
	public Hadoop(String cpuFileName, String memoryFileName, String controllerFileName) throws IOException {
		checkNotNull(cpuFileName, "cpuFileName must not be null.");
		checkNotNull(memoryFileName, "memoryFileName must not be null.");
		usage = new HadoopMachineUsage(getCPUUsage(cpuFileName), getMemoryUsage(memoryFileName));
		info = new HadoopInformation(readInformation(controllerFileName));
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
			cpu.put(file.getLineTime(), Double.parseDouble(file.getMessage()));
			file.advance();
		} while (!file.reachedEnd());
		
		return cpu;
	}

	private Map<Long, Double> getMemoryUsage(String memoryFileName) throws NumberFormatException, IOException {
		LogFile file = new LogFile(memoryFileName);
		Map<Long, Double> memory = new HashMap<Long, Double>();
		
		do {
			memory.put(file.getLineTime(), Double.parseDouble(file.getMessage()));
			file.advance();
		} while (!file.reachedEnd());
		
		return memory;
	}

	public HadoopMachineUsage getMachineUsage(Execution execution) {
		checkNotNull(execution, "execution must not be null.");
		Map<Long, Double> newCPU = new HashMap<Long, Double>();
		Map<Long, Double> newMemory = new HashMap<Long, Double>();
		
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
		
		return new HadoopMachineUsage(newCPU, newMemory);
	}

	public HadoopInformation getInformation(Execution execution) {
		checkNotNull(execution, "execution must not be null.");
		Map<Long, String> newBenchmarks = new HashMap<Long, String>();
		
		for (Long time : info.getBenchmarks().keySet()) {
			if (execution.getStartTime() <= time && time <= execution.getFinishTime()) {
				newBenchmarks.put(time, info.getBenchmarks().get(time));
			}
		}
		
		return new HadoopInformation(newBenchmarks);
	}
}
