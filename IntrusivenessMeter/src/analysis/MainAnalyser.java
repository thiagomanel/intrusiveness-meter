package analysis;

import java.io.IOException;
import java.util.List;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class MainAnalyser {
	private static final int EXPECTED_ARGS_LENGTH = 5;
	private static final int SERVER_LOGS_DIRECTORY_INDEX = 1;
	private static final int MACHINE_LOGS_DIRECTORY_INDEX = 2;
	private static final int TOTAL_MEMORY_INDEX = 4;
	private static final int CPU_NUMBER_INDEX = 3;
	private Discomfort discomfort;
	private IdleUser idle;
	private Hadoop hadoop;
	private Controller controller;
	private Machine machine;
	private ReportWriter reportWriter;
	
	public MainAnalyser(Discomfort discomfort, IdleUser idle, Hadoop hadoop,
			Controller controller, Machine machine, ReportWriter reportWriter) {
		this.discomfort = discomfort;
		this.idle = idle;
		this.hadoop = hadoop;
		this.controller = controller;
		this.machine = machine;
		this.reportWriter = reportWriter;
	}

	public void writeReport() throws IOException {
		List<Execution> executions = getExecutions();
		for (Execution execution : executions) {
			if (isValid(execution)) {
				boolean relatedDiscomfort = getDiscomfort(execution);
				MachineUsage machineUsage = getMachineUsage(execution);
				HadoopMachineUsage hadoopMachineUsage = getHadoopMachineUsage(execution);
				HadoopInformation hadoopInfo = getHadoopInformation(execution);
				
				writeExecutionReport(execution, relatedDiscomfort, machineUsage, hadoopMachineUsage, hadoopInfo);
			}				
		}
	}

	private HadoopMachineUsage getHadoopMachineUsage(Execution execution) {
		return hadoop.getMachineUsage(execution);
	}

	private void writeExecutionReport(Execution execution, boolean relatedDiscomfort,
			MachineUsage machineUsage, HadoopMachineUsage hadoopMachineUsage, HadoopInformation hadoopInfo) {
		reportWriter.write(execution, relatedDiscomfort, machineUsage, hadoopMachineUsage, hadoopInfo);
	}

	private HadoopInformation getHadoopInformation(Execution execution) {
		return hadoop.getInformation(execution);
	}

	private MachineUsage getMachineUsage(Execution execution) {
		return machine.getUsage(execution);
	}

	private boolean getDiscomfort(Execution execution) {
		return discomfort.reportedDiscomfort(execution);
	}

	private boolean isValid(Execution execution) {
		return !idle.idle(execution) && thereAreRunningTasks(execution);
	}

	private boolean thereAreRunningTasks(Execution execution) {
		return hadoop.thereAreRunningTasks(execution);
	}

	private List<Execution> getExecutions() throws IOException {
		return controller.getExecutions();
	}
	
	public static void main(String[] args) throws IOException {
		String serverLogsDirectory = getServerLogsDirectory(args);
		String machineLogsDirectory = getMachineLogsDirectory(args);
		long machineTotalMemory = getMachineTotalMemory(args);
		int numberOfCPUs = getNumberOfCPUs(args);
		
		System.out.println("Reading idleness information...");
		IdleUser idle = new IdleUser(machineLogsDirectory + "/results/user_activity.log", 100000);
		System.out.println("Finished reading idleness information.");
		System.out.println("Reading controller information...");
		Controller controller = new Controller(serverLogsDirectory + "/logs/hadoop_running.log");
		System.out.println("Finished reading controller information.");
		System.out.println("Reading discomfort information...");
		Discomfort discomfort = new Discomfort(machineLogsDirectory + "/logs/discomfort.log");
		System.out.println("Finished reading discomfort information.");
		System.out.println("Reading machine resources usage information...");
		Machine machine = new Machine("jurupoca", machineLogsDirectory + "/results/system_monitoring_system.idlecpu",  machineLogsDirectory + "/results/system_monitoring_system.usercpu", 
				machineLogsDirectory + "/results/system_monitoring_system.mem",  machineLogsDirectory + "/results/system_monitoring_system.read",  machineLogsDirectory + "/results/system_monitoring_system.write");
		System.out.println("Finished reading machine resources usage information.");
		System.out.println("Reading Hadoop information...");
		Hadoop hadoop = new Hadoop(machineLogsDirectory + "/logs/hadoop_resources_usage.cpu", machineLogsDirectory + "/logs/hadoop_resources_usage.mem", 
				serverLogsDirectory + "/logs/controller.log", machineLogsDirectory + "/logs/hadoop_processes.proc");
		System.out.println("Finished reading Hadoop information.");
		System.out.println("Creating report writer...");
		ReportWriter reportWriter = new ReportWriter("result.csv");
		System.out.println("Created report writer.");
		
		System.out.println("Creating MainAnalyser...");
		MainAnalyser analyser = new MainAnalyser(discomfort, idle, hadoop, controller, machine, reportWriter);
		System.out.println("Created MainAnalyser.");
		
		System.out.println("Writing report...");
		analyser.writeReport();
		System.out.println("Finished report writing.");
		
		System.out.println("Creating Clustering...");
		Clustering clustering = new Clustering(hadoop, discomfort, controller.getExecutions(), machineTotalMemory, numberOfCPUs);
		System.out.println("Created Clustering.");
		
		System.out.println("Writing CPU usage Discomfort probability report...");
		reportWriter.write(clustering.getHadoopCPUUsageDiscomfortProbability(), "cpu_usage_discomfort_probability.csv");
		System.out.println("Finished CPU usage Discomfort probability report writing.");
		System.out.println("Writing memory usage Discomfort probability report...");
		reportWriter.write(clustering.getMemoryUsageDiscomfortProbability(), "memory_usage_discomfort_probability.csv");
		System.out.println("Finished memory usage Discomfort probability report writing.");
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

	private static String getMachineLogsDirectory(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			printCorrectUsage();
			throw new IOException("Invalid number of arguments.");
		}
		return args[MACHINE_LOGS_DIRECTORY_INDEX];
	}

	private static String getServerLogsDirectory(String[] args) throws IOException {
		if (args.length != EXPECTED_ARGS_LENGTH) {
			throw new IOException("Invalid number of arguments.");
		}
		return args[SERVER_LOGS_DIRECTORY_INDEX];
	}
	
	private static void printCorrectUsage() {
		System.out.println("Usage: analysis.MainAnalyser [server logs directory] [machine logs directory] [number of cpus] [total memory]");
	}
}
