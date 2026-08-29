package cmsc13.game;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/** One proximity-based Trial 1 gate. */
public final class Gate {
    private final double x;
    private final Group node = new Group();
    private final Rectangle body = new Rectangle(70, 90);
    private boolean completed;

    public Gate(double x) {
        this.x = x;
        body.setArcWidth(14); body.setArcHeight(14);
        Label label = new Label("TRIAL 1");
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
        label.setLayoutX(8); label.setLayoutY(34);
        node.getChildren().addAll(body, label);
        node.setLayoutX(x); node.setLayoutY(510);
        refresh();
    }
    public boolean isNear(Player player) { return Math.abs(player.getX() - x) < 100; }
    public void setCompleted(boolean completed) { this.completed = completed; refresh(); }
    public Group getNode() { return node; }
    private void refresh() { body.setFill(completed ? Color.web("#65c18c") : Color.web("#7562a8")); }
}
