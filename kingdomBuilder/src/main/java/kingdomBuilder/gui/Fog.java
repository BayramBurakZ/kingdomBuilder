package kingdomBuilder.gui;

import javafx.animation.AnimationTimer;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;

/**
 * Fog objects can be placed in a scene to provide a simple animated transparent fog effect.
 */
public class Fog extends Box {

    /**
     * The time required for the fog to fade in or out.
     */
    private static final double FADE_DURATION = 0.5;

    /**
     * The default minimum opacity on the surface of the fog object once fully visible.
     */
    private static final double DEFAULT_MIN_OPACITY = 0.2;

    /**
     * The default maximum opacity on the surface of the fog object once fully visible.
     */
    private static final double DEFAULT_MAX_OPACITY = 0.6;

    /**
     * Represents the state of the fog's animation.
     */
    private enum State { STOPPED, FADE_IN, PLAYING, FADE_OUT }

    /**
     * Represents the noise applied to the fog's diffuse map.
     */
    private final float[][] noise;

    /**
     * The diffuse map of the fog containing varying opacity to be interpolated across the surface of the fog object.
     */
    private final WritableImage diffuseMap;

    /**
     * The AnimationTimer used for animating the fog's fade-in/-out and morphing opacity over time.
     */
    private final AnimationTimer animation;

    /**
     * The minimum opacity on the surface of the fog object once fully visible.
     */
    private final double minOpacity;

    /**
     * The maximum opacity on the surface of the fog object once fully visible.
     */
    private final double maxOpacity;

    /**
     * The current state of the fog's animation.
     */
    private State state = State.STOPPED;

    /**
     * The system timestamp in nanoseconds at which the fog's current state was entered.
     */
    private long startTime;

    /**
     * Constructs a new fog object with box proportions.
     * @param width the width of the box.
     * @param height the height of the box.
     * @param depth the depth of the box.
     * @param resolution the resolution of the noise and diffuse map of the fog.
     * @param color the color of the fog.
    */
    public Fog(double width, double height, double depth, int resolution, Color color) {
        this(width, height, depth, resolution, color, DEFAULT_MIN_OPACITY, DEFAULT_MAX_OPACITY);
    }

    /**
     * Constructs a new fog object with box proportions.
     * @param width the width of the box.
     * @param height the height of the box.
     * @param depth the depth of the box.
     * @param resolution the resolution of the noise and diffuse map of the fog.
     * @param color the color of the fog.
     * @param minOpacity the minimum opacity on the surface of the fog object once fully visible.
     * @param maxOpacity the maximum opacity on the surface of the fog object once fully visible
     */
    public Fog(double width, double height, double depth, int resolution, Color color, double minOpacity, double maxOpacity) {
        super(width, height, depth);
        setVisible(false);

        this.minOpacity = minOpacity;
        this.maxOpacity = maxOpacity;

        noise = new float[resolution][resolution];
        diffuseMap = new WritableImage(resolution, resolution);
        PixelWriter pw = diffuseMap.getPixelWriter();
        for (int y = 1; y < resolution-1; y++) {
            for (int x = 1; x < resolution-1; x++) {
                // first/last row/column of the noise array is unused and could be adjusted
                noise[y][x] = (float)Math.random() * 10;
                pw.setColor(x, y, Color.WHITE);
            }
        }

        // the border is transparent to fade out the diffuseMap
        for (int i = 0; i < resolution; i++) {
            pw.setColor(0, i, Color.gray(1, 0));
            pw.setColor(resolution-1, i, Color.gray(1, 0));
            pw.setColor(i, 0, Color.gray(1, 0));
            pw.setColor(i, resolution-1, Color.gray(1, 0));
        }

        setMaterial(new PhongMaterial(color, diffuseMap, null, null, null));

        animation = new AnimationTimer() {

            @Override
            public void handle(long now) {
                switch (state) {
                    case FADE_IN -> {
                        double relativeFadeIn = relativeFadeIn(now);
                        updateDiffuseMap(now, relativeFadeIn);
                        if (relativeFadeIn >= 1) {
                            state = State.PLAYING;
                        }
                    }
                    case PLAYING -> updateDiffuseMap(now, 1);
                    case FADE_OUT -> {
                        double relativeFadeOut = relativeFadeOut(now);
                        updateDiffuseMap(now, relativeFadeOut);
                        if (relativeFadeOut <= 0) {
                            super.stop();
                            state = State.STOPPED;
                            Fog.this.setVisible(false);
                        }
                    }
                }
            }

            @Override
            public void start() {
                super.start();
                long now = System.nanoTime();
                if (state == State.STOPPED) {
                    startTime = now;
                    state = State.FADE_IN;
                    Fog.this.setVisible(true);
                } else if (state == State.FADE_OUT) {
                    startTime = 2 * now - (long) (FADE_DURATION * 1e9d) - startTime;
                    state = State.FADE_IN;
                }
            }

            @Override
            public void stop() {
                startTime = System.nanoTime();
                state = State.FADE_OUT;
            }

        };
    }

    /**
     * Sets the animation to fade in the fog if it isn't already.
     */
    public void fadeIn() {
        animation.start();
    }

    /**
     * Sets the animation to fade out the fog if it isn't already.
     */
    public void fadeOut() {
        animation.stop();
    }

    /**
     * Gets the resolution of the noise.
     * @return the resolution of the noise.
     */
    private int getResolution() {
        return noise.length;
    }

    /**
     * Updates the diffuse map based on the provided timestamp and a factor applied to the overall opacity.
     * Uses default values for minimum and maximum opacity.
     * @param now the system timestamp in nanoseconds since the fog's current state was entered.
     * @param opacityFactor the factor to be applied to the overall opacity.
     */
    private void updateDiffuseMap(long now, double opacityFactor) {
        updateDiffuseMap(now, opacityFactor, minOpacity, maxOpacity);
    }

    /**
     * Updates the diffuse map based on the provided timestamp and a factor applied to the overall opacity.
     * The opacity oscillates over time between the provided minimum and maximum values.
     * @param now the system timestamp in nanoseconds since the fog's current state was entered.
     * @param opacityFactor the factor to be applied to the overall opacity.
     * @param minOpacity the minimum opacity in the diffuse map.
     * @param maxOpacity the maximum opacity in the diffuse map.
     */
    private void updateDiffuseMap(long now, double opacityFactor, double minOpacity, double maxOpacity) {
        int resolution = getResolution();
        PixelWriter pw = diffuseMap.getPixelWriter();
        double variance = maxOpacity - minOpacity;
        for (int y = 1; y < resolution-1; y++) {
            for (int x = 1; x < resolution-1; x++) {
                pw.setColor(x, y, Color.gray(1,
                        Math.max(opacityFactor, 0) * (minOpacity + variance * (0.5 + 0.5 * Math.sin(now * 1e-9 + noise[y][x])))));
            }
        }
    }

    /**
     * Calculates the relative amount of time between the beginning and end of the fade-in animation.
     * @param now the system timestamp in nanoseconds since the fog's current state was entered.
     * @return the relative amount of time between the beginning and end of the fade-in animation.
     */
    private double relativeFadeIn(long now) {
        return Math.min((now - startTime) * 1e-9d / FADE_DURATION, 1);
    }

    /**
     * Calculates the relative amount of time between the beginning and end of the fade-out animation.
     * @param now the system timestamp in nanoseconds since the fog's current state was entered.
     * @return the relative amount of time between the beginning and end of the fade-out animation.
     */
    private double relativeFadeOut(long now) {
        return 1 - relativeFadeIn(now);
    }
}
