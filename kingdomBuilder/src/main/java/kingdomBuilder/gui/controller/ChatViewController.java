package kingdomBuilder.gui.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.ResourceBundle;

// TODO: naming conventions (variables, methods, views/classes), also in .fxml files (fx:id)

public class ChatViewController implements Initializable {

    @FXML
    private TextArea chat_view_ta_chat;

    @FXML
    private TextField chat_view_tf_chat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void onEnterPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            printMessageOnTextArea();
        }
    }

    private void printMessageOnTextArea() {
        String msg = chat_view_tf_chat.getText();
        if (!msg.isEmpty()) {
            chat_view_ta_chat.appendText(msg);
            chat_view_ta_chat.appendText(System.lineSeparator());
            chat_view_tf_chat.clear();
        }
    }
}
