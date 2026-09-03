package cmsc13.game;

/**
 * Central configuration constants for The Paradigm Trials
 */
public class Constants {
    
    public static final int BYTE_X = 32;
    public static final int BYTE_Y = 32; //32X32

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
    public static final int WORLD_TILES_WIDE = 480;
    public static final double WORLD_WIDTH = WORLD_TILES_WIDE * TILE_SIZE;
    public static final int GROUND_TILE_HEIGHT = 6;
    public static final double GROUND_HEIGHT = TILE_SIZE * GROUND_TILE_HEIGHT;
    public static final double GROUND_Y = LOGICAL_HEIGHT - GROUND_HEIGHT;
    public static final double SKY_SCROLL_SPEED = 1;
    public static final double MOUNTAIN_SCROLL_SPEED = 1;
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
    /** The collision body remains a single 32 x 32 logical tile. */
    public static final double PLAYER_WIDTH = 32;
    public static final double PLAYER_HEIGHT = 32;
    public static final double GRAVITY = 0.6;
    public static final double JUMP_FORCE = 15;
    public static final double MOVE_SPEED = 5;
    public static final double MAX_FALL_SPEED = 20;
    
    // World
    public static final double GROUND_Y = 600;
    public static final double WORLD_LENGTH = 15000 * BYTE_X;
    
    // Trials - Checkpoint X coordinates
    public static final double TRIAL_1_X = 1200 * BYTE_X;
    public static final double TRIAL_2_X = 2500 * BYTE_X;
    public static final double TRIAL_3_X = 3800 * BYTE_X;
    public static final double TRIAL_4_X = 5100 * BYTE_X;
    public static final double TRIAL_5_X = 6400 * BYTE_X;
    public static final double TRIAL_6_X = 7700 * BYTE_X;
    public static final double TRIAL_7_X = 9000 * BYTE_X;
    public static final double TRIAL_8_X = 10300 * BYTE_X;
    public static final double TRIAL_9_X = 11600 * BYTE_X;
    public static final double TRIAL_10_X = 12900 * BYTE_X;
    public static final double CORE_GATE_X = 14200 * BYTE_X;
    
    // EXP System
    public static final int CORRECT_ANSWER_EXP = 100;
    public static final int INCORRECT_ANSWER_EXP = 0;
    public static final int QUESTIONS_PER_TRIAL = 14;
    public static final int THEORY_QUESTIONS_PER_TRIAL = 7;
    public static final int PRACTICAL_QUESTIONS_PER_TRIAL = 7;
    public static final int MAX_EXP_PER_TRIAL = QUESTIONS_PER_TRIAL * CORRECT_ANSWER_EXP;
    public static final int REQUIRED_EXP_PER_TRIAL = 1000;
    public static final int TOTAL_QUESTIONS = 140;
    
    // Lifelines
    public static final int SCAN_USES = 2;
    public static final int SYNC_USES = 2;
    public static final int RECOVERY_USES = 1;
    
    // Color progression
    public static final int[] COLOR_PERCENTAGES = {0, 10, 20, 30, 40, 50, 60, 70, 80, 90, 97, 100};
    
    // UI
    public static final int CORNER_RADIUS = 12;
    public static final int PADDING = 10;
    
    // Timing
    public static final long DIALOGUE_DELAY_MS = 300;
    public static final long ANSWER_FEEDBACK_DELAY_MS = 1500;
}
