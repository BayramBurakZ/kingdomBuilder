package kingdomBuilder.gui.gameboard;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
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
}
