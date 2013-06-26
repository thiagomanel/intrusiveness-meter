package analysis;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import analysis.data.Execution;
import analysis.data.HadoopInformation;
import analysis.data.HadoopMachineUsage;
import analysis.data.MachineUsage;

public class ReportWriter {

	private PrintStream resultFileStream;
	
	public ReportWriter(String resultFileName) throws FileNotFoundException {
		resultFileStream = new PrintStream(resultFileName);
	}
	
	public void write(Execution execution, boolean relatedDiscomfort,
			MachineUsage machineUsage, HadoopMachineUsage hadoopMachineUsage,
			HadoopInformation hadoopInfo) {
		resultFileStream.printf("%d, %d, %b, %s\n", execution.getStartTime(), execution.getFinishTime(),
						relatedDiscomfort, hadoopInfo.getBenchmarks().toString());	
	}
}
