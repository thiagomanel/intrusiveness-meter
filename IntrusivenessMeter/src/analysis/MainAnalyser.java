package analysis;

import java.io.IOException;
import java.util.List;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class MainAnalyser {
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
		String serverLogsDirectory = "all/ourico/08-07-2013-10-55-44";
		String machineLogsDirectory = "all/abelhinha/07-07-2013-01-38-06";
		
		IdleUser idle = new IdleUser(machineLogsDirectory + "/results/user_activity.log", 100000);
		System.out.println("idle");
		Controller controller = new Controller(serverLogsDirectory + "/logs/hadoop_running.log");
		System.out.println("controller");
		Discomfort discomfort = new Discomfort(machineLogsDirectory + "/logs/discomfort.log");
		System.out.println("discomfort");
		Machine machine = new Machine("jurupoca", machineLogsDirectory + "/results/system_monitoring_system.idlecpu",  machineLogsDirectory + "/results/system_monitoring_system.usercpu", 
				machineLogsDirectory + "/results/system_monitoring_system.mem",  machineLogsDirectory + "/results/system_monitoring_system.read",  machineLogsDirectory + "/results/system_monitoring_system.write");
		System.out.println("machine");
		Hadoop hadoop = new Hadoop(machineLogsDirectory + "/logs/hadoop_resources_usage.cpu", machineLogsDirectory + "/logs/hadoop_resources_usage.mem", 
				serverLogsDirectory + "/logs/controller.log", machineLogsDirectory + "/logs/hadoop_processes.proc");
		System.out.println("hadoop");
		ReportWriter reportWriter = new ReportWriter("result.csv");
		System.out.println("writer");
		
		MainAnalyser analyser = new MainAnalyser(discomfort, idle, hadoop, controller, machine, reportWriter);

		analyser.writeReport();
	}
}
