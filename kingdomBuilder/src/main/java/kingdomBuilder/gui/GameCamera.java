package kingdomBuilder.gui;

import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.transform.Transform;

/**
 * Class for a standard camera used for viewing a board.
 */
public class GameCamera extends PerspectiveCamera {

    /**
     * Represents the setting for the field of view (fov).
     */
    private static final double FOV = 50.0;

    /**
     * Represents the angle for the camera.
     */
    private static final double VIEW_ANGLE = 30.0;

    /**
     * Represents the SubScene using the camera.
     */
    private final SubScene subScene;

    /**
     * Constructs a new GameCamera and sets the specified SubScene to use it.
     * The initial position of the camera is set to look at the specified position on the board.
     * @param subScene the SubScene using the camera.
     * @param boardPosition the initial position the camera should look at on the board.
     * @param initialZoom the initial zoom of the camera.
     */
    public GameCamera(SubScene subScene, Point3D boardPosition, double initialZoom) {
        // fixedEyeAtCameraZero has to be true or a change in the window's aspect ratio modifies the FOV
        super(true);

        setFarClip(4096.0);
        setRotationAxis(new Point3D(1.0, 0, 0));
        setRotate(VIEW_ANGLE);
        setFieldOfView(FOV);
        subScene.setCamera(this);
        this.subScene = subScene;

        // calculates the initial position of the camera considering the view angle and FOV using the law of sines
        double ang1 = Math.toRadians(VIEW_ANGLE - FOV/2d);

        double a = boardPosition.getY() * 2;
        double b = a / Math.sin(Math.toRadians(FOV)) * Math.sin(Math.toRadians(90d) + ang1);

        double gamma = Math.toRadians(90d - FOV) - ang1;

        double z = -Math.sin(gamma) * b;
        double y = Math.cos(gamma) * b;

        Transform t = getLocalToSceneTransform();
        Point3D offset = new Point3D(t.getMxz(), t.getMyz(), t.getMzz()).multiply(initialZoom);

        setTranslateX(boardPosition.getX() + offset.getX());
        setTranslateY(y + offset.getY());
        setTranslateZ(z + offset.getZ());

        setupHandlers();
    }

    /**
     * Setup all connected EventHandler.
     */
    private void setupHandlers() {
        setupZoomHandler();
        setupScrollHandler();
    }

    /**
     * Zooms the camera when the user scrolls the mousewheel.
     */
    private void setupZoomHandler() {
        subScene.setOnScroll((ScrollEvent event) -> {
            double deltaY = event.getDeltaY();

            double zoomSpeed = 1.5;

            double translation = deltaY * zoomSpeed;

            Point3D pos = localToScene(0, 0, translation);
            setTranslateX(pos.getX());
            setTranslateY(pos.getY());
            setTranslateZ(pos.getZ());

            event.consume();
        });
    }

    /**
     * Translates the camera when the user presses the arrow keys.
     */
    private void setupScrollHandler() {
        subScene.setOnMouseEntered(event -> subScene.requestFocus());
        // TODO: smoother scrolling
        subScene.setOnKeyPressed((KeyEvent event) -> {
            double scrollSpeed = 20.0;
            switch (event.getCode()) {
                case UP -> setTranslateY(getTranslateY() - scrollSpeed);
                case DOWN -> setTranslateY(getTranslateY() + scrollSpeed);
                case LEFT -> setTranslateX(getTranslateX() - scrollSpeed);
                case RIGHT -> setTranslateX(getTranslateX() + scrollSpeed);
            }
            event.consume();
        });
    }
}
