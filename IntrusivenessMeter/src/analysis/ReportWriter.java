package analysis;

import static commons.Preconditions.checkNotNull;
import static commons.util.StringUtil.concat;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class ReportWriter {

	private PrintStream resultFileStream;
	
	public ReportWriter(String resultFileName) throws FileNotFoundException {
		checkNotNull(resultFileName, "resultFileName must not be null.");
		resultFileStream = new PrintStream(resultFileName);
		
		String header = "start_time, finish_time, related_discomfort, benchmark, hadoop_cpu_usage, hadoop_memory_usage";
		header += ", system_idle_cpu, system_user_cpu, system_memory, system_read_number, system_read_sectors";
		header += ", system_write_number, system_write_attempts\n";
		
		resultFileStream.printf(header);
	}
	
	public void write(Execution execution, boolean relatedDiscomfort,
			MachineUsage machineUsage, HadoopMachineUsage hadoopMachineUsage,
			HadoopInformation hadoopInfo) {
		resultFileStream.printf("%d, %d, %b, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s\n", execution.getStartTime(), execution.getFinishTime(),
					relatedDiscomfort, getBenchmark(hadoopInfo), getCPUUsageString(hadoopMachineUsage), 
					getMemoryUsageString(hadoopMachineUsage), getSystemIdleCPUString(machineUsage), 
					getSystemUserCPUString(machineUsage), getSystemMemory(machineUsage), 
					getSystemReadNumberString(machineUsage), getSystemReadSectorsString(machineUsage), 
					getSystemWriteNumberString(machineUsage), getSystemWriteAttemptsString(machineUsage));
	}

	private String getSystemWriteAttemptsString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getWriteAttempts().values()));
	}

	private String getSystemWriteNumberString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getWriteNumber().values()));
	}

	private String getSystemReadSectorsString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getReadSectors().values()));
	}

	private String getSystemReadNumberString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getReadNumber().values()));
	}

	private String getSystemMemory(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getMemory().values()));
	}

	private String getSystemUserCPUString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getUserCPU().values()));
	}

	private String getSystemIdleCPUString(MachineUsage machineUsage) {
		return concat(" ", new ArrayList<Object>(machineUsage.getIdleCPU().values()));
	}

	private String getCPUUsageString(HadoopMachineUsage hadoopMachineUsage) {
		return concat(" ", new ArrayList<Object>(hadoopMachineUsage.getCPU().values()));
	}

	private String getMemoryUsageString(HadoopMachineUsage hadoopMachineUsage) {
		return concat(" ", new ArrayList<Object>(hadoopMachineUsage.getMemory().values()));
	}
	
	private String getBenchmark(HadoopInformation hadoopInfo) {
		// should have only one benchmark
		return hadoopInfo.getBenchmarks().values().iterator().next();
	}

	public <K extends Object, V extends Object> void write(Map<K, V> map, String fileName, String header) throws FileNotFoundException {
		TreeMap<K, V> newMap = new TreeMap<K, V>(map);
		PrintStream stream = new PrintStream(fileName);
		stream.printf("%s\n", header);
		for (K key : newMap.keySet()) {
			stream.printf("%s, %s\n", key.toString(), map.get(key).toString());
		}	
		stream.close();
	}
}
