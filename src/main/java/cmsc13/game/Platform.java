package cmsc13.game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** A simple one-way floating platform: the player lands on it from above but can jump up through it. */
public final class Platform {
    public static final double THICKNESS = Constants.TILE_SIZE;
    private final double x;
    private final double y;
    private final double width;
    private final ImageView node;

    public Platform(double x, double y, double width) {
        this.x = x;
        this.y = y;
        this.width = width;
        String asset = width >= 110
            ? "/BackGround/Platform/platform_green_large.png"
            : "/BackGround/Platform/platform_green_small.png";
        node = new ImageView(new Image(Platform.class.getResource(asset).toExternalForm()));
        node.setFitWidth(width);
        node.setFitHeight(THICKNESS);
        node.setPreserveRatio(false);
        node.setSmooth(false);
        node.setLayoutX(x); node.setLayoutY(y);
    }

    public double getX() { return x; }
    public double getWidth() { return width; }
    public double getTop() { return y; }
    public ImageView getNode() { return node; }
}
