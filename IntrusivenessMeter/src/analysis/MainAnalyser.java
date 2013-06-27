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
		return !idle.idle(execution);
	}

	private List<Execution> getExecutions() throws IOException {
		return controller.getExecutions();
	}
	
	public static void main(String[] args) throws IOException {
		IdleUser idle = new IdleUser("client_logs/results/user_activity.log", 100000);
		System.out.println("idle");
		Controller controller = new Controller("server_logs/logs/hadoop_running.log");
		System.out.println("controller");
		Discomfort discomfort = new Discomfort("client_logs/logs/discomfort.log");
		System.out.println("discomfort");
		Machine machine = new Machine("whatever", "client_logs/results/system_monitoring_system.idlecpu",  "client_logs/results/system_monitoring_system.usercpu", 
				"client_logs/results/system_monitoring_system.mem",  "client_logs/results/system_monitoring_system.read",  "client_logs/results/system_monitoring_system.write");
		System.out.println("machine");
		Hadoop hadoop = new Hadoop("client_logs/logs/hadoop_resources_usage.cpu", "client_logs/logs/hadoop_resources_usage.mem", "server_logs/logs/controller.log");
		System.out.println("hadoop");
		ReportWriter reportWriter = new ReportWriter("result.csv");
		System.out.println("writer");
		
		MainAnalyser analyser = new MainAnalyser(discomfort, idle, hadoop, controller, machine, reportWriter);

		analyser.writeReport();
	}
}
