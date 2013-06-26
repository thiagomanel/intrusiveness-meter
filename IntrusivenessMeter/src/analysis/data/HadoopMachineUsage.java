package analysis.data;

import static commons.Preconditions.checkNotNull;

import java.util.Map;

public class HadoopMachineUsage {
	private Map<Long, Double> cpuUsage;
	private Map<Long, Double> memoryUsage;
	
	public HadoopMachineUsage(Map<Long, Double> cpuUsage, Map<Long, Double> memoryUsage) {
		checkNotNull(cpuUsage, "cpuUsage must not be null.");
		checkNotNull(memoryUsage, "memoryUsage must not be null.");
		this.cpuUsage = cpuUsage;
		this.memoryUsage = memoryUsage;
	}

	public Map<Long, Double> getCPU() {
		return cpuUsage;
	}

	public Map<Long, Double> getMemory() {
		return memoryUsage;
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
}
