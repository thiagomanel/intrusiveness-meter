package analysis.data;

import static commons.Preconditions.checkNotNull;

import java.util.Map;
import java.util.TreeMap;

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
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("HadoopInformation [benchmark=");
		builder.append(benchmark);
		builder.append(", conf=");
		builder.append(conf);
		builder.append("]");
		return builder.toString();
	}

	public String getFirstBenchmark() {
		return new TreeMap<Long, String>(benchmark).firstEntry().getValue();
	}
}
