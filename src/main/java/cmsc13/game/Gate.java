package cmsc13.game;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** One proximity-based trial gate. */
public final class Gate {
    private final int trialNumber;
    private final double x;
    private final Group node = new Group();
    private final Rectangle body = new Rectangle(18, 55);
    private boolean completed;

    public Gate(int trialNumber, double x) {
        this.trialNumber = trialNumber;
        this.x = x;
        body.setArcWidth(4); body.setArcHeight(4);
        Label label = new Label("T" + trialNumber);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 6;");
        label.setLayoutX(3); label.setLayoutY(1);
        node.getChildren().addAll(body, label);
        node.setLayoutX(x); node.setLayoutY(Player.GROUND_Y - 55);
        refresh();
    }

    public int getTrialNumber() { return trialNumber; }
    public boolean isNear(Player player) { return Math.abs(player.getX() - x) < 25; }
    public double getLeftEdge() { return x; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; refresh(); }
    public Group getNode() { return node; }
    private void refresh() { body.setFill(completed ? Color.web("#65c18c") : Color.web("#7562a8")); }
}
