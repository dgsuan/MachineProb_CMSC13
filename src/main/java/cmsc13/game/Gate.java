package cmsc13.game;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/** A sprite-backed milestone which exposes only world/proximity state. */
public final class Gate {
    private static final Image[] INCOMPLETE = frames("/Gate/incompleted", 6);
    private static final Image[] COMPLETE = frames("/Gate/completed", 5);
    private final int trialNumber;
    private final double x;
    private final Group node = new Group();
    private final ImageView sprite = new ImageView();
    private boolean completed;
    private long lastFrame;
    private int frame;

    public Gate(int trialNumber, double x) {
        this.trialNumber = trialNumber; this.x = x;
        sprite.setFitWidth(48); sprite.setFitHeight(64); sprite.setPreserveRatio(true); sprite.setSmooth(false);
        Label label = new Label(trialNumber == Constants.TRIAL_COUNT ? "CORE" : "T" + trialNumber);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 8; -fx-effect: dropshadow(gaussian, black, 2, .8, 0, 1);");
        label.setLayoutX(9); label.setLayoutY(19);
        node.getChildren().addAll(sprite, label);
        node.setLayoutX(x); node.setLayoutY(Constants.GROUND_Y - 62);
        tick(System.nanoTime());
    }
    public void tick(long now) {
        if (lastFrame == 0 || now - lastFrame > 150_000_000L) {
            Image[] images = completed ? COMPLETE : INCOMPLETE;
            sprite.setImage(images[frame++ % images.length]); lastFrame = now;
        }
    }
    public double getX() { return x; }
    public int getTrialNumber() { return trialNumber; }
    public boolean isNear(Player player) { return player.getX() + Player.WIDTH >= x - 34 && player.getX() <= x + 52; }
    public double getLeftEdge() { return x; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean value) { completed = value; frame = 0; lastFrame = 0; }
    public Group getNode() { return node; }
    private static Image[] frames(String stem, int count) {
        Image[] result = new Image[count];
        for (int i = 0; i < count; i++) result[i] = new Image(Gate.class.getResource(stem + (i + 1) + ".png").toExternalForm());
        return result;
    }
}
