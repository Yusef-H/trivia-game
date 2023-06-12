import java.io.Serializable;

public class Question implements Serializable {
	String question;
	String[] incorrectAnswers;
	String correctAnswer;
	
	
	public Question(String question, String[] incorrectAnswers, String correctAnswer) {
		super();
		this.question = question;
		this.incorrectAnswers = incorrectAnswers;
		this.correctAnswer = correctAnswer;
	}

	
	public String getQuestion() {
		return question;
	}


	public String[] getIncorrectAnswers() {
		return incorrectAnswers;
	}


	public String getCorrectAnswer() {
		return correctAnswer;
	}


	public boolean isCorrectAnswer(String answer) {
		return answer.equals(correctAnswer);
	}

}
