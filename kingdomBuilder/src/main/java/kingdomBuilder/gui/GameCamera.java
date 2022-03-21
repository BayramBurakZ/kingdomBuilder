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
     * Represents the lower x, y and z boundary of the camera's view position.
     */
    private final Point3D minPosition;

    /**
     * Represents the upper x, y and z boundary of the camera's view position.
     */
    private final Point3D maxPosition;

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
     * @param minPosition the lower boundary regarding the (x,y)-viewpoint on the board and the z-depth of the camera.
     * @param maxPosition the upper boundary regarding the (x,y)-viewpoint on the board and the z-depth of the camera.
     * @param boardPosition the initial position the camera should look at on the board.
     * @param initialZoom the initial zoom of the camera.
     */
    public GameCamera(SubScene subScene, Point3D minPosition, Point3D maxPosition, Point3D boardPosition, double initialZoom) {
        // fixedEyeAtCameraZero has to be true or a change in the window's aspect ratio modifies the FOV
        super(true);

        this.subScene = subScene;
        this.minPosition = minPosition;
        this.maxPosition = maxPosition;

        setFarClip(4096.0);
        setRotationAxis(new Point3D(1.0, 0, 0));
        setRotate(VIEW_ANGLE);
        setFieldOfView(FOV);
        subScene.setCamera(this);

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

            clampPositionToBounds();
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

                clampPositionToBounds();
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

                clampPositionToBounds();
            }
        });
    }

    /**
     * Checks if the camera's position is outside its boundaries and clamps it to the border if so.
     */
    private void clampPositionToBounds() {
        Transform t = getLocalToSceneTransform();
        Point3D forward = new Point3D(t.getMxz(), t.getMyz(), t.getMzz());
        // find intersection point of view line and board (z = 0) based on view angle (using forward vector)
        Point2D viewPoint = new Point2D(
                getTranslateX(),
                -getTranslateZ() * (forward.getY() / forward.getZ()) + getTranslateY());

        // clamp camera into bounds
        if (viewPoint.getX() < minPosition.getX()) {
            setTranslateX(minPosition.getX());
        } else if (viewPoint.getX() > maxPosition.getX()) {
            setTranslateX(maxPosition.getX());
        }

        if (viewPoint.getY() < minPosition.getY()) {
            // offsetY describes how far out of bounds the camera's y position is
            double offsetY = viewPoint.getY() - minPosition.getY();
            setTranslateY(getTranslateY() - offsetY);
        } else if (viewPoint.getY() > maxPosition.getY()) {
            double offsetY = viewPoint.getY() - maxPosition.getY();
            setTranslateY(getTranslateY() - offsetY);
        }

        if (getTranslateZ() < minPosition.getZ()) {
            double offsetZ = getTranslateZ() - minPosition.getZ();
            setTranslateY(getTranslateY() - (forward.getY() / forward.getZ()) * offsetZ);
            setTranslateZ(minPosition.getZ());
        } else if (getTranslateZ() > maxPosition.getZ()) {
            double offsetZ = getTranslateZ() - maxPosition.getZ();
            setTranslateY(getTranslateY() - (forward.getY() / forward.getZ()) * offsetZ);
            setTranslateZ(maxPosition.getZ());
        }
    }
}
