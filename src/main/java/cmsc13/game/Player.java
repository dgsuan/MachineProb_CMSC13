package cmsc13.game;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/** Minimal player movement and ground physics. */
public final class Player {
    public static final double WIDTH = 30;
    public static final double HEIGHT = 50;
    public static final double GROUND_Y = 600;
    private final Group node = new Group();
    private double x = 100;
    private double y = GROUND_Y - HEIGHT;
    private double velocityY;
    private boolean left;
    private boolean right;
    private boolean onGround = true;
    private double wallLeft = Double.POSITIVE_INFINITY;

    public Player() {
        Rectangle body = new Rectangle(WIDTH, HEIGHT, Color.web("#ff8fab"));
        body.setArcWidth(10); body.setArcHeight(10);
        Circle head = new Circle(WIDTH / 2, -10, 12, Color.web("#ffc2d1"));
        node.getChildren().addAll(body, head);
        draw();
    }

    public void update() {
        if (left) x -= 5;
        if (right) x += 5;
        x = Math.max(0, Math.min(2970, x));
        if (x + WIDTH > wallLeft) x = wallLeft - WIDTH;
        if (!onGround) velocityY += 0.6;
        y += velocityY;
        if (y >= GROUND_Y - HEIGHT) { y = GROUND_Y - HEIGHT; velocityY = 0; onGround = true; }
        draw();
    }
    public void jump() { if (onGround) { velocityY = -15; onGround = false; } }
    public void setWallLeft(double worldX) { this.wallLeft = worldX; }
    public void clearWall() { this.wallLeft = Double.POSITIVE_INFINITY; }
    public void respawnAtTrialOne() { x = 1080; y = GROUND_Y - HEIGHT; velocityY = 0; onGround = true; draw(); }
    public void setLeft(boolean value) { left = value; }
    public void setRight(boolean value) { right = value; }
    public double getX() { return x; }
    public Group getNode() { return node; }
    private void draw() { node.setLayoutX(x); node.setLayoutY(y); }
}
