import java.io.Serializable;

public class Question implements Serializable {
	String question;
	String[] incorrectAnswers;
	String correctAnswer;
	
	public boolean isCorrectAnswer(String answer) {
		return answer.equals(correctAnswer);
	}

}
