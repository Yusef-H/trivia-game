import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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
	
	public String[] getShuffledAnswers() {
		List<String> answers = new ArrayList<String>();
        answers.add(correctAnswer);
        answers.addAll(Arrays.asList(incorrectAnswers));
        Collections.shuffle(answers);
        return answers.toArray(new String[0]);
	}


	public boolean isCorrectAnswer(String answer) {
		return answer.equals(correctAnswer);
	}

}
