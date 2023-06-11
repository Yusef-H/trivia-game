import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
	public Server() {
		ServerSocket sc = null;
		Socket s = null;
		
		try {
			sc = new ServerSocket(3333);
			System.out.println("Server listening on port 3333");
			while(true) {
				s = sc.accept();
				new ServerThread(s).start();
			}
		} catch(IOException e) {
			try {
				sc.close();
			} catch(IOException ec) {
				ec.printStackTrace();
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		new Server();
	}
}
