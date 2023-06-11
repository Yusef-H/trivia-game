import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;

public class ServerThread extends Thread{
	private static final int QUESTION_AMOUNT = 20;
	ArrayList<Question> questions;
	private Socket s = null;
	
	public ServerThread(Socket socket) {
		this.s = socket;
	}
	
	@Override
	public void run() {
		super.run();
		try {
			fetchQuestions();
			handleTriviaQuestions();
		} catch(Exception e) {
			
		}
	}
	
	private void handleTriviaQuestions() throws Exception {
		OutputStream outputStream = s.getOutputStream();
		ObjectOutputStream objOutputStream = new ObjectOutputStream(outputStream);
//		InputStream inputStream = s.getInputStream();
//		ObjectInputStream objInputStream = new ObjectInputStream(inputStream);
		
		// send questions to the client
	    objOutputStream.writeObject(questions);
	    objOutputStream.flush();
	}
	
	
	/* Fetch trivia questions from API. */
	private void fetchQuestions() {
        String apiUrl = "https://opentdb.com/api.php?amount=" + QUESTION_AMOUNT + "&difficulty=medium&type=multiple";

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get response code
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Parse the JSON response 
                String json = response.toString();
                int startIndex = json.indexOf("[");
                int endIndex = json.lastIndexOf("]");
                json = json.substring(startIndex, endIndex + 1);
                /* now json contains the list of trivia questions
                 * so its time to parse them into Question objects to put in
                 * the questions arraylist. */
                
                parseAndPopulateQuestions(json);
            
                

            } else {
                System.out.println("Error: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	/* Parse the json response into the question text and 3 incorrect answers and 1 correct
	 * answer then create a Question object and add it to the list. */
	private void parseAndPopulateQuestions(String json) {
		questions = new ArrayList<>();
        int currentIndex = 0;
        int questionStartIndex = 0;

        while (questionStartIndex != -1) {
            int questionEndIndex = json.indexOf("\",", questionStartIndex);
            String questionText = json.substring(questionStartIndex + 12, questionEndIndex);

            int correctAnswerStartIndex = json.indexOf("\"correct_answer\":", questionEndIndex);
            int correctAnswerEndIndex = json.indexOf("\",", correctAnswerStartIndex);
            String correctAnswer = json.substring(correctAnswerStartIndex + 18, correctAnswerEndIndex);

            int incorrectAnswersStartIndex = json.indexOf("\"incorrect_answers\":", correctAnswerEndIndex);
            int incorrectAnswersEndIndex = json.indexOf("]", incorrectAnswersStartIndex);
            String incorrectAnswersString = json.substring(incorrectAnswersStartIndex + 20, incorrectAnswersEndIndex);

            String[] incorrectAnswers = incorrectAnswersString.split("\",\"");
            incorrectAnswers[0] = incorrectAnswers[0].substring(1);  // Remove leading double
            Question question = new Question(questionText, incorrectAnswers, correctAnswer);
            questions.add(question);

            currentIndex = questionStartIndex;
            questionStartIndex = json.indexOf("\"question\":", currentIndex + 1);
            
        }
	}

}
