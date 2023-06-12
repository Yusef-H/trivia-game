import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ClientController {

    @FXML
    private Button confirmButton;
    @FXML
    private Label firstAnswer;
    @FXML
    private Label secondAnswer;
    @FXML
    private Label thirdAnswer;
    @FXML
    private Label fourthAnswer;
    @FXML
    private Label question;
    @FXML
    private RadioButton radio1;
    @FXML
    private RadioButton radio2;
    @FXML
    private RadioButton radio3;
    @FXML
    private RadioButton radio4;
    @FXML
    private Text timer;
    @FXML
    private Text title;
    @FXML
    private ProgressBar progressBar;
    
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int remainingTime;
    private Timeline timeline;
    
    public void initialize() {
    	new ClientThread(this, "127.0.0.1").start();
    	title.setText("Trivia Game");
    }
    
    public void handleQuestions(ArrayList<Question> questions) {
    	this.questions = questions;
    	this.currentQuestionIndex = 0;
    	displayQuestion(questions.get(currentQuestionIndex));
    	remainingTime = 2; 
    	startTimer();
    }
    
    private void displayQuestion(Question question) {
    	Platform.runLater(() -> {
    		String questionText = question.getQuestion();
        	this.question.setText(questionText);
        	String[] answers = question.getShuffledAnswers();
        	firstAnswer.setText(answers[0]);
        	secondAnswer.setText(answers[1]);
        	thirdAnswer.setText(answers[2]);
        	fourthAnswer.setText(answers[3]);
    	});	
    }
    
    private void startTimer() {
        confirmButton.setDisable(false);
        
        timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    
                    @Override
                    public void handle(ActionEvent event) {
                        remainingTime--;
                        timer.setText(String.valueOf(remainingTime));
                        if(remainingTime == 5) {
                        	title.setText("Hurry Up!");
                        }
                        
                        if (remainingTime <= 0) {
                        	// Update the progress bar
                            double progress = progressBar.getProgress() + 0.05;
                            progressBar.setProgress(progress);
                            // Timer expired, handle logic for time up
                            confirmButton.setDisable(true);
                            
                            // Check answer and update score accordingly (assuming it's incorrect for time up)
                            boolean isCorrect = false;
                            updateScore(isCorrect);
                            
                            // Move on to the next question
                            currentQuestionIndex++;
                            if (currentQuestionIndex < questions.size()) {
                            	remainingTime = 2; // Reset the remaining time for the next question
                                displayQuestion(questions.get(currentQuestionIndex));
                                confirmButton.setDisable(false);
                                timeline.stop();
                                startTimer();
                            } else {
                                // All questions finished, display the final score
                                displayFinalScore();
                            }
                        }
                    }
                })
        );
        timeline.setCycleCount(remainingTime);
        timeline.play();
    }
    
    private void updateScore(boolean isCorrect) {
    	// Update the score based on the user's answer
    }
    
    private void displayFinalScore() {
    	// Display the final score to the user
    }

    // Other methods and event handlers...
}


