package cmsc13.game;

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

/** Controls the menu, one world, and one Trial 1 quiz loop. */
public final class GameApplication extends Application {
    private static final int WIDTH = 1280;
    private static final int HEIGHT = 720;
    private static final double GATE_X = 1200;
    private final StackPane root = new StackPane();
    private final Pane world = new Pane();
    private final Pane viewport = new Pane();
    private final Player player = new Player();
    private final Gate gate = new Gate(GATE_X);
    private final QuestionBank questionBank = new QuestionBank();
    private GameState state = GameState.MENU;
    private Trial trial;
    private Label prompt;
    private boolean trialOneComplete;

    @Override
    public void start(Stage stage) {
        stage.setTitle("SystemBound: The Paradigm Trials");
        stage.setResizable(false);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        scene.setOnKeyPressed(event -> onKeyPressed(event.getCode()));
        scene.setOnKeyReleased(event -> onKeyReleased(event.getCode()));
        stage.setScene(scene);
        showMenu();
        new AnimationTimer() {
            @Override public void handle(long now) {
                if (state == GameState.WORLD) updateWorld();
            }
        }.start();
        stage.show();
    }

    private void createWorld() {
        world.getChildren().clear();
        world.setPrefSize(3000, HEIGHT);
        world.setStyle("-fx-background-color: linear-gradient(to bottom, #202333, #36394a);");
        Rectangle ground = new Rectangle(0, Player.GROUND_Y, 3000, 120);
        ground.setFill(Color.web("#596275"));
        Label start = new Label("SYSTEM START");
        start.setTextFill(Color.WHITE); start.setLayoutX(45); start.setLayoutY(630);
        world.getChildren().addAll(ground, gate.getNode(), player.getNode(), start);
        viewport.getChildren().setAll(world);
        viewport.setPrefSize(WIDTH, HEIGHT);
        viewport.setMinSize(WIDTH, HEIGHT);
        viewport.setMaxSize(WIDTH, HEIGHT);
        viewport.setClip(new Rectangle(WIDTH, HEIGHT));
    }

    private void showMenu() {
        state = GameState.MENU;
        VBox box = new VBox(18);
        box.setAlignment(Pos.CENTER);
        Label title = title("SystemBound: The Paradigm Trials", 42);
        Label subtitle = text("CMSC 13 — JavaFX Game Skeleton", 17);
        Button play = new Button("PLAY");
        play.setDefaultButton(true); play.setOnAction(e -> showWorld());
        Button help = new Button("HOW TO PLAY"); help.setOnAction(e -> showHowToPlay());
        Button exit = new Button("EXIT"); exit.setOnAction(e -> ((Stage) root.getScene().getWindow()).close());
        box.getChildren().addAll(title, subtitle, play, help, exit);
        root.getChildren().setAll(box);
    }

    private void showHowToPlay() {
        state = GameState.HOW_TO_PLAY;
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER); box.setMaxWidth(700);
        Label instructions = text("HOW TO PLAY\n\nMove through the system and reach Trial 1.\n\nA / Left Arrow — Move left\nD / Right Arrow — Move right\nSpace — Jump\nE — Enter a nearby trial\nEnter — Continue after a trial result\n\nEarn 3 EXP from 5 questions to complete Trial 1.", 18);
        Button back = new Button("BACK"); back.setOnAction(e -> showMenu());
        box.getChildren().addAll(title("HOW TO PLAY", 32), instructions, back);
        root.getChildren().setAll(box);
    }

    private void showWorld() {
        state = GameState.WORLD;
        createWorld();
        if (trialOneComplete) player.clearWall(); else player.setWallLeft(gate.getLeftEdge());
        prompt = text("", 16);
        prompt.setTextFill(Color.WHITE); prompt.setTranslateY(-300);
        root.getChildren().setAll(viewport, prompt);
        root.requestFocus();
    }

    private void updateWorld() {
        player.update();
        double cameraX = Math.max(0, Math.min(player.getX() - 320, 3000 - WIDTH));
        world.setTranslateX(-cameraX);
        if (gate.isNear(player)) {
            prompt.setText(trialOneComplete ? "TRIAL 1 COMPLETE" : "Press E to Enter Trial 1");
        } else {
            prompt.setText("");
        }
    }

    private void startTrial() {
        if (trialOneComplete) return;
        state = GameState.TRIAL;
        trial = new Trial(questionBank, 1);
        showQuestion();
    }

    private void showQuestion() {
        Question question = trial.getCurrentQuestion();
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER); box.setMaxWidth(880);
        Label exp = text(
            "TRIAL 1    EXP: " 
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
        box.getChildren().addAll(title("TRIAL 1", 28), exp, kernel, answers);
        root.getChildren().setAll(viewport, box);
    }

    private void answerQuestion(int answer) {
        Question question = trial.getCurrentQuestion();
        boolean correct = trial.answer(answer);
        Label playerLine = text("PLAYER:\n\"" + question.getChoices().get(answer) + "\"\n\n"
            + "PLAYER: \"" + (correct ? "Yes!" : "Oh no...") + "\"\n\n"
            + "KERNEL: \"" + (correct ? "Correct. +1 EXP." : "Incorrect. +0 EXP.") + "\"", 19);
        root.getChildren().setAll(viewport, playerLine);
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
        if (passed) { trialOneComplete = true; gate.setCompleted(true); }
        Label result = text((passed ? "TRIAL COMPLETE" : "TRIAL FAILED") + "\n\nEXP EARNED: "
            + trial.getExp() + " / " + Trial.REQUIRED_EXP + "\n\n"
            + (passed ? "Press ENTER to return to the map." : "The Kernel returns you to the Trial 1 checkpoint.\nPress ENTER to retry."), 25);
        result.setTextFill(passed ? Color.web("#8ee6a1") : Color.web("#ff9a9a"));
        root.getChildren().setAll(viewport, result);
    }

    private void finishResult() {
        if (!trial.isComplete()) player.respawnAtTrialOne();
        showWorld();
    }

    private void onKeyPressed(KeyCode key) {
        if (state == GameState.WORLD) {
            if (key == KeyCode.A || key == KeyCode.LEFT) player.setLeft(true);
            if (key == KeyCode.D || key == KeyCode.RIGHT) player.setRight(true);
            if (key == KeyCode.SPACE) player.jump();
            if (key == KeyCode.E && gate.isNear(player)) startTrial();
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

    private Label title(String value, int size) { 
        Label label = text(value, size); label.setStyle("-fx-font-weight: bold; -fx-text-fill: white;"); return label; 
    }
    private Label text(String value, int size) { 
        Label label = new Label(value); label.setStyle("-fx-font-size: " + size + "; -fx-text-fill: white;"); label.setWrapText(true); label.setTextAlignment(javafx.scene.text.TextAlignment.CENTER); return label; 
    }
}