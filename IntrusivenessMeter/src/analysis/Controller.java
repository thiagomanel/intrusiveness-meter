package analysis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import analysis.data.Execution;

import commons.util.LogFile;

public class Controller {
	private static final String RUNNING_BENCHMARK_MESSAGE = "True";
	private static final String NOT_RUNNING_BENCHMARK_MESSAGE = "False";
	private LogFile hadoopRunningFile;

	public Controller(String hadoopRunningInfoFileName) throws IOException {
		hadoopRunningFile = new LogFile(hadoopRunningInfoFileName);
	}

	public List<Execution> getExecutions() throws IOException {
		List<Execution> executions = new LinkedList<Execution>();
		
		do {
			// find an execution
			if (hadoopRunningFile.getMessage().contains(RUNNING_BENCHMARK_MESSAGE)) {
				// get the start time
				long startTime = hadoopRunningFile.getLineTime();
				// and find its end time
				long endTime = getExecutionEndTime(startTime);
				
				executions.add(new Execution(startTime, endTime));
			}			
		} while (hadoopRunningFile.advance());
		return executions;
	}

	private long getExecutionEndTime(long startTime) throws IOException {
		while (hadoopRunningFile.advance() && !hadoopRunningFile.getMessage().contains(NOT_RUNNING_BENCHMARK_MESSAGE));
		
		return hadoopRunningFile.getLineTime();
	}
}
