package kingdomBuilder.gui.util;

import javafx.geometry.Point2D;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HexagonCalculatorTest {

    @Test
    @DisplayName("Check Corners of Hexagon-")
    void testCalculateCornersWithConstantValues() {

        ArrayList<Point2D> constantValues = new ArrayList<>();

        //top corner
        constantValues.add(new Point2D(35, 80));

        //lower left
        constantValues.add(new Point2D(0, 60));

        //top left
        constantValues.add(new Point2D(0, 20));

        //bottom corner
        constantValues.add(new Point2D(35, 0));

        //top right
        constantValues.add(new Point2D(70, 20));

        //lower right
        constantValues.add(new Point2D(70, 60));

        ArrayList<Point2D> generatedValues = HexagonCalculator.calculateCorners(40);

        for (int i = 0; i < HexagonCalculator.NUMBER_OF_CORNERS; i++) {
            assertEquals(constantValues.get(i).getX(), generatedValues.get(i).getX(),
                    "X Coordinate failed at: " + i);
            assertEquals(constantValues.get(i).getY(), generatedValues.get(i).getY(),
                    "Y Coordinate failed at: " + i);
        }
    }
}