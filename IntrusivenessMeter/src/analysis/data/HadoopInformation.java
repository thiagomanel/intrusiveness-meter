package analysis.data;

import static commons.Preconditions.checkNotNull;

import java.util.Map;

public class HadoopInformation {
	private Map<Long, String> benchmark;
	private Map<Long, Configuration> conf;
	
	public HadoopInformation(Map<Long, String> benchmarks) {
		checkNotNull(benchmarks, "benchmarks must not be null.");
		this.benchmark = benchmarks;
	}

	public Map<Long, String> getBenchmarks() {
		return benchmark;
	}
}
