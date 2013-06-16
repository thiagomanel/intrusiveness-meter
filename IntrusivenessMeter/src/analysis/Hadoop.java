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

	private HadoopMachineUsage usage;

	public Hadoop(String cpuFileName, String memoryFileName) throws IOException {
		checkNotNull(cpuFileName, "cpuFileName must not be null.");
		checkNotNull(memoryFileName, "memoryFileName must not be null.");
		usage = new HadoopMachineUsage(getCPUUsage(cpuFileName), getMemoryUsage(memoryFileName));
	}

	private Map<Long, Double> getCPUUsage(String cpuFileName) throws IOException {
		LogFile file = new LogFile(cpuFileName);
		Map<Long, Double> cpu = new HashMap<Long, Double>();
		
		do {
			cpu.put(file.getLineTime(), Double.parseDouble(file.getMessage()));
		} while (file.advance());
		
		return cpu;
	}

	private Map<Long, Double> getMemoryUsage(String memoryFileName) throws NumberFormatException, IOException {
		LogFile file = new LogFile(memoryFileName);
		Map<Long, Double> memory = new HashMap<Long, Double>();
		
		do {
			memory.put(file.getLineTime(), Double.parseDouble(file.getMessage()));
		} while (file.advance());
		
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
		// TODO Auto-generated method stub
		return null;
	}

}
