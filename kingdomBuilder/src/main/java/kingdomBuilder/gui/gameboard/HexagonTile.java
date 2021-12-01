package kingdomBuilder.gui.gameboard;

import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;

public class HexagonTile extends Polygon {

    public HexagonTile(double xPos, double yPos) {
        getPoints().add(xPos + 0.0);
        getPoints().add(yPos + 20.0);

        getPoints().add(xPos + 0.0);
        getPoints().add(yPos + 60.0);

        getPoints().add(xPos + 35.0);
        getPoints().add(yPos + 80.0);

        getPoints().add(xPos + 70.0);
        getPoints().add(yPos + 60.0);

        getPoints().add(xPos + 70.0);
        getPoints().add(yPos + 20.0);

        getPoints().add(xPos + 35.0);
        getPoints().add(yPos + 0.0);

        setStroke(Paint.valueOf("BLACK"));
        setStrokeType(StrokeType.INSIDE);
        setFill(Paint.valueOf("WHITE"));

        setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStroke(Paint.valueOf("RED"));
                setStrokeWidth(2.0);
            }
        });

        setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                setStroke(Paint.valueOf("BLACK"));
                setStrokeWidth(1.0);
            }
        });
    }

    //sets the texture with the correct ImagePattern
    public void setTexture(Image texture) {
        setFill(new ImagePattern(texture, 0.0f, 0.0f, 1.0f, 1.0f, true));
    }
}
