import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.text.Text;

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
    
    public void initialize() {
    	System.out.println("OOOOOOOOOOOOOOOOO");
    	new ClientThread(this, "127.0.0.1").start();
    }

}

