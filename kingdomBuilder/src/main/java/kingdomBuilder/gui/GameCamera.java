package kingdomBuilder.gui;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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
     * Handles scrolling based on the up-arrow key.
     */
    private boolean dragUpPressed;

    /**
     * Handles scrolling based on the down-arrow key.
     */
    private boolean dragDownPressed;

    /**
     * Handles scrolling based on the left-arrow key.
     */
    private boolean dragLeftPressed;

    /**
     * Handles scrolling based on the right-arrow key.
     */
    private boolean dragRightPressed;

    /**
     * Handles scrolling based on the horizontal mouse movement.
     */
    private double dragPreviousX;

    /**
     * Handles scrolling based on the vertical mouse movement.
     */
    private double dragPreviousY;

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
        AnimationTimer scrolling = new AnimationTimer() {
            private long previousTime;

            @Override
            public void handle(long l) {
                // converting given time from nanoseconds (10^-9 s) into seconds
                double deltaTime = (l - previousTime) * 1e-9;
                previousTime = l;

                // restrict scroll speed based on passed time since last frame to some reasonable amount
                double scrollSpeed = 0.08 * Math.max(deltaTime, 0.1) * Math.abs(getTranslateZ());

                // this is a little awkward with java booleans, getting the direction based on held arrow keys
                Point2D direction = new Point2D(
                        (dragRightPressed ? 1 : 0) + (dragLeftPressed ? -1 : 0),
                        (dragDownPressed ? 1 : 0) + (dragUpPressed ? -1 : 0)).normalize();
                setTranslateX(getTranslateX() + direction.getX() * scrollSpeed);
                setTranslateY(getTranslateY() + direction.getY() * scrollSpeed);
            }

            @Override
            public void start() {
                super.start();
                previousTime = System.nanoTime();
            }
        };

        subScene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP -> dragUpPressed = true;
                case DOWN -> dragDownPressed = true;
                case LEFT -> dragLeftPressed = true;
                case RIGHT -> dragRightPressed = true;
            }
            scrolling.start();
            keyEvent.consume();
        });

        subScene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case UP -> dragUpPressed = false;
                case DOWN -> dragDownPressed = false;
                case LEFT -> dragLeftPressed = false;
                case RIGHT -> dragRightPressed = false;
            }
            if (!(dragUpPressed || dragDownPressed || dragLeftPressed || dragRightPressed)) {
                scrolling.stop();
            }
            keyEvent.consume();
        });

        subScene.setOnMousePressed(mouseEvent -> {
            subScene.requestFocus();
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                dragPreviousX = mouseEvent.getSceneX();
                dragPreviousY = mouseEvent.getSceneY();
            }
        });

        subScene.setOnMouseDragged(mouseEvent -> {
            double scrollSpeed = 0.0015 * Math.abs(getTranslateZ());
            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                setTranslateX(getTranslateX() + (mouseEvent.getSceneX() - dragPreviousX) * -scrollSpeed);
                setTranslateY(getTranslateY() + (mouseEvent.getSceneY() - dragPreviousY) * -scrollSpeed);
                dragPreviousX = mouseEvent.getSceneX();
                dragPreviousY = mouseEvent.getSceneY();
            }
        });
    }
}
