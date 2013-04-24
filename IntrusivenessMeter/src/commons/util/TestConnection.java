package commons.util;
import java.io.IOException;
import java.net.ServerSocket;

public class TestConnection {

	private static ServerSocket socket;
	
	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		socket = new ServerSocket(8001);
		
		socket.accept();
	}
}
