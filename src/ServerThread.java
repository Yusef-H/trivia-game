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
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

	    objOutputStream.writeObject(questions);
	    objOutputStream.flush();
	    
	    objOutputStream.close();
	    outputStream.close();
	}
	
	
	/* Fetch trivia questions from API. */
	private void fetchQuestions() {
	    String apiUrl = "https://opentdb.com/api.php?amount=" + QUESTION_AMOUNT + "&category=18&type=multiple";

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

	            // Pass the JSON string to the parsing method
	            parseAndPopulateQuestions(json);
	        } else {
	            System.out.println("Error: " + responseCode);
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	/* Parse the Json response (contains all questions and answers) and populate
	 * the questions list. */
	private void parseAndPopulateQuestions(String json) {
        questions = new ArrayList<>();

        Pattern questionPattern = Pattern.compile("\"question\":\"(.*?)\"");
        Pattern correctAnswerPattern = Pattern.compile("\"correct_answer\":\"(.*?)\"");
        Pattern incorrectAnswersPattern = Pattern.compile("\"incorrect_answers\":\\[(.*?)\\]");

        Matcher questionMatcher = questionPattern.matcher(json);
        Matcher correctAnswerMatcher = correctAnswerPattern.matcher(json);
        Matcher incorrectAnswersMatcher = incorrectAnswersPattern.matcher(json);

        while (questionMatcher.find() && correctAnswerMatcher.find() && incorrectAnswersMatcher.find()) {
            String question = questionMatcher.group(1);
            String correctAnswer = correctAnswerMatcher.group(1);
            String incorrectAnswersString = incorrectAnswersMatcher.group(1);

            // Remove HTML entities from the question and answers
            question = removeHTMLentities(question);
            correctAnswer = removeHTMLentities(correctAnswer);
            List<String> incorrectAnswers = removeHTMLentities(Arrays.asList(incorrectAnswersString.split(",")));

            Question q = new Question(question, incorrectAnswers.toArray(new String[0]), correctAnswer);
            questions.add(q);
        }

        // Print the parsed questions and answers
        for (Question q : questions) {
            System.out.println("Question: " + q.getQuestion());
            System.out.println("Correct Answer: " + q.getCorrectAnswer());
            System.out.println("Incorrect Answers: " + Arrays.toString(q.getIncorrectAnswers()));
            System.out.println();
        }
    }

    private String removeHTMLentities(String text) {
        return text.replaceAll("&quot;", "\"")
                .replaceAll("&#039;", "'");
    }

    private List<String> removeHTMLentities(List<String> texts) {
        List<String> cleanedTexts = new ArrayList<>();
        for (String text : texts) {
            cleanedTexts.add(removeHTMLentities(text));
        }
        return cleanedTexts;
    }
}































