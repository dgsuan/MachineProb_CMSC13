package cmsc13.game;

/**
 * Central configuration constants for The Paradigm Trials
 */
public class Constants {
    
    public static final int BYTE_X = 32;
    public static final int BYTE_Y = 32; //32X32

    // Window
    public static final int WINDOW_WIDTH = 1280;
    public static final int WINDOW_HEIGHT = 720;
    public static final String WINDOW_TITLE = "THE PARADIGM TRIALS - Escape the System";
    
    // Game
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
