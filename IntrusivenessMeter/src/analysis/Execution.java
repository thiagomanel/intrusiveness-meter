package analysis;

public class Execution {
	private long startTime;
	private long finishTime;
	
	public Execution(long startTime, long endTime) {
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
