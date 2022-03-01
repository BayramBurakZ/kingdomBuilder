package kingdomBuilder.gui.gameboard;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import kingdomBuilder.gamelogic.PlayerColor;

/**
 * Class to create an information panel for a player.
 */
public class PlayerInformation extends HBox{

    /**
     * Represents the Label for the players name.
     */
    private Label name;
    /**
     * Represents the settlement which the player has left.
     */
    private Label settlements = new Label("40");
    /**
     * Represents the score of the player.
     */
    private Label score = new Label("00");

    /**
     * Constructs a new PlayerInformation Element which contains the name, the settlements left and the score.
     * @param playerName the name of the player.
     * @param color the color for the Player.
     * @param colorMode defines which colors should be used.
     */
    public PlayerInformation(String playerName, PlayerColor color, boolean colorMode) {
        super();

        // set the players name
        name = new Label(playerName);
        name.setWrapText(true);

        name.setTextFill(Color.WHITE);
        settlements.setTextFill(Color.WHITE);
        score.setTextFill(Color.WHITE);

        setAlignment(Pos.CENTER);
        getChildren().addAll(name, settlements, score);

        if (!colorMode) {
            switch (color) {
                case RED -> setStyle("-fx-background-color: #B22222;");
                case BLUE -> setStyle("-fx-background-color: #4682B4;");
                case BLACK -> setStyle("-fx-background-color: #808080;");
                case WHITE -> {
                    setStyle("-fx-background-color: #FFFFFF;");
                    name.setTextFill(Color.BLACK);
                    settlements.setTextFill(Color.BLACK);
                    score.setTextFill(Color.BLACK);
                }
            }
        } else {
            switch (color) {
                case RED ->  setStyle("-fx-background-color: #FF8C00;");
                case BLUE -> setStyle("-fx-background-color: #9982CC;");
                case BLACK -> setStyle("-fx-background-color: #9ACD32;");
                case WHITE -> setStyle("-fx-background-color: #4682B4;");
            }
        }

        settlements.setMinWidth(35);
        score.setMinWidth(35);

        setPadding();
    }

    /**
     * Sets the padding for the Labels.
     */
    private void setPadding() {
        name.setPadding(new Insets(5,5,5,5));
        settlements.setPadding(new Insets(5,5,5,5));
        score.setPadding(new Insets(5,5,5,5));
    }

    /**
     * Sets the count for the score to the given.
     * @param count the count to set.
     */
    public void setScore(int count) {
        score.setText(integerToString(count));
    }

    /**
     * Sets the count for the settlements to the given.
     * @param count the count to set.
     */
    public void setSettlementCount(int count) {
        settlements.setText(integerToString(count));
    }

    /**
     * Changes int to String and add a leading zero, so it has two digits.
     * @param in the int to convert.
     * @return the String that represents the int.
     */
    private String integerToString(int in) {
        String out = Integer.toString(in);
        if (out.length() != 2) {
            out = "0" + out;
        }
        return out;
    }
}
