package cmsc13.game;

/** Shared, logical-coordinate configuration for SYSTEMBOUND. */
public final class Constants {
    private Constants() { }

    // Fixed virtual game viewport. The JavaFX window scales this area uniformly.
    public static final int LOGICAL_WIDTH = 1024;
    public static final int LOGICAL_HEIGHT = 576;
    public static final int MIN_WINDOW_WIDTH = 640;
    public static final int MIN_WINDOW_HEIGHT = 360;
    public static final String WINDOW_TITLE = "SYSTEMBOUND: The Paradigm Trials";

    // Pixel-art world grid: 32 columns by 18 rows in the visible viewport.
    public static final int TILE_SIZE = 32;
    public static final int VIEWPORT_TILES_WIDE = LOGICAL_WIDTH / TILE_SIZE;
    public static final int VIEWPORT_TILES_HIGH = LOGICAL_HEIGHT / TILE_SIZE;

    // Long-map layout. All gate positions are tile-aligned and intentionally uneven.
    public static final int FUTURE_WORLD_TILES_WIDE = 480;
    public static final double FUTURE_WORLD_WIDTH = FUTURE_WORLD_TILES_WIDE * TILE_SIZE;
    public static final double GROUND_Y = LOGICAL_HEIGHT - (TILE_SIZE * 4);
    public static final double GROUND_HEIGHT = TILE_SIZE;
    public static final int TRIAL_COUNT = 10;
    private static final int[] TRIAL_TILE_POSITIONS = {
        38, 78, 119, 164, 205, 246, 289, 332, 382, 432
    };

    public static double trialX(int trialNumber) {
        if (trialNumber < 1 || trialNumber > TRIAL_COUNT) {
            throw new IllegalArgumentException("Trial number must be between 1 and " + TRIAL_COUNT);
        }
        return TRIAL_TILE_POSITIONS[trialNumber - 1] * TILE_SIZE;
    }

    // Player physics in logical pixels.
    public static final double PLAYER_WIDTH = 23;
    public static final double PLAYER_HEIGHT = 28;
    public static final double GRAVITY = 0.6;
    public static final double JUMP_VELOCITY = -15;
    public static final double MOVE_SPEED = 5;
    public static final double FALL_LIMIT = LOGICAL_HEIGHT + 324;
}
