package kingdomBuilder.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import kingdomBuilder.KBState;
import kingdomBuilder.actions.game.GameEndAction;
import kingdomBuilder.gamelogic.Player;
import kingdomBuilder.network.protocol.Scores;
import kingdomBuilder.network.protocol.ScoresData;
import kingdomBuilder.redux.Store;

import java.net.URL;
import java.util.*;

public class WinViewController extends Controller implements Initializable {

    /**
     * Represents a label for the score.
     */
    @FXML
    private Label win_label_score1;

    /**
     * Represents a label for the score.
     */
    @FXML
    private Label win_label_score2;

    /**
     * Represents a label for the score.
     */
    @FXML
    private Label win_label_score3;

    /**
     * Represents a label for the score.
     */
    @FXML
    private Label win_label_score4;

    /**
     * Represents a label for the name.
     */
    @FXML
    private Label win_label_name1;

    /**
     * Represents a label for the name.
     */
    @FXML
    private Label win_label_name2;

    /**
     * Represents a label for the name.
     */
    @FXML
    private Label win_label_name3;

    /**
     * Represents a label for the name.
     */
    @FXML
    private Label win_label_name4;

    /**
     * Constructs the WinViewController with the given store.
     * @param store the Store for access to the state.
     */
    public WinViewController(Store<KBState> store) {
        this.store = store;
    }

    /**
     * Called to initialize this controller after its root element has been completely processed.
     *
     * @param location the location used to resolve relative paths for the root object,
     *                  or null if the location is not known.
     * @param resources the resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    /**
     * Sets the score and name for every player.
     * @param scores the network message with all data.
     */
    public void setScoreWithName(Scores scores) {
        HashMap<Integer, Player> players = store.getState().playersMap();
        Collection<ScoresData> collection = new ArrayList<>();
        collection.addAll(scores.scoresDataList());

        ArrayList<ScoresData> score = (ArrayList<ScoresData>) collection;

        // the message looks like this:
        // [GAME_MESSAGE] [SCORES] <{[8;43],[7;44]}>')

        score.sort(Comparator.comparing(ScoresData::score));

        for (int i = 0; i < score.size(); i++) {
            switch (i) {
                case 0 -> {
                    // Winner
                    win_label_name1.setText(players.get(score.get(i).clientId()).name);
                    win_label_score1.setText(Integer.toString(scores.scoresDataList().get(i).score()));
                }
                case 1 -> {
                    // Winner
                    win_label_name2.setText(players.get(score.get(i).clientId()).name);
                    win_label_score2.setText(Integer.toString(scores.scoresDataList().get(i).score()));
                }
                case 2 -> {
                    // Winner
                    win_label_name3.setText(players.get(score.get(i).clientId()).name);
                    win_label_score3.setText(Integer.toString(scores.scoresDataList().get(i).score()));
                }
                case 3 -> {
                    // Winner
                    win_label_name4.setText(players.get(score.get(i).clientId()).name);
                    win_label_score4.setText(Integer.toString(scores.scoresDataList().get(i).score()));
                }
            }
        }
    }

    /**
     * Sets the functionality for the Ok Button.
     * @param event contains the data from the event source.
     */
    @FXML
    private void onOKButtonPressed(Event event) {
        sceneLoader.showGameSelectionView();
        store.dispatch(new GameEndAction());
    }
}
