package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import kingdomBuilder.model.ClientDAO;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatViewController implements Initializable {
    private String playerName;
    private List<String> clients = new ArrayList<>();

    @FXML
    private TableView<ClientDAO> tableview_chat;

    @FXML
    private TableColumn<ClientDAO, String> column_id;

    @FXML
    private TableColumn<ClientDAO, String> column_name;

    @FXML
    private TableColumn<ClientDAO, String> column_gameid;

    @FXML
    private Tab tab_global;

    @FXML
    private Tab tab_game;

    @FXML
    private TextArea chatview_textarea_chatinput;

    @FXML
    private TextArea textarea_globalchat;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        playerName = "Player 1";
        clients.add(playerName);

        column_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_gameid.setCellValueFactory(new PropertyValueFactory<>("gameid"));

        ClientDAO clientDAO = new ClientDAO(1337, "Dude", 1);

        tableview_chat.getItems().add(clientDAO);
    }

    public void onButtonSendPressed(Event event) {
        chatview_textarea_chatinput.appendText(System.lineSeparator());
        printMessage();
    }

    public void onEnterPressed(KeyEvent event) {
        if (event.isShiftDown() && event.getCode().equals(KeyCode.ENTER)) {
            System.out.println("Shift linebreak");
            chatview_textarea_chatinput.appendText(System.lineSeparator());
        } else if (event.getCode() == KeyCode.ENTER) {
            printMessage();
        }
    }

    private void printMessage() {
        String message = chatview_textarea_chatinput.getText().trim();
        if (!message.isEmpty()) {
            System.out.println(message);
            textarea_globalchat.appendText(message);
            textarea_globalchat.appendText(System.lineSeparator());
        }
        chatview_textarea_chatinput.clear();
    }
}
