package cmsc13.game;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Controls the menu, one world with simple platforming, and the trial quiz loop. */
public final class GameApplication extends Application {
    private static final double WORLD_WIDTH = Constants.FUTURE_WORLD_WIDTH;

    /** The long map starts as a continuous walkable ground layer. */
    private static final List<double[]> GROUND_SEGMENTS = List.of(
        new double[] {0, WORLD_WIDTH}
    );
    /** Placeholder platforms for the first section of the long map. */
    private static final List<Platform> PLATFORMS = List.of(
        new Platform(368, 140, 45),
        new Platform(527, 140, 45)
    );

    private final StackPane root = new StackPane();
    /** Fixed logical viewport; the outer root only provides the physical window. */
    private final StackPane logicalRoot = new StackPane();
    private final Pane world = new Pane();
    private final Pane viewport = new Pane();
    private final Player player = new Player();
    private final List<Gate> gates = createGates();
    private final QuestionBank questionBank = new QuestionBank();
    private GameState state = GameState.MENU;
    private Trial trial;
    private Gate activeGate;
    private Label prompt;

    @Override
    public void start(Stage stage) {
        stage.setTitle(Constants.WINDOW_TITLE);
        stage.setMinWidth(Constants.MIN_WINDOW_WIDTH);
        stage.setMinHeight(Constants.MIN_WINDOW_HEIGHT);
        logicalRoot.setMinSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        logicalRoot.setPrefSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        logicalRoot.setMaxSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        root.setStyle("-fx-background-color: black;");
        root.getChildren().add(logicalRoot);
        Scene scene = new Scene(root, Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        scene.setOnKeyPressed(event -> onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> onKeyReleased(event.getCode()));
        stage.setScene(scene);
        root.widthProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        root.heightProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        showMenu();
        new AnimationTimer() {
            @Override public void handle(long now) {
                if (state == GameState.WORLD || state == GameState.MENU) updateWorld();
            }
        }.start();
        stage.show();
        updateViewportScale();
    }

    private void createWorld() {
        world.getChildren().clear();
        world.setPrefSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);
        world.setStyle("-fx-background-color: linear-gradient(to bottom, #202333, #36394a);");
        for (double[] segment : GROUND_SEGMENTS) {
            Rectangle ground = new Rectangle(segment[0], Constants.GROUND_Y,
                segment[1] - segment[0], Constants.GROUND_HEIGHT);
            ground.setFill(Color.web("#596275"));
            world.getChildren().add(ground);
        }
        for (Platform platform : PLATFORMS) {
            world.getChildren().add(platform.getNode());
        }
        Label start = new Label("SYSTEM START");
        start.setTextFill(Color.WHITE); start.setLayoutX(45); start.setLayoutY(Constants.GROUND_Y + 4);
        for (Gate gate : gates) world.getChildren().add(gate.getNode());
        world.getChildren().addAll(player.getNode(), start);
        player.setLevel(WORLD_WIDTH, GROUND_SEGMENTS, PLATFORMS);
        viewport.getChildren().setAll(world);
        viewport.setPrefSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setMinSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setMaxSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setClip(new Rectangle(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT));
    }

    private void showMenu() {
        state = GameState.MENU;
        prompt = null;
        createWorld();
        refreshWall();
        player.setLeft(false);
        player.setRight(false);
        VBox box = new VBox(18);
        box.setAlignment(Pos.CENTER);
        // The menu is only an overlay: the live world stays visible behind it.
        box.setStyle("-fx-background-color: transparent;");
        Label title = title("SystemBound: The Paradigm Trials", 42);
        Label subtitle = text("CMSC 13 — JavaFX Game Skeleton", 17);
        Button play = new Button("PLAY");
        play.setDefaultButton(true); play.setOnAction(e -> showWorld());
        Button help = new Button("HOW TO PLAY"); help.setOnAction(e -> showHowToPlay());
        Button exit = new Button("EXIT"); exit.setOnAction(e -> ((Stage) root.getScene().getWindow()).close());
        box.getChildren().addAll(title, subtitle, play, help, exit);
        logicalRoot.getChildren().setAll(viewport, box);
    }

    private void showHowToPlay() {
        state = GameState.HOW_TO_PLAY;
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER); box.setMaxWidth(700);
        Label instructions = text("HOW TO PLAY\n\nMove through the system, cross the gaps, and clear each trial gate.\n\n"
            + "A / Left Arrow — Move left\nD / Right Arrow — Move right\nSpace — Jump onto floating platforms\n"
            + "E — Enter a nearby trial\nEnter — Continue after a trial result\n\n"
            + "Fall into a hole and you respawn at the last checkpoint.\n"
            + "Earn 3 EXP from the 14 questions to clear a trial.", 18);
        Button back = new Button("BACK"); back.setOnAction(e -> showMenu());
        box.getChildren().addAll(title("HOW TO PLAY", 32), instructions, back);
        logicalRoot.getChildren().setAll(box);
    }

    private void showWorld() {
        state = GameState.WORLD;
        createWorld();
        refreshWall();
        prompt = text("", 16);
        prompt.setTextFill(Color.WHITE); prompt.setTranslateY(-300);
        logicalRoot.getChildren().setAll(viewport, prompt);
        logicalRoot.requestFocus();
    }

    /** Blocks the player at the first uncleared gate to their right. */
    private void refreshWall() {
        for (Gate gate : gates) {
            if (!gate.isCompleted()) {
                player.setWallLeft(gate.getLeftEdge());
                return;
            }
        }
        player.clearWall();
    }

    private void updateWorld() {
        player.update();
        double cameraX = Math.max(0, Math.min(player.getX() - Constants.LOGICAL_WIDTH / 4,
            WORLD_WIDTH - Constants.LOGICAL_WIDTH));
        world.setTranslateX(-cameraX);
        // Menus deliberately have no gameplay prompt, but their background still animates.
        if (prompt == null) return;
        Gate near = nearbyGate();
        if (near == null) {
            prompt.setText("");
        } else if (near.isCompleted()) {
            prompt.setText("TRIAL " + near.getTrialNumber() + " COMPLETE");
        } else {
            prompt.setText("Press E to Enter Trial " + near.getTrialNumber());
        }
    }

    private Gate nearbyGate() {
        for (Gate gate : gates) {
            if (gate.isNear(player)) return gate;
        }
        return null;
    }

    private static List<Gate> createGates() {
        List<Gate> gates = new ArrayList<>();
        for (int trialNumber = 1; trialNumber <= Constants.TRIAL_COUNT; trialNumber++) {
            gates.add(new Gate(trialNumber, Constants.trialX(trialNumber)));
        }
        return gates;
    }

    private void startTrial(Gate gate) {
        if (gate == null || gate.isCompleted()) return;
        activeGate = gate;
        state = GameState.TRIAL;
        trial = new Trial(questionBank, gate.getTrialNumber());
        showQuestion();
    }

    private void showQuestion() {
        Question question = trial.getCurrentQuestion();
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER); box.setMaxWidth(880);
        Label exp = text(
            "TRIAL " + trial.getNumber() + "    EXP: "
            + trial.getExp() 
            + " / " 
            + Trial.REQUIRED_EXP,
             18
        );

        Label kernel = text(
            "KERNEL:\n\"" 
            + question.getQuestionText() 
            + "\"", 
            22
        );

        GridPane answers = new GridPane(); answers.setAlignment(Pos.CENTER); answers.setHgap(16); answers.setVgap(16);
        for (int i = 0; i < 4; i++) {
            int answer = i;
            Button button = new Button((char) ('A' + i) + ". " + question.getChoices().get(i));
            button.setPrefWidth(380); button.setPrefHeight(55);
            button.setWrapText(true); button.setOnAction(e -> answerQuestion(answer));
            answers.add(button, i % 2, i / 2);
        }
        box.getChildren().addAll(title("TRIAL " + trial.getNumber(), 28), exp, kernel, answers);
        logicalRoot.getChildren().setAll(viewport, box);
    }

    private void answerQuestion(int answer) {
        Question question = trial.getCurrentQuestion();
        boolean correct = trial.answer(answer);
        Label playerLine = text("PLAYER:\n\"" + question.getChoices().get(answer) + "\"\n\n"
            + "PLAYER: \"" + (correct ? "Yes!" : "Oh no...") + "\"\n\n"
            + "KERNEL: \"" + (correct ? "Correct. +1 EXP." : "Incorrect. +0 EXP.") + "\"", 19);
        logicalRoot.getChildren().setAll(viewport, playerLine);
        PauseTransition pause = new PauseTransition(Duration.seconds(1.2));
        pause.setOnFinished(e -> {
            trial.nextQuestion();
            if (trial.isFinished()) showResult(); else showQuestion();
        });
        pause.play();
    }

    private void showResult() {
        state = GameState.RESULT;
        boolean passed = trial.isComplete();
        if (passed) {
            activeGate.setCompleted(true);
            player.setCheckpoint(activeGate.getLeftEdge() + 25);
            refreshWall();
        }
        Label result = text((passed ? "TRIAL COMPLETE" : "TRIAL FAILED") + "\n\nEXP EARNED: "
            + trial.getExp() + " / " + Trial.REQUIRED_EXP + "\n\n"
            + (passed ? "Press ENTER to return to the map."
                      : "The Kernel returns you to the Trial " + trial.getNumber() + " checkpoint.\nPress ENTER to retry."), 25);
        result.setTextFill(passed ? Color.web("#8ee6a1") : Color.web("#ff9a9a"));
        logicalRoot.getChildren().setAll(viewport, result);
    }

    private void finishResult() {
        if (!trial.isComplete()) player.respawnAt(activeGate.getLeftEdge() - 30);
        showWorld();
    }

    private void onKeyPressed(KeyCode key) {
        if (state == GameState.WORLD) {
            if (key == KeyCode.A || key == KeyCode.LEFT) player.setLeft(true);
            if (key == KeyCode.D || key == KeyCode.RIGHT) player.setRight(true);
            if (key == KeyCode.SPACE) player.jump();
            if (key == KeyCode.E) startTrial(nearbyGate());
            if (key == KeyCode.ESCAPE) showMenu();
        } else if (state == GameState.RESULT && key == KeyCode.ENTER) {
            finishResult();
        } else if (state == GameState.HOW_TO_PLAY && key == KeyCode.ESCAPE) {
            showMenu();
        }
    }

    private void onKeyReleased(KeyCode key) {
        if (key == KeyCode.A || key == KeyCode.LEFT) player.setLeft(false);
        if (key == KeyCode.D || key == KeyCode.RIGHT) player.setRight(false);
    }

    /** Fits the fixed logical viewport into the window with centered letterboxing. */
    private void updateViewportScale() {
        if (root.getWidth() <= 0 || root.getHeight() <= 0) return;
        double scale = Math.min(root.getWidth() / Constants.LOGICAL_WIDTH,
            root.getHeight() / Constants.LOGICAL_HEIGHT);
        logicalRoot.setScaleX(scale);
        logicalRoot.setScaleY(scale);
    }

    private Label title(String value, int size) { 
        Label label = text(value, size); label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;"); return label; 
    }
    private Label text(String value, int size) { 
        Label label = new Label(value); label.setStyle("-fx-font-size: " + size + "; -fx-text-fill: white;"); label.setWrapText(true); label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER); return label; 
    }
}
