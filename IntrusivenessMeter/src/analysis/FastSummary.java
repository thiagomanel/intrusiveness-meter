package analysis;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import analysis.data.Execution;

public class FastSummary {

	private static final int SERVER_LOGS_DIRECTORY_INDEX = 1;
	private static final int MACHINE_LOGS_DIRECTORY_INDEX = 2;
	private static final int EXPECTED_ARGS_LENGTH = 3;
	
	private Controller controller;
	private Discomfort discomfort;
	private IdleUser idle;
	private Hadoop hadoop;
	
	public FastSummary(Controller controller, Discomfort discomfort, IdleUser idleUser, Hadoop hadoop) {
		this.controller = controller;
		this.discomfort = discomfort;
		this.idle = idleUser;
		this.hadoop = hadoop;
	}
	
	public void printSummary() throws IOException {
		PrintStream generalSummaryFile = new PrintStream("general_summary.txt");
		
		List<Execution> executions = controller.getExecutions();
		List<Long> discomfortTimes = discomfort.getDiscomfortTimes(new Execution(Long.MIN_VALUE, Long.MAX_VALUE));
		
		generalSummaryFile.printf("Number of executions: %d\n", executions.size());
		generalSummaryFile.printf("Number of discomfort reports: %d\n", discomfortTimes.size());
		generalSummaryFile.printf("Number of executions on machine: %d\n", countExecutionsOnMachine(executions));
		generalSummaryFile.printf("Number of valid executions on machine: %d\n", countValidExecutions(executions));
		generalSummaryFile.printf("Number of executions that caused discomfort: %d\n", countExecutionsThatCausedDiscomfort(executions));
		generalSummaryFile.printf("Number of discomforts caused by Hadoop: %d\n", countHadoopDiscomforts());
		writeBenchmarksReport(generalSummaryFile, discomfortTimes);
		
		generalSummaryFile.close();
	}

	private void writeBenchmarksReport(PrintStream generalSummaryFile,
			List<Long> discomfortTimes) {
		Map<String, Integer> discomfortsPerBenchmark = getDiscomfortPerBenchmark(discomfortTimes);
		
		generalSummaryFile.printf("------------------------\n");
		generalSummaryFile.printf("Discomfort per benchmark\n");
		generalSummaryFile.printf("------------------------\n");
		
		for (String benchmark : discomfortsPerBenchmark.keySet()) {
			generalSummaryFile.printf("%s : %d\n", benchmark, discomfortsPerBenchmark.get(benchmark));
		}
	}

	private Map<String, Integer> getDiscomfortPerBenchmark(List<Long> discomfortTimes) {
		Map<String, Integer> discomfortsPerBenchmark = new TreeMap<String, Integer>();
		
		for (long discomfortTime : discomfortTimes) {
			String runningBenchmark = hadoop.getInformation(new Execution(discomfortTime - 5000000000L, 
										discomfortTime + 5000000000L)).getFirstBenchmark();
			
			if (discomfortsPerBenchmark.get(runningBenchmark) == null) {
				discomfortsPerBenchmark.put(runningBenchmark, 0);
			}
			
			discomfortsPerBenchmark.put(runningBenchmark, discomfortsPerBenchmark.get(runningBenchmark) + 1);
		}
		
		return discomfortsPerBenchmark;
	}
	
	private int countHadoopDiscomforts() {
		int hadoopDiscomforts = 0;
	
		for (long time : discomfort.getDiscomfortTimes(new Execution(Long.MIN_VALUE, Long.MAX_VALUE))) {
			// the delta 5000000000L is used because the attempt to get the tasks of a given time
			// may fail, so I try to get in a given interval.
			if (hadoop.thereAreRunningTasks(new Execution(time - 5000000000L, time + 5000000000L))) {
				hadoopDiscomforts++;
			}
		}
		
		return hadoopDiscomforts;
	}

	private int countValidExecutions(List<Execution> executions) throws IOException {
		int validExecutions = 0;
		
		for (Execution execution : executions) {
			if (isValid(execution)) {
				validExecutions++;
			}
		}
		
		return validExecutions;
	}

	private int countExecutionsOnMachine(List<Execution> executions) {
		int executionsOnMachine = 0;
		
		for (Execution execution : executions) {		
			if (thereAreRunningTasks(execution)) {
				executionsOnMachine++;
			}
		}
		
		return executionsOnMachine;
	}

	private int countExecutionsThatCausedDiscomfort(
			List<Execution> executions) {
		int executionsThatCausedDiscomfort = 0;
		
		for (Execution execution : executions) {
			if (discomfort.reportedDiscomfort(execution)) {
				executionsThatCausedDiscomfort++;
			}
		}
		
		return executionsThatCausedDiscomfort;
	}
	
	private boolean isValid(Execution execution) {
		return !idle.idle(execution) && thereAreRunningTasks(execution);
	}

	private boolean thereAreRunningTasks(Execution execution) {
		return hadoop.thereAreRunningTasks(execution);
	}

	public static void main(String[] args) throws IOException {
		String serverLogsDirectory = getServerLogsDirectory(args);
		String machineLogsDirectory = getMachineLogsDirectory(args);

		System.out.println("Reading idleness information...");
		IdleUser idle = new IdleUser(machineLogsDirectory + "/results/user_activity.log", 100000);
		System.out.println("Finished reading idleness information.");
		System.out.println("Reading controller information...");
		Controller controller = new Controller(serverLogsDirectory + "/logs/hadoop_running.log");
		System.out.println("Finished reading controller information.");
		System.out.println("Reading discomfort information...");
		Discomfort discomfort = new Discomfort(machineLogsDirectory + "/logs/discomfort.log");
		System.out.println("Finished reading discomfort information.");
		System.out.println("Reading Hadoop information...");
		Hadoop hadoop = new Hadoop(machineLogsDirectory + "/logs/hadoop_resources_usage.cpu", machineLogsDirectory + "/logs/hadoop_resources_usage.mem", 
				serverLogsDirectory + "/logs/controller.log", machineLogsDirectory + "/logs/hadoop_processes.proc");
		System.out.println("Finished reading Hadoop information.");
		
		System.out.println("Creating FastSummary.");
		FastSummary summary = new FastSummary(controller, discomfort, idle, hadoop);
		System.out.println("Created FastSummary.");
		
		System.out.println("Printing summary.");
		summary.printSummary();
		System.out.println("Finished summary printing.");
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
	
	private static void printCorrectUsage() {
		System.out.println("Usage: analysis.MainAnalyser [server logs directory] [machine logs directory] [number of cpus] [total memory]");
	}
}
