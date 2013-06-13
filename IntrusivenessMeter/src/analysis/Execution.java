package analysis;

import static commons.Preconditions.check;

public class Execution {
	private long startTime;
	private long finishTime;
	
	public Execution(long startTime, long endTime) {
		check(endTime >= startTime, "endTime must be greater than or equal to startTime.");
		this.startTime = startTime;
		this.finishTime = endTime;
	}
	
	public long getStartTime() {
		return startTime;
	}
	
	public long getFinishTime() {
		return finishTime;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Execution [startTime=");
		builder.append(startTime);
		builder.append(", finishTime=");
		builder.append(finishTime);
		builder.append("]");
		return builder.toString();
	}
}
