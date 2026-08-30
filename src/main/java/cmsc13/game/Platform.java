package cmsc13.game;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** A simple one-way floating platform: the player lands on it from above but can jump up through it. */
public final class Platform {
    public static final double THICKNESS = Constants.TILE_SIZE / 2.0;
    private final double x;
    private final double y;
    private final double width;
    private final Rectangle node;

    public Platform(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        node = new Rectangle(width, THICKNESS, Color.web("#8d7ec8"));
        node.setArcWidth(4); node.setArcHeight(4);
        node.setLayoutX(x); node.setLayoutY(y);
    }

    public double getX() { return x; }
    public double getWidth() { return width; }
    public double getTop() { return y; }
    public Rectangle getNode() { return node; }
}
