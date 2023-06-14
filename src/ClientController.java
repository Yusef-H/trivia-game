import java.util.ArrayList;
import java.util.Optional;

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
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class ClientController {

	@FXML
    private ToggleGroup answers;
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
    @FXML
    private Label scoreLabel;
    @FXML
    private Button newGameButton;
    
    private ArrayList<Question> questions;
    private int currentQuestionIndex;
    private int remainingTime;
    private Timeline timeline;
    private int scoreVal;
    
    private int questionTime = 60;
    
    public void initialize() {
    	String ipAddress = getIpAddress();
    	questionTime = getTimeForQuestion();
    	scoreVal = 0;
    	scoreLabel.setText("Score: "+scoreVal);
    	new ClientThread(this, ipAddress).start();
    	title.setText("Trivia Game");
    	newGameButton.setDisable(true);
    	
    }
    
    private String getIpAddress() {
    	TextInputDialog dialog = new TextInputDialog("127.0.0.1");
    	dialog.setHeaderText("Enter ip address");
    	
    	Optional<String> ip = dialog.showAndWait();
    	if(ip.isPresent()) {
    		return ip.get();
    	}
    	else {
    		return "127.0.0.1";
    	}
    }
    
    private int getTimeForQuestion() {
	    TextInputDialog dialog = new TextInputDialog("20");
	    dialog.setHeaderText("Enter time (in seconds)");
	    dialog.setContentText("Time:");

	    Optional<String> time = dialog.showAndWait();
	    if (time.isPresent()) {
	        try {
	            return Integer.parseInt(time.get());
	        } catch (NumberFormatException e) {
	        	// incase of invalid input time, return default time.
	            return questionTime;
	        }
	    }

	    return questionTime; // Default time
    }
    
    public void handleQuestions(ArrayList<Question> questions) {
    	this.questions = questions;
    	this.currentQuestionIndex = 0;
    	displayQuestion(questions.get(currentQuestionIndex));
    	remainingTime = questionTime; 
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
                        if(remainingTime == 10) {
                        	title.setText("Hurry Up!");
                        }
                        
                        if (remainingTime <= 0) {
                        	// Update the progress bar
                            double progress = progressBar.getProgress() + 0.05;
                            progressBar.setProgress(progress);
                            
                            // Check answer and update score accordingly (assuming it's incorrect for time up)
                            boolean isCorrect = false;
                            updateScore(isCorrect);
                            title.setText("Pick a solution then Press Confirm");
                            
                            // Move on to the next question
                            currentQuestionIndex++;
                            if (currentQuestionIndex < questions.size()) {
                            	remainingTime = questionTime; // Reset the remaining time for the next question
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
    
    /* Update the score based on the user's answer */
    private void updateScore(boolean isCorrect) {
    	if(isCorrect) {
    		scoreVal+= 10;
    	}
    	else {
    		scoreVal-= 5;
    	}
    	scoreLabel.setText("Score: " + scoreVal);
    }
    
    @FXML
    void confirmPressed(ActionEvent event) {
    	RadioButton selectedRadioButton = (RadioButton) answers.getSelectedToggle();
    	Label selectedLabel = null;

    	if (selectedRadioButton == radio1) {
    	    selectedLabel = firstAnswer;
    	} else if (selectedRadioButton == radio2) {
    	    selectedLabel = secondAnswer;
    	} else if (selectedRadioButton == radio3) {
    	    selectedLabel = thirdAnswer;
    	} else if (selectedRadioButton == radio4) {
    	    selectedLabel = fourthAnswer;
    	}

    	if (selectedLabel != null) {
    	    String selectedAnswer = selectedLabel.getText();
    	    System.out.println(selectedAnswer);
    	}
    	else {
    		return;
    	}


        // Check if the selected answer is correct
        boolean isCorrect = questions.get(currentQuestionIndex).isCorrectAnswer(selectedLabel.getText());

        // Update the score based on the selected answer
        updateScore(isCorrect);

        // Move on to the next question
        currentQuestionIndex++;
        double progress = progressBar.getProgress() + 0.05;
        progressBar.setProgress(progress);
        if (currentQuestionIndex < questions.size()) {
            remainingTime = questionTime; // Reset the remaining time for the next question
            displayQuestion(questions.get(currentQuestionIndex));
            confirmButton.setDisable(false);
            timeline.stop();
            startTimer();
        } else {
            // All questions finished, display the final score
            displayFinalScore();
        }
    }
    
    @FXML
    void newGamePressed(ActionEvent event) {
    	// Reset the game state
        currentQuestionIndex = 0;
        remainingTime = questionTime;
        progressBar.setProgress(0.0);
        scoreVal = 0;
        scoreLabel.setText("Score: 0");
        title.setText("Trivia Game");
        
        //start a new game
        new ClientThread(this, "127.0.0.1").start();

        // disable the new game button
        newGameButton.setDisable(true);
    }
    
    
    private void displayFinalScore() {
    	confirmButton.setDisable(true);
    	timeline.stop();
    	title.setText("Press New Game to Play Again!");
    	newGameButton.setDisable(false);
    	
    }

}


