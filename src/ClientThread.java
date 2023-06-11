import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientThread extends Thread{
	private ClientController controller;
	private String ip;
	private ArrayList<Question> questions;
	
	public ClientThread(ClientController controller, String ip) {
		this.controller = controller;
		this.ip = ip;
	}
	
	public void run() {
		super.run();
		
		try {
			
			questions = getQuestionsFromServer();
			System.out.println(questions.get(0));
			System.out.println("aaaaaaaa");
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private ArrayList<Question> getQuestionsFromServer() throws Exception {
		Socket socket = new Socket(ip, 3333);
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
        // Receive questions from the server
        questions = (ArrayList<Question>) objInputStream.readObject();
        System.out.println("New thread run");

        // Close the connection
        socket.close();
        
        return questions;
	}

}
