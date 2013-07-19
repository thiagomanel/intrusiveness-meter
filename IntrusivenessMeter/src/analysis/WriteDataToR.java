package analysis;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;

public class WriteDataToR {
	
	private static final int SERVER_LOGS_DIRECTORY_INDEX = 1;
	private static final int MACHINE_LOGS_DIRECTORY_INDEX = 2;
	private static final int EXPECTED_ARGS_LENGTH = 5;
	private static final int TOTAL_MEMORY_INDEX = 4;
	private static final int CPU_NUMBER_INDEX = 3;
	private static final String CPU_DATA_FILENAME = "cpu_discomfort.csv";
	private static final String MEMORY_DATA_FILENAME = "memory_discomfort.csv";
	private static final String SEPARATOR = " ";
	
	private static final long INTERVAL_GET_DISCOMFORT_DATA = 10 * 1000000000L;
	
	public class Pair<T1, T2> {
		private T1 t1;
		private T2 t2;
		
		public Pair(T1 t1, T2 t2) {
			this.t1 = t1;
			this.t2 = t2;
		}

		public T1 getT1() {
			return t1;
		}
		
		public void setT1(T1 t1) {
			this.t1 = t1;
		}
		
		public T2 getT2() {
			return t2;
		}
		
		public void setT2(T2 t2) {
			this.t2 = t2;
		}
	}

	private Hadoop hadoop;
	private Discomfort discomfort;
	private int numberOfCPUs;
	private long machineTotalMemory;
	
	private Map<Long, Pair<Boolean, Double>> dataCPU;
	private Map<Long, Pair<Boolean, Double>> dataMemory;
	
	public WriteDataToR(Hadoop hadoop, Discomfort discomfort, int numberOfCPUs, long machineTotalMemory) {
		this.hadoop = hadoop;
		this.discomfort = discomfort;
		this.numberOfCPUs = numberOfCPUs;
		this.machineTotalMemory = machineTotalMemory;
	}

	// output : [timestamp] [cpu] [discomfort] 
	public void getDataToR() throws IOException {
		Map<Long, Pair<Boolean, Double>> resultCPU = new TreeMap<Long, Pair<Boolean,Double>>();
		Map<Long, Pair<Boolean, Double>> resultMemory = new TreeMap<Long, Pair<Boolean,Double>>();
		
		for (long discomfortTime : discomfort.getDiscomfortTimes(new Execution(Long.MIN_VALUE, Long.MAX_VALUE))) {
			double cpuUsageAtDiscomfortTime = getCPUUsage(discomfortTime);
			double memoryUsageAtDiscomfortTime = getMemoryUsage(discomfortTime);
			
			resultCPU.put(discomfortTime, new Pair<Boolean, Double>(true, cpuUsageAtDiscomfortTime));
			resultMemory.put(discomfortTime, new Pair<Boolean, Double>(true, memoryUsageAtDiscomfortTime));
		}
		
		dataCPU = resultCPU;
		dataMemory = resultMemory;
	}
	
	private double getMemoryUsage(long discomfortTime) {
		return hadoop.getNearestMemoryUsage(discomfortTime, INTERVAL_GET_DISCOMFORT_DATA);
	}

	private double getCPUUsage(long discomfortTime) {
		return hadoop.getNearestCPUUsage(discomfortTime, INTERVAL_GET_DISCOMFORT_DATA);
	}

	public static void main(String[] args) throws IOException {
		String serverLogsDirectory = getServerLogsDirectory(args);
		String machineLogsDirectory = getMachineLogsDirectory(args);
		long machineTotalMemory = getMachineTotalMemory(args);
		int numberOfCPUs = getNumberOfCPUs(args);
		
		System.out.println("Reading discomfort information...");
		Discomfort discomfort = new Discomfort(machineLogsDirectory + "/logs/discomfort.log");
		System.out.println("Finished reading discomfort information.");
		System.out.println("Reading Hadoop information...");
		Hadoop hadoop = new Hadoop(machineLogsDirectory + "/logs/hadoop_resources_usage.cpu", machineLogsDirectory + "/logs/hadoop_resources_usage.mem", 
				serverLogsDirectory + "/logs/controller.log", machineLogsDirectory + "/logs/hadoop_processes.proc");
		System.out.println("Finished reading Hadoop information.");
		
		System.out.println("Creating WriteDataToR...");
		WriteDataToR r = new WriteDataToR(hadoop, discomfort, numberOfCPUs, machineTotalMemory);
		System.out.println("Created WriteDataToR...");
		
		System.out.println("Reading data...");
		r.getDataToR();
		System.out.println("Finished data reading.");
		
		System.out.println("Writing result files...");
		r.writeResultFiles();
		System.out.println("Finished results files writing.");
	}
	
	private void writeResultFiles() throws FileNotFoundException {
		PrintStream cpuDataFile = new PrintStream(CPU_DATA_FILENAME);
		PrintStream memoryDataFile = new PrintStream(MEMORY_DATA_FILENAME);
		
		cpuDataFile.printf("timestamp cpu discomfort\n");
		memoryDataFile.printf("timestamp memory discomfort\n");
		
		for (long key : dataCPU.keySet()) {
			cpuDataFile.printf("%d%s%f%s%s\n", key, SEPARATOR, dataCPU.get(key).getT2()/numberOfCPUs, SEPARATOR, dataCPU.get(key).getT1());
		}
		
		for (long key : dataMemory.keySet()) {
			memoryDataFile.printf("%d%s%f%s%s\n", key, SEPARATOR, dataMemory.get(key).getT2()*100/machineTotalMemory, SEPARATOR, dataMemory.get(key).getT1());
		}
		
		cpuDataFile.close();
		memoryDataFile.close();
	}

	private static String getMachineLogsDirectory(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			printCorrectUsage();
			throw new IOException("Invalid number of arguments.");
		}
		return args[MACHINE_LOGS_DIRECTORY_INDEX];
	}

	private static String getServerLogsDirectory(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			printCorrectUsage();
			throw new IOException("Invalid number of arguments.");
		}
		return args[SERVER_LOGS_DIRECTORY_INDEX];
	}
	
	private static int getNumberOfCPUs(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			printCorrectUsage();
			throw new IOException("Invalid number of arguments.");
		}
		return Integer.parseInt(args[CPU_NUMBER_INDEX]);
	}

	private static long getMachineTotalMemory(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			printCorrectUsage();
			throw new IOException("Invalid number of arguments.");
		}
		return Long.parseLong(args[TOTAL_MEMORY_INDEX]);
	}
	
	private static void printCorrectUsage() {
		System.out.println("Usage: WriteDataToR [server logs directory] [machine logs directory]");
	}
}

