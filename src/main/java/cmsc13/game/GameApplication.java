package cmsc13.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    private static final double WORLD_WIDTH = Constants.WORLD_WIDTH;

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
    private final Pane backgroundPane = new Pane();
    private final Pane world = new Pane();
    private final Pane viewport = new Pane();
    private final Player player = new Player();
    private final List<Gate> gates = createGates();
    private final List<LeafSprite> leaves = createLeaves();
    private final QuestionBank questionBank = new QuestionBank();
    private static final Image[] LEAF_FRAMES = loadLeafFrames();
    private final ImageView skyImage = createBackgroundImage("/BackGround/Map_sky.png");
    private final ImageView mountainLayerFar = createBackgroundImage("/BackGround/Mountains.png");
    private final ImageView mountainLayerNear = createBackgroundImage("/BackGround/Mountains.png");
    private final ImageView cloudLayerFar = createBackgroundImage("/BackGround/Clouds.png");
    private final ImageView cloudLayerNear = createBackgroundImage("/BackGround/Clouds.png");
    private final ImageView groundImage = createBackgroundImage("/BackGround/Map_ground.png");
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
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
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
        world.setStyle("-fx-background-color: transparent;");

        backgroundPane.getChildren().setAll(
            skyImage,
            mountainLayerFar,
            mountainLayerNear,
            cloudLayerFar,
            cloudLayerNear,
            groundImage
        );
        backgroundPane.setPrefSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);
        backgroundPane.setMinSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);
        backgroundPane.setMaxSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);

        skyImage.setFitWidth(WORLD_WIDTH);
        skyImage.setFitHeight(Constants.LOGICAL_HEIGHT);
        skyImage.setLayoutX(0);
        skyImage.setLayoutY(0);

        mountainLayerFar.setFitWidth(WORLD_WIDTH * 1.6);
        mountainLayerFar.setFitHeight(Constants.LOGICAL_HEIGHT);
        mountainLayerFar.setLayoutX(0);
        mountainLayerFar.setLayoutY(0);

        mountainLayerNear.setFitWidth(WORLD_WIDTH * 1.8);
        mountainLayerNear.setFitHeight(Constants.LOGICAL_HEIGHT);
        mountainLayerNear.setLayoutX(0);
        mountainLayerNear.setLayoutY(0);

        cloudLayerFar.setFitWidth(WORLD_WIDTH * 2.1);
        cloudLayerFar.setFitHeight(Constants.LOGICAL_HEIGHT);
        cloudLayerFar.setLayoutX(0);
        cloudLayerFar.setLayoutY(0);

        cloudLayerNear.setFitWidth(WORLD_WIDTH * 2.3);
        cloudLayerNear.setFitHeight(Constants.LOGICAL_HEIGHT);
        cloudLayerNear.setLayoutX(0);
        cloudLayerNear.setLayoutY(0);

        groundImage.setFitWidth(WORLD_WIDTH);
        groundImage.setFitHeight(Constants.GROUND_HEIGHT);
        groundImage.setLayoutY(Constants.GROUND_Y);
        groundImage.setLayoutX(0);

        Rectangle groundCollision = new Rectangle(0, Constants.GROUND_Y, WORLD_WIDTH, Constants.GROUND_HEIGHT);
        groundCollision.setFill(Color.web("#596275"));
        groundCollision.setMouseTransparent(true);
        world.getChildren().add(groundCollision);

        for (Platform platform : PLATFORMS) {
            world.getChildren().add(platform.getNode());
        }
        for (LeafSprite leaf : leaves) {
            world.getChildren().add(leaf.node);
        }
        Label start = new Label("SYSTEM START");
        start.setTextFill(Color.WHITE); start.setLayoutX(45); start.setLayoutY(Constants.GROUND_Y + 4);
        for (Gate gate : gates) world.getChildren().add(gate.getNode());
        world.getChildren().addAll(player.getNode(), start);
        player.setLevel(WORLD_WIDTH, GROUND_SEGMENTS, PLATFORMS);
        viewport.getChildren().setAll(backgroundPane, world);
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

    private Gate wallCompleted() {

        Gate notCompleteGate = null;
        for (Gate gate : gates) {
            if(!gate.isCompleted()) {
                notCompleteGate = gate;
                break;
            }
        }
        
        return notCompleteGate;
    }

    private void updateWorld() {
        player.update();
        double cameraX;
        Gate gateCheck = wallCompleted();

        if(gateCheck != null) {
            cameraX = Math.max(0, Math.min(player.getX() - Constants.LOGICAL_WIDTH / 4,
                (gateCheck.getX() + Constants.TILE_SIZE) - Constants.LOGICAL_WIDTH));
        } else {
            cameraX = Math.max(0, Math.min(player.getX() - Constants.LOGICAL_WIDTH / 4,
                WORLD_WIDTH - Constants.LOGICAL_WIDTH));
        }

        world.setTranslateX(-cameraX);
        updateBackgroundParallax(cameraX);
        updateLeaves();
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

    private void updateBackgroundParallax(double cameraX) {
        double skyOffset = cameraX * Constants.SKY_SCROLL_SPEED;
        double mountainOffset = cameraX * Constants.MOUNTAIN_SCROLL_SPEED;
        double cloudOffset = cameraX * Constants.CLOUD_SCROLL_SPEED;

        skyImage.setLayoutX(-skyOffset);
        mountainLayerFar.setLayoutX(-mountainOffset);
        mountainLayerNear.setLayoutX(-mountainOffset * 1.5);
        cloudLayerFar.setLayoutX(-cloudOffset);
        cloudLayerNear.setLayoutX(-cloudOffset * 1.5);
    }

    private void updateLeaves() {
        double time = System.nanoTime() / 1_000_000_000.0;
        for (LeafSprite leaf : leaves) {
            double phase = leaf.hitTimer > 0 ? leaf.phase + 1.3 : leaf.phase;
            double speed = leaf.hitTimer > 0 ? Constants.LEAF_HIT_SPEED : Constants.LEAF_SWAY_SPEED;
            double sway = Math.sin(time * speed + phase) * (leaf.hitTimer > 0 ? Constants.LEAF_HIT_AMPLITUDE : Constants.LEAF_SWAY_AMPLITUDE);
            double drift = Math.sin(time * (speed * 1.5) + phase * 1.7) * (leaf.hitTimer > 0 ? 8.0 : 3.6);

            leaf.node.setLayoutX(leaf.baseX + sway);
            leaf.node.setLayoutY(leaf.baseY + drift);
            leaf.node.setRotate(Math.toDegrees((12 * Math.sin(1.5 * time + phase)) + (3 * Math.sin(4.2 * time + phase))));

            if (leaf.hitTimer > 0) {
                leaf.hitTimer = Math.max(0, leaf.hitTimer - 0.05);
                if (leaf.hitTimer == 0) leaf.hit = false;
            }

            long frameDelay = leaf.hitTimer > 0 ? Constants.LEAF_HIT_FRAME_DURATION : Constants.LEAF_FRAME_DURATION;
            if (leaf.lastFrameTime == 0 || (System.nanoTime() - leaf.lastFrameTime) >= frameDelay) {
                leaf.lastFrameTime = System.nanoTime();
                leaf.frameIndex = (leaf.frameIndex + 1) % LEAF_FRAMES.length;
                leaf.node.setImage(LEAF_FRAMES[leaf.frameIndex]);
            }

            if (!leaf.hit && playerIntersectsLeaf(leaf)) {
                leaf.hit = true;
                leaf.hitTimer = 0.8;
                player.hitLeaf();
            }
        }
    }

    private boolean playerIntersectsLeaf(LeafSprite leaf) {
        double playerCenterX = player.getX() + Player.WIDTH / 2.0;
        double playerCenterY = player.getY() + Player.HEIGHT / 2.0;
        double leafCenterX = leaf.node.getLayoutX() + leaf.node.getFitWidth() / 2.0;
        double leafCenterY = leaf.node.getLayoutY() + leaf.node.getFitHeight() / 2.0;
        return Math.abs(playerCenterX - leafCenterX) < Constants.LEAF_COLLISION_WIDTH
            && Math.abs(playerCenterY - leafCenterY) < Constants.LEAF_COLLISION_HEIGHT;
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

    private static List<LeafSprite> createLeaves() {
        Random random = new Random(7L);
        List<LeafSprite> leaves = new ArrayList<>();
        for (int index = 0; index < Constants.LEAF_COUNT; index++) {
            double x = 72 + random.nextDouble() * (WORLD_WIDTH - 144);
            double y = Constants.GROUND_Y - (8 + random.nextDouble() * 12);
            double size = 9 + random.nextDouble() * 13;
            ImageView leafNode = createLeafNode(size);
            leafNode.setLayoutX(x);
            leafNode.setLayoutY(y);
            leaves.add(new LeafSprite(leafNode, x, y, random.nextDouble() * Math.PI * 2.0));
        }
        return leaves;
    }

    private static ImageView createLeafNode(double size) {
        ImageView imageView = new ImageView(LEAF_FRAMES[0]);
        imageView.setFitWidth(size);
        imageView.setFitHeight(size);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        imageView.setOpacity(0.92);
        return imageView;
    }

    private static Image[] loadLeafFrames() {
        Image[] frames = new Image[15];
        for (int index = 1; index <= frames.length; index++) {
            frames[index - 1] = new Image(GameApplication.class.getResource("/BackGround/leaves/leaves" + index + ".png").toExternalForm());
        }
        return frames;
    }

    private static ImageView createBackgroundImage(String resourcePath) {
        Image image = new Image(GameApplication.class.getResource(resourcePath).toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(false);
        imageView.setSmooth(false);
        return imageView;
    }

    // private static void configureBackgroundImage(ImageView imageView, double scrollSpeed, double width, double y, double x) {
    //     imageView.setFitWidth(width);
    //     imageView.setFitHeight(Constants.LOGICAL_HEIGHT);
    //     imageView.setLayoutX(x);
    //     imageView.setLayoutY(y);
    //     imageView.setOpacity(0.92);
    // }

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

    private void onKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (event.isControlDown() && event.isShiftDown()) {
            if (key == KeyCode.T) {
                completeNearestGate();
                return;
            }
            if (key == KeyCode.C) {
                completeAllGates();
                return;
            }
        }
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

    private void onKeyReleased(KeyEvent event) {
        KeyCode key = event.getCode();
        if (key == KeyCode.A || key == KeyCode.LEFT) player.setLeft(false);
        if (key == KeyCode.D || key == KeyCode.RIGHT) player.setRight(false);
    }

    private void completeNearestGate() {
        Gate gate = nearbyGate();
        if (gate == null) return;
        gate.setCompleted(true);
        player.setCheckpoint(gate.getLeftEdge() + 25);
        refreshWall();
    }

    private void completeAllGates() {
        for (Gate gate : gates) {
            gate.setCompleted(true);
        }
        player.clearWall();
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

    private static final class LeafSprite {
        private final ImageView node;
        private final double baseX;
        private final double baseY;
        private final double phase;
        private boolean hit;
        private double hitTimer;
        private long lastFrameTime;
        private int frameIndex;

        private LeafSprite(ImageView node, double baseX, double baseY, double phase) {
            this.node = node;
            this.baseX = baseX;
            this.baseY = baseY;
            this.phase = phase;
            this.lastFrameTime = 0L;
            this.frameIndex = 0;
        }
    }
}
