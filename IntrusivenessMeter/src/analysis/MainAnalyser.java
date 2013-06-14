package analysis;

import java.io.IOException;
import java.util.List;

import analysis.data.HadoopInformation;
import analysis.data.MachineUsage;

public class MainAnalyser {
	private static String[] machinesNames = new String[] {"abotoado"};
	
	private Discomfort discomfort;
	private IdleUser idle;
	private Hadoop hadoop;
	private Controller controller;
	private Machine machine;
	
	public MainAnalyser(Discomfort discomfort, IdleUser idle, Hadoop hadoop,
			Controller controller, Machine machine) {
		this.discomfort = discomfort;
		this.idle = idle;
		this.hadoop = hadoop;
		this.controller = controller;
		this.machine = machine;
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
		// TODO Auto-generated method stub
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
		return idle.idle(execution);
	}

	private List<Execution> getExecutions() throws IOException {
		return controller.getExecutions();
	}
	
	public static void main(String[] args) throws IOException {
		for (String machine : machinesNames) {
			MainAnalyser analyser = new MainAnalyser(null, null, null, null, null);
			analyser.writeReport();
		}
	}
}
