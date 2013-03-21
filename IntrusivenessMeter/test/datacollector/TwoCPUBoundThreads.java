package datacollector;

public class TwoCPUBoundThreads {
	public static void main(String[] args) throws InterruptedException {
		Thread t1 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true);
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while (true);
			}
		});
		
		t1.start();
		t2.start();
		t1.join();
		t2.join();
	}
}
