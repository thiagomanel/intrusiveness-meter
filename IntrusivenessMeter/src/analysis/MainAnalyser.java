package analysis;

import java.io.IOException;
import java.util.List;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class MainAnalyser {
	private static String[] machinesNames = new String[] {"abotoado"};
	
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
		//IdleUser idle = new IdleUser("client_logs/results/user_activity.log", 100000);
		//System.out.println(user.idle(new Execution(1372200225749925220L, 1372246023085740102L)));
		//Controller controller = new Controller("server_logs/logs/hadoop_running.log");
		
		//List<Execution> exec = controller.getExecutions();
		
		//System.out.println(exec.size());
		//System.out.println(exec.get(0));
		//System.out.println(exec.get(1));
		//Discomfort d = new Discomfort("client_logs/logs/discomfort.log");
		//System.out.println(d.reportedDiscomfort(new Execution(1372179181530577415L, 1372179929410674034L)));
		//Machine m = new Machine("whatever", "client_logs/results/system_monitoring_system.idlecpu",  "client_logs/results/system_monitoring_system.usercpu", 
				//"client_logs/results/system_monitoring_system.mem",  "client_logs/results/system_monitoring_system.read",  "client_logs/results/system_monitoring_system.write");
		//Hadoop h = new Hadoop("client_logs/logs/hadoop_resources_usage.cpu", "client_logs/logs/hadoop_resources_usage.mem", "server_logs/logs/controller.log");
		
		//System.out.println(m.getUsage(new Execution(0, 1372245779562897240L)));
		//System.out.println("Information");
		//System.out.println(h.getInformation(new Execution(0, 1372245857993850343L)));
		
		//System.out.println();
		//System.out.println("Machine Usage");
		//System.out.println(h.getMachineUsage(new Execution(1372178774072434335L, 1372245857993850343L)));
		//System.out.println(d.reportedDiscomfort(new Execution(0, 1371228352166341369L)));
		//System.out.println(controller.getExecutions());
		/*for (String machine : machinesNames) {
			MainAnalyser analyser = new MainAnalyser(null, null, null, null, null, null);
			analyser.writeReport();
		}*/
		/*int missed = 0;
		int worked = 0;
		for (Execution execution : controller.getExecutions()) {
			
			if (!idle.idle(execution)) {
				worked++;
				boolean relatedDiscomfort = d.reportedDiscomfort(execution);
				HadoopMachineUsage hadoopUsage = h.getMachineUsage(execution);
				//System.out.println(relatedDiscomfort);
				//System.out.println(hadoopUsage.getCPU().values());
				//System.out.println(hadoopUsage.getMemory().values());
			} else {
				missed++;
				///System.out.println("missed!!!!!!!!!!!!!!!!");
			}
		}
		System.out.println("missed:" + missed);
		System.out.println("worked:" + worked);*/
		
	}
}
