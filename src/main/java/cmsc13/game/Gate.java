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
    private static final double WIDTH = 48;
    private static final double HEIGHT = 200;
    private final Rectangle body = new Rectangle(WIDTH, HEIGHT);
    private boolean completed;

    public Gate(int trialNumber, double x) {
        this.trialNumber = trialNumber;
        this.x = x;
        body.setArcWidth(10); body.setArcHeight(10);
        body.setStroke(Color.web("#c8bdf0"));
        body.setStrokeWidth(2);
        Label label = new Label("T" + trialNumber);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16;");
        label.setLayoutX(WIDTH / 2 - 9); label.setLayoutY(6);
        node.getChildren().addAll(body, label);
        node.setLayoutX(x); node.setLayoutY(Constants.GROUND_Y - HEIGHT);
        refresh();
    }

    public double getX() {return this.x;}
    public int getTrialNumber() { return trialNumber; }
    public boolean isNear(Player player) { return Math.abs(player.getX() - x) < 70; }
    public double getLeftEdge() { return x; }
    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; refresh(); }
    public Group getNode() { return node; }
    private void refresh() { body.setFill(completed ? Color.web("#65c18c") : Color.web("#7562a8")); }
}
