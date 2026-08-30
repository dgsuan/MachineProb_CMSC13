package cmsc13.game;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** Minimal player movement with ground segments, holes, and floating platforms. */
public final class Player {
<<<<<<< Updated upstream
    public static final double WIDTH = 30;
    public static final double HEIGHT = 50;
    public static final double GROUND_Y = 600;
    private static final double GRAVITY = 0.6;
    private static final double JUMP_VELOCITY = -15;
    private static final double MOVE_SPEED = 5;
    private static final double FALL_LIMIT = 900;
=======
    public static final double WIDTH = 23;
    public static final double HEIGHT = 28;
    public static final double GROUND_Y = 300;
    private static final double GRAVITY = 0.3;
    private static final double JUMP_VELOCITY = -7.5;
    private static final double MOVE_SPEED = 2.5;
    private static final double FALL_LIMIT = 450;
    private static final double SPRITE_SIZE = 32;
    private static final long IDLE_FRAME_DURATION = 180_000_000L;
    private static final long RUN_FRAME_DURATION = 95_000_000L;
    private static final long JUMP_FRAME_DURATION = 90_000_000L;
    private static final Image[] IDLE_FRAMES = loadFrames("Idle", 4);
    private static final Image[] RUN_RIGHT_FRAMES = loadFrames("Run_right", 6);
    private static final Image[] RUN_LEFT_FRAMES = loadFrames("Run_left", 6);
    private static final Image[] JUMP_RIGHT_FRAMES = loadFrames("Jump_right", 8);
    // The supplied left-facing jump sprites use the "Jump_leftt" filename prefix.
    private static final Image[] JUMP_LEFT_FRAMES = loadFrames("Jump_leftt", 8);

>>>>>>> Stashed changes
    private final Group node = new Group();
    private double x = 100;
    private double y = GROUND_Y - HEIGHT;
    private double velocityY;
    private boolean left;
    private boolean right;
    private boolean onGround = true;
    private double wallLeft = Double.POSITIVE_INFINITY;
    private double worldWidth = 3000;
    private double checkpointX = 100;
    private List<double[]> groundSegments = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();

    public Player() {
        Rectangle body = new Rectangle(WIDTH, HEIGHT, Color.web("#ff8fab"));
        body.setArcWidth(10); body.setArcHeight(10);
        Circle head = new Circle(WIDTH / 2, -10, 12, Color.web("#ffc2d1"));
        node.getChildren().addAll(body, head);
        draw();
    }

    /** Feeds the current level geometry to the player each time the world is (re)built. */
    public void setLevel(double worldWidth, List<double[]> groundSegments, List<Platform> platforms) {
        this.worldWidth = worldWidth;
        this.groundSegments = groundSegments;
        this.platforms = platforms;
    }

    public void update() {
        if (left) x -= MOVE_SPEED;
        if (right) x += MOVE_SPEED;
        x = Math.max(0, Math.min(worldWidth - WIDTH, x));
        if (x + WIDTH > wallLeft) x = wallLeft - WIDTH;

        velocityY += GRAVITY;
        double nextY = y + velocityY;
        double support = supportSurface();
        if (nextY + HEIGHT >= support) {
            y = support - HEIGHT;
            velocityY = 0;
            onGround = true;
        } else {
            y = nextY;
            onGround = false;
        }
        if (y > FALL_LIMIT) respawnAtCheckpoint();
        draw();
    }

    /** The highest surface directly under the player's feet, or +infinity if there is only a hole. */
    private double supportSurface() {
        double support = Double.POSITIVE_INFINITY;
        if (onSolidGround()) support = GROUND_Y;
        if (velocityY >= 0) {
            for (Platform platform : platforms) {
                boolean horizontallyOnPlatform = x + WIDTH > platform.getX()
                    && x < platform.getX() + platform.getWidth();
                boolean fallingFromAbove = y + HEIGHT <= platform.getTop() + 2;
                if (horizontallyOnPlatform && fallingFromAbove) {
                    support = Math.min(support, platform.getTop());
                }
            }
        }
        return support;
    }

    private boolean onSolidGround() {
        double centerX = x + WIDTH / 2;
        for (double[] segment : groundSegments) {
            if (centerX >= segment[0] && centerX <= segment[1]) return true;
        }
        return false;
    }

    public void jump() { if (onGround) { velocityY = JUMP_VELOCITY; onGround = false; } }
    public void setWallLeft(double worldX) { this.wallLeft = worldX; }
    public void clearWall() { this.wallLeft = Double.POSITIVE_INFINITY; }
    public void setCheckpoint(double worldX) { this.checkpointX = worldX; }
    public void respawnAtCheckpoint() { respawnAt(checkpointX); }
    public void respawnAt(double worldX) {
        x = Math.max(0, worldX);
        y = GROUND_Y - HEIGHT;
        velocityY = 0;
        onGround = true;
        draw();
    }
    public void setLeft(boolean value) { left = value; }
    public void setRight(boolean value) { right = value; }
    public double getX() { return x; }
    public Group getNode() { return node; }
    private void draw() { node.setLayoutX(x); node.setLayoutY(y); }
}
