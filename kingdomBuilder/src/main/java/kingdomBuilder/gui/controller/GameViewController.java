package kingdomBuilder.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import kingdomBuilder.gui.gameboard.TextureLoader;
import kingdomBuilder.model.TileType;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class controls all functions for the GameView.
 */
public class GameViewController extends Controller implements Initializable {
    /**
     * Represents the texture loader which all hexagons share.
     */
    private static final TextureLoader textureLoader = new TextureLoader();

    /**
     * Represents the initial VBox.
     */
    @FXML
    private VBox game_vbox;

    /**
     * Represents the HBox that contains the statistics of the game at the top of the screen.
     */
    @FXML
    private HBox game_hbox_statistic;
    /**
     * Represents the Label for the name of player 1.
     */
    @FXML
    private Label game_label_player1_name;
    /**
     * Represents the Label for the settlements of player 1.
     */
    @FXML
    private Label game_label_player1_settlements;
    /**
     * Represents the Label for the score of player 1.
     */
    @FXML
    private Label game_label_player1_score;
    /**
     * Represents the Label for the name of player 2.
     */
    @FXML
    private Label game_label_player2_name;
    /**
     * Represents the Label for the settlements of player 2.
     */
    @FXML
    private Label game_label_player2_settlements;
    /**
     * Represents the Label for the score of player 2.
     */
    @FXML
    private Label game_label_player2_score;
    /**
     * Represents the Label for the name of player 3.
     */
    @FXML
    private Label game_label_player3_name;
    /**
     * Represents the Label for the settlements of player 3.
     */
    @FXML
    private Label game_label_player3_settlements;
    /**
     * Represents the Label for the score of player 3.
     */
    @FXML
    private Label game_label_player3_score;
    /**
     * Represents the Label for the name of player 4.
     */
    @FXML
    private Label game_label_player4_name;
    /**
     * Represents the Label for the settlements of player 4.
     */
    @FXML
    private Label game_label_player4_settlements;
    /**
     * Represents the Label for the score of player 4.
     */
    @FXML
    private Label game_label_player4_score;

    /**
     * Represents the Label for the turn counter.
     */
    @FXML
    private Label game_label_turn;
    /**
     * Represents the Label for the time.
     */
    @FXML
    private Label game_label_time;
    /**
     * Represents the HBox that contains the subscene
     */
    @FXML
    private HBox game_hbox_subscene;
    /**
     * Represents the SubScene to show the board.
     */
    @FXML
    private SubScene game_subscene;
    /**
     * Represents the HBox that contains every player specific information.
     */
    @FXML
    private HBox game_hbox_playerinformation;
    /**
     * Represents the VBox to display the terrain card.
     */
    @FXML
    private VBox game_vbox_terraincard;
    /**
     * Represents the Rectangle for the terrain image.
     */
    @FXML
    private Rectangle game_rectangle_card;
    /**
     * Represents the Label that describes the terrain type.
     */
    @FXML
    private Label game_label_carddescribtion;
    /**
     * Represents the Hbox that contains the tokens.
     */
    @FXML
    private HBox gameview_hbox_tokens;
    /**
     * Represents the Button to end the turn.
     */
    @FXML
    private Button game_button_end;


    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

    /**
     * Represents if the view is in spectating mode.
     */
    private boolean isSpectating;

    /**
     * Represents if the view is in online mode.
     */
    private boolean isOnline;

    /**
     * Called to initialize this controller after its root element has been completely processed.
     * @param location The location used to resolve relative paths for the root object,
     *                 or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        resourceBundle = resources;
        setupLayout();
    }

    /**
     * Changes some functionalities when spectating or playing.
     */
    private void playingOrSpectating() {
        if (isSpectating) {
            game_button_end.setText(resourceBundle.getString("endSpectate"));
            game_button_end.setOnAction(this::onSpectateEndButtonPressed);
        } else {
            game_button_end.setText(resourceBundle.getString("endTurn"));
            game_button_end.setOnAction(this::onTurnEndButtonPressed);
        }
    }

    /**
     * Initialize layout arrangement.
     */
    private void setupLayout() {
        //important: the order in which the bind-operations are called

        // resize the HBox for statistic at top and player information on the bottom
        game_hbox_statistic.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.1));
        game_hbox_subscene.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.7));
        game_hbox_playerinformation.prefHeightProperty().bind(game_vbox.heightProperty().multiply(0.15));


        //resize VBox that contains the information for the current terrain type (picture, text)
        game_vbox_terraincard.prefWidthProperty().bind(game_vbox.widthProperty().multiply(0.15));
        game_vbox_terraincard.prefHeightProperty().bind(game_hbox_playerinformation.heightProperty().multiply(0.8));

        // resize rectangle that contains the Image for the current terrain type
        game_rectangle_card.widthProperty().bind(game_vbox_terraincard.widthProperty().multiply(0.8));
        game_rectangle_card.heightProperty().bind(game_vbox_terraincard.heightProperty().multiply(0.7));

        // resize the subScene
        game_subscene.widthProperty().bind(game_hbox_subscene.widthProperty());
        game_subscene.heightProperty().bind(game_hbox_subscene.heightProperty());
    }

    /**
     * Updates the Card to the current card of the turn
     * @param tileType
     */
    private void cardDescription(TileType tileType) {
        // TODO read TileType from Datalogic/Store instead of parameter

        //Update Image
        Image img = textureLoader.getTexture(tileType);
        game_rectangle_card.setFill(new ImagePattern(img, 0.0f, 0.0f, 1.0f, 1.0f, true));

        //Update Text
        String cardDescription = "";
        switch(tileType) {
            case GRAS -> cardDescription = resourceBundle.getString("gras");
            case FLOWER -> cardDescription = resourceBundle.getString("flower");
            case DESERT -> cardDescription = resourceBundle.getString("desert");
            case CANYON -> cardDescription = resourceBundle.getString("canyon");
            case FORREST -> cardDescription = resourceBundle.getString("forrest");
        }
        game_label_carddescribtion.setText(cardDescription);
    }

    /**
     * Gets the SubScene for the game board.
     * @return SubScene for displaying the game board
     */
    public SubScene getGame_subscene() {
        return this.game_subscene;
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending the turn.
     * @param actionEvent the triggered event.
     */
    public void onTurnEndButtonPressed(ActionEvent actionEvent) {
    }

    /**
     * Sets the functions for the "End"-Button. Here for ending spectating
     * @param actionEvent the triggered event.
     */
    public void onSpectateEndButtonPressed(ActionEvent actionEvent) {
        // TODO: Network Message "unspectate"
        if (isOnline) {
            sceneLoader.showGameSettingsView(true);
        } else {
            // TODO: Network End internal server
            sceneLoader.showMenuView();
        }
    }

    /**
     * Set the spectating value, if the game view is for a spectating game.
     * @param spectating If the game is observed by a spectator.
     */
    public void setSpectating(boolean spectating) {
        this.isSpectating = spectating;
    }

    /**
     * Sets the online value, if the game is on an online server.
     * @param isOnline If the game is an online game.
     */
    public void setIsOnline(boolean isOnline) {
        this.isOnline = isOnline;
        playingOrSpectating();
    }
}
