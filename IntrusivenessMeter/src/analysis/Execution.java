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
}
