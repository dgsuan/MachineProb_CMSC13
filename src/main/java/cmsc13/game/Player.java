package cmsc13.game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * Minimal player movement with ground segments, holes, and floating platforms.
 */
public final class Player {

    public static final double WIDTH = Constants.PLAYER_WIDTH;
    public static final double HEIGHT = Constants.PLAYER_HEIGHT;
    public static final double GROUND_Y = Constants.GROUND_Y;
    private static final double SPRITE_SIZE = 32;
    private static final long IDLE_FRAME_DURATION = 150_000_000L;
    private static final long WALK_FRAME_DURATION = 40_000_000L;
    private static final Image[] IDLE_FRAMES = loadFrames("idle", "idle", 6);
    private static final Image[] WALK_FORWARD_FRAMES = loadFrames("forward", "forward", 6);
    private static final Image[] WALK_BACKWARD_FRAMES = loadFrames("backward", "backward", 6);
    private static final Image[] JUMP_FRAMES = loadFrames("jump", "jump", 10);
    private static final int JUMP_DURATION_TICKS = (int) Math.ceil(
            2.0 * Math.abs(Constants.JUMP_VELOCITY) / Constants.GRAVITY);

    private final Group node = new Group();
    private final ImageView sprite = new ImageView();
    private double x = 100;
    private double y = GROUND_Y - HEIGHT;
    private double velocityY;
    private boolean left;
    private boolean right;
    /**
     * Prevents movement input from cancelling a fall into a pit; reset at
     * checkpoint respawn.
     */
    private boolean fallInputLocked;
    private boolean onGround = true;
    private double wallLeft = Double.POSITIVE_INFINITY;
    private double worldWidth = 3000;
    private double checkpointX = Constants.TILE_SIZE * 8;
    private List<double[]> groundSegments = new ArrayList<>();
    private List<Platform> platforms = new ArrayList<>();
    private AnimationState animationState = AnimationState.IDLE;
    private int animationFrame;
    private long lastAnimationUpdate;
    private boolean facingRight = true;
    private int jumpElapsedTicks;
    private Runnable onJump = () -> {
    };
    private Runnable onFall = () -> {
    };

    public void setOnJump(Runnable action) {
        onJump = action;
    }

    public void setOnFall(Runnable action) {
        onFall = action;
    }

    public Player() {
        sprite.setFitWidth(SPRITE_SIZE);
        sprite.setFitHeight(SPRITE_SIZE);
        sprite.setPreserveRatio(true);
        sprite.setSmooth(false);
        // Centre the sprite over the collision box and keep its feet on the ground.
        sprite.setLayoutX((WIDTH - SPRITE_SIZE) / 2);
        sprite.setLayoutY(HEIGHT - SPRITE_SIZE);
        sprite.setImage(IDLE_FRAMES[0]);
        node.getChildren().add(sprite);
        draw();
    }

    /**
     * Feeds the current level geometry to the player each time the world is
     * (re)built.
     */
    public void setLevel(double worldWidth, List<double[]> groundSegments, List<Platform> platforms) {
        this.worldWidth = worldWidth;
        this.groundSegments = groundSegments;
        this.platforms = platforms;
    }

    public void update() {
        if (!fallInputLocked && left) {
            x -= Constants.MOVE_SPEED;
        }
        if (!fallInputLocked && right) {
            x += Constants.MOVE_SPEED;
        }
        if (left && !right) {
            facingRight = false;
        }
        if (right && !left) {
            facingRight = true;
        }
        x = Math.max(0, Math.min(worldWidth - WIDTH, x));
        if (x + WIDTH > wallLeft) {
            x = wallLeft - WIDTH;
        }

        velocityY += Constants.GRAVITY;
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
        // Once the player drops below the ground plane over a gap, ignore movement until respawn.
        if (y > GROUND_Y + 8 && !onSolidGround()) {
            fallInputLocked = true;
            left = false;
            right = false;
        }
        if (y > Constants.FALL_LIMIT) {
            onFall.run();
            respawnAtCheckpoint();
        }
        updateAnimation();
        draw();
    }

    /**
     * The highest surface directly under the player's feet, or +infinity if
     * there is only a hole.
     */
    private double supportSurface() {
        double support = Double.POSITIVE_INFINITY;
        if (onSolidGround()) {
            support = GROUND_Y;
        }
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
            if (centerX >= segment[0] && centerX <= segment[1]) {
                return true;
            }
        }
        return false;
    }

    public void jump() {
        if (onGround) {
            velocityY = Constants.JUMP_VELOCITY;
            onGround = false;
            onJump.run();
        }
    }

    public void hitLeaf() {
        velocityY = Math.min(velocityY, -8.0);
        x += facingRight ? -10 : 10;
        y = Math.max(0, y - 8);
        onGround = false;
    }

    public void setWallLeft(double worldX) {
        this.wallLeft = worldX;
    }

    public void clearWall() {
        this.wallLeft = Double.POSITIVE_INFINITY;
    }

    public void setCheckpoint(double worldX) {
        this.checkpointX = worldX;
    }

    public void respawnAtCheckpoint() {
        respawnAt(checkpointX);
    }

    public void respawnAt(double worldX) {
        x = Math.max(0, worldX);
        y = GROUND_Y - HEIGHT;
        velocityY = 0;
        onGround = true;
        fallInputLocked = false;
        setAnimationState(AnimationState.IDLE);
        jumpElapsedTicks = 0;
        lastAnimationUpdate = 0;
        sprite.setImage(IDLE_FRAMES[0]);
        draw();
    }

    public void setLeft(boolean value) {
        left = value;
    }

    public void setRight(boolean value) {
        right = value;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public Group getNode() {
        return node;
    }

    private void draw() {
        node.setLayoutX(x);
        node.setLayoutY(y);
    }

    private void updateAnimation() {
        boolean moving = left != right && !fallInputLocked;
        if (!onGround) {
            if (animationState != AnimationState.JUMP) {
                setAnimationState(AnimationState.JUMP);
                jumpElapsedTicks = 0;
            }
            jumpElapsedTicks++;
            animationFrame = Math.min(JUMP_FRAMES.length - 1,
                    jumpElapsedTicks * JUMP_FRAMES.length / JUMP_DURATION_TICKS);
            sprite.setImage(JUMP_FRAMES[animationFrame]);
            return;
        }
        AnimationState groundState = moving
                ? (facingRight ? AnimationState.WALK_FORWARD : AnimationState.WALK_BACKWARD)
                : AnimationState.IDLE;
        if (animationState != groundState) setAnimationState(groundState);

        long now = System.nanoTime();
        long duration = animationState == AnimationState.IDLE ? IDLE_FRAME_DURATION : WALK_FRAME_DURATION;
        if (lastAnimationUpdate == 0 || now - lastAnimationUpdate >= duration) {
            Image[] frames = currentFrames();
            sprite.setImage(frames[animationFrame]);
            animationFrame = (animationFrame + 1) % frames.length;
            lastAnimationUpdate = now;
        }
    }

    private void setAnimationState(AnimationState state) {
        animationState = state;
        animationFrame = 0;
        lastAnimationUpdate = 0;
    }

    private Image[] currentFrames() {
        if (animationState == AnimationState.IDLE) {
            return IDLE_FRAMES;
        }
        if (animationState == AnimationState.WALK_FORWARD) {
            return WALK_FORWARD_FRAMES;
        }
        if (animationState == AnimationState.WALK_BACKWARD) {
            return WALK_BACKWARD_FRAMES;
        }
        return JUMP_FRAMES;
    }

    private static Image[] loadFrames(String folder, String prefix, int count) {
        Image[] frames = new Image[count];
        for (int index = 0; index < count; index++) {
            String resource = "/characters/player/" + folder + "/" + prefix + (index + 1) + ".png";
            frames[index] = new Image(Player.class.getResource(resource).toExternalForm());
        }
        return frames;
    }

    private enum AnimationState {
        IDLE, WALK_FORWARD, WALK_BACKWARD, JUMP
    }
}
