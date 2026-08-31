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
    private final Rectangle body = new Rectangle(70, 250);
    private boolean completed;

    public Gate(int trialNumber, double x) {
        this.trialNumber = trialNumber;
        this.x = x;
        body.setArcWidth(14); body.setArcHeight(14);
        Label label = new Label("TRIAL " + trialNumber);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        label.setLayoutX(10); label.setLayoutY(0);
        node.getChildren().addAll(body, label);
        node.setLayoutX(x); node.setLayoutY(Player.GROUND_Y - 250);
        refresh();
    }

    public int getTrialNumber() { return trialNumber; }
    public boolean isNear(Player player) { return Math.abs(player.getX() - x) < 100; }
    public double getLeftEdge() { return x; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; refresh(); }
    public Group getNode() { return node; }
    private void refresh() { body.setFill(completed ? Color.web("#65c18c") : Color.web("#7562a8")); }
}
