package analysis;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import commons.util.LogFile;

public class Controller {

	private LogFile controllerLogFile;
	private LogFile hadoopProcessesLogFile;

	public Controller(String controllerLogFileName, String hadoopProcessesLogFileName) throws IOException {
		controllerLogFile = new LogFile(controllerLogFileName);
		hadoopProcessesLogFile = new LogFile(hadoopProcessesLogFileName);
	}

	public List<Execution> getExecutions() throws IOException {
		List<Execution> executions = new LinkedList<Execution>();
		
		do {
			// find an execution
			if (controllerLogFile.getMessage().contains("started benchmark")) {
				// get the start time
				long startTime = controllerLogFile.getLineTime();
				// and find its end time
				long endTime = getExecutionEndTime(startTime);
				
				executions.add(new Execution(startTime, endTime));
			}			
		} while (controllerLogFile.advance());
		return executions;
	}

	private long getExecutionEndTime(long startTime) throws IOException {
		// find next execution
		while (hadoopProcessesLogFile.advance() && hadoopProcessesLogFile.getMessage().contains("[]"));
				
		// find execution end
		while (hadoopProcessesLogFile.advance()) {
			if (hadoopProcessesLogFile.getMessage().contains("[]") && hadoopProcessesLogFile.getLineTime() > startTime) {
				return hadoopProcessesLogFile.getLineTime();
			}
		}
		
		throw new IOException("No execution end was found");
	}
}
