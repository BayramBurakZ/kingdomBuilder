package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import javafx.event.ActionEvent;

import java.net.URL;
import java.util.ResourceBundle;


public class GameScreenController implements Initializable {

    boolean GameScreenAsCurrentWindow = true;

    @FXML
    private AnchorPane anchorPaneRoot;

    @FXML
    private SplitPane splitPaneMain;

    @FXML
    private BorderPane borderPaneMenu;

    @FXML
    private BorderPane borderPaneGame;

    @FXML
    private TextArea textAreaChatDisplay;

    @FXML
    private TextField textFieldChatBox;

    @FXML
    private void onEnterPressed(KeyEvent event){
        if(event.getCode() == KeyCode.ENTER)
            printMessageOnTextArea();
    }

    private void printMessageOnTextArea(){
        String msg = textFieldChatBox.getText();

        if(!msg.isEmpty()){
            textAreaChatDisplay.appendText(msg);
            textAreaChatDisplay.appendText(System.lineSeparator());
            textFieldChatBox.clear();
        }
    }

    // only for demonstration

    /**
     * This methode switches the GameBorderPane with the MenuBorderPane
     * and dis-/enable them.
     *
     * @author Burak & Tom
     * @param event incoming ActionEvent
     */
    @FXML
    private void switchPanes(ActionEvent event){
        if(GameScreenAsCurrentWindow){
            //switch the enable-state of the panes
            borderPaneGame.setDisable(!borderPaneGame.isDisable());
            borderPaneMenu.setDisable(!borderPaneMenu.isDisable());
            //set the opacity to display only one pane
            borderPaneGame.setOpacity(0);
            borderPaneMenu.setOpacity(1);
            GameScreenAsCurrentWindow = false;
        }
        else{
            //switch the enable-state of the panes
            borderPaneGame.setDisable(!borderPaneGame.isDisable());
            borderPaneMenu.setDisable(!borderPaneMenu.isDisable());
            //set the opacity to display only one pane
            borderPaneGame.setOpacity(1);
            borderPaneMenu.setOpacity(0);
            GameScreenAsCurrentWindow = true;
        }
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    // there is a better solution ...
    public void resizePanes(Number width, Number height) {
        double w = (double) width;
        double h = (double) height;
        anchorPaneRoot.resize(w, h);
        splitPaneMain.setPrefSize(w, h);
    }
}

