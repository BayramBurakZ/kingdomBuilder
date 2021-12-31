package kingdomBuilder.gui.controller;

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
    private Button game_button_endturn;

    /**
     * Represents the texture loader which all hexagons share.
     */
    private static final TextureLoader textureLoader = new TextureLoader();

    /**
     * Represents the resourceBundle that used for language support.
     */
    private ResourceBundle resourceBundle;

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
    private void updateTerrainCard(TileType tileType) {
        // TODO read TileType from Datalogic/Store instead of parameter

        //Update Image
        Image img = textureLoader.getTexture(tileType);
        game_rectangle_card.setFill(new ImagePattern(img, 0.0f, 0.0f, 1.0f, 1.0f, true));

        //Update Text
        String cardDescribtion = "";
        switch(tileType) {
            case GRAS -> cardDescribtion = resourceBundle.getObject("gras").toString();
            case FLOWER -> cardDescribtion = resourceBundle.getObject("flower").toString();
            case DESERT -> cardDescribtion = resourceBundle.getObject("desert").toString();
            case CANYON -> cardDescribtion = resourceBundle.getObject("canyon").toString();
            case FORREST -> cardDescribtion = resourceBundle.getObject("forrest").toString();
        }
        game_label_carddescribtion.setText(cardDescribtion);
    }

    /**
     * Gets the SubScene for the game board.
     * @return SubScene for displaying the game board
     */
    public SubScene getGame_subscene() {
        return this.game_subscene;
    }
}
