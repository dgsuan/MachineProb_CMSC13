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

    // Long-map layout. The first seven gates spread across the route with gently growing gaps.
    // Gate eight sits one viewport from the far edge, leaving a short final stretch of map.
    public static final int WORLD_TILES_WIDE = 480;
    public static final double WORLD_WIDTH = WORLD_TILES_WIDE * TILE_SIZE;
    public static final int GROUND_TILE_HEIGHT = 6;
    /** Points earned for each correct answer and the score threshold per cleared gate. */
    public static final int POINTS_PER_CORRECT_ANSWER = 100;
    public static final int POINTS_PER_TRIAL = 600;
    public static final double GROUND_HEIGHT = TILE_SIZE * GROUND_TILE_HEIGHT;
    public static final double GROUND_Y = LOGICAL_HEIGHT - GROUND_HEIGHT;
    public static final double SKY_SCROLL_SPEED = 1;
    public static final double MOUNTAIN_SCROLL_SPEED = 0.28;
    public static final double CLOUD_SCROLL_SPEED = 0.34;
    public static final int LEAF_COUNT = 34;
    public static final double LEAF_SWAY_AMPLITUDE = 8.0;
    public static final double LEAF_SWAY_SPEED = 1.8;
    public static final double LEAF_HIT_AMPLITUDE = 22.0;
    public static final double LEAF_HIT_SPEED = 3.4;
    public static final long LEAF_FRAME_DURATION = 120_000_000L;
    public static final long LEAF_HIT_FRAME_DURATION = 65_000_000L;
    public static final double LEAF_COLLISION_WIDTH = 20.0;
    public static final double LEAF_COLLISION_HEIGHT = 18.0;
    private static final int[] TRIAL_TILE_POSITIONS = {
        38, 78, 120, 164, 210, 258, 308, 448
    };

    public static double trialX(int trialNumber) {
        if (trialNumber < 1 || trialNumber > TRIAL_TILE_POSITIONS.length) {
            throw new IllegalArgumentException("Trial number must be between 1 and " + TRIAL_TILE_POSITIONS.length);
        }
        return TRIAL_TILE_POSITIONS[trialNumber - 1] * TILE_SIZE;
    }

    // Player physics in logical pixels.
    /** The collision body remains a single 32 x 32 logical tile. */
    public static final double PLAYER_WIDTH = 32;
    public static final double PLAYER_HEIGHT = 32;
    public static final double GRAVITY = 0.6;
    public static final double JUMP_VELOCITY = -15;
    public static final double MOVE_SPEED = 5;
    public static final double FALL_LIMIT = LOGICAL_HEIGHT + 324;
}
