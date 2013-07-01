package analysis;

import static commons.Preconditions.checkNotNull;
import static commons.util.StringUtil.concat;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class ReportWriter {

	private PrintStream resultFileStream;
	
	public ReportWriter(String resultFileName) throws FileNotFoundException {
		checkNotNull(resultFileName, "resultFileName must not be null.");
		resultFileStream = new PrintStream(resultFileName);
		
		resultFileStream.printf("start_time, finish_time, related_discomfort, benchmark, hadoop_cpu_usage, hadoop_memory_usage\n");
	}
	
	public void write(Execution execution, boolean relatedDiscomfort,
			MachineUsage machineUsage, HadoopMachineUsage hadoopMachineUsage,
			HadoopInformation hadoopInfo) {
		resultFileStream.printf("%d, %d, %b, %s, %s, %s\n", execution.getStartTime(), execution.getFinishTime(),
					relatedDiscomfort, getBenchmark(hadoopInfo), getCPUUsageString(hadoopMachineUsage), 
					getMemoryUsageString(hadoopMachineUsage));
	}

	private String getCPUUsageString(HadoopMachineUsage hadoopMachineUsage) {
		return concat(" ", new ArrayList<Object>(hadoopMachineUsage.getCPU().values()));
	}

	private String getMemoryUsageString(HadoopMachineUsage hadoopMachineUsage) {
		return concat(" ", new ArrayList<Object>(hadoopMachineUsage.getMemory().values()));
	}
	
	private String getBenchmark(HadoopInformation hadoopInfo) {
		return hadoopInfo.getBenchmarks().values().iterator().next();
	}
}
