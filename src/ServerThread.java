import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread{
	ArrayList<Question> questions;
	private Socket s = null;
	
	public ServerThread(Socket socket, ArrayList<Question> questions) {
		this.s = socket;
		this.questions = questions;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			
		} catch(Exception e) {
			
		}
	}

}
