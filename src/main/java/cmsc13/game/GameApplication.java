package cmsc13.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Properties;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.KeyValue;
import javafx.animation.Interpolator;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.shape.Rectangle;
import javafx.geometry.Rectangle2D;
import javafx.stage.Stage;
import javafx.util.Duration;

/** Controls the menu, one world with simple platforming, and the trial quiz loop. */
public final class GameApplication extends Application {
    private static final double WORLD_WIDTH = Constants.WORLD_WIDTH;
    /** How long the splash art holds before the "press any key" prompt appears. */
    private static final double SPLASH_SECONDS = 5;

    /** The long map starts as a continuous walkable ground layer. */
    private static final List<double[]> GROUND_SEGMENTS = List.of(
        new double[] {0, 1500}, new double[] {1580, 2940}, new double[] {3030, 4700},
        new double[] {4800, 6900}, new double[] {7000, 9400}, new double[] {9500, WORLD_WIDTH}
    );
    /** Placeholder platforms for the first section of the long map. */
    private static final List<Platform> PLATFORMS = List.of(
        new Platform(368, 300, 110), new Platform(527, 250, 120), new Platform(1510, 330, 95),
        new Platform(2950, 320, 100), new Platform(4700, 315, 115), new Platform(6890, 325, 120)
    );

    private final StackPane root = new StackPane();
    /** Fixed logical viewport; the outer root only provides the physical window. */
    private final StackPane logicalRoot = new StackPane();
    private final Pane backgroundPane = new Pane();
    private final Pane world = new Pane();
    private final Pane viewport = new Pane();
    private final Player player = new Player();
    private final QuestionBank questionBank = new QuestionBank();
    private final TrialManager trialManager = new TrialManager(questionBank);
    private final GameMenuView menuView = new GameMenuView();
    private final List<Gate> gates = createGates(questionBank);
    private final List<LeafSprite> leaves = createLeaves();
    /** Question IDs unlocked by correctly answering them during gameplay. */
    private final Set<Integer> unlockedQuestionIds = new HashSet<>();
    /** A small properties file stores durable score, gate, and archive progress on this device. */
    private final Path savePath = Paths.get(System.getProperty("user.home"), ".systembound", "progress.properties");
    private static final Image[] LEAF_FRAMES = loadLeafFrames();
    private final ImageView skyImage = createBackgroundImage("/BackGround/Map_sky.png");
    private final ImageView mountainLayerFar = createBackgroundImage("/BackGround/Mountains.png");
    private final ImageView mountainLayerNear = createBackgroundImage("/BackGround/Mountains.png");
    private final ImageView cloudLayerFar = createBackgroundImage("/BackGround/Clouds.png");
    private final ImageView cloudLayerNear = createBackgroundImage("/BackGround/Clouds.png");
    private final ImageView groundImage = createBackgroundImage("/BackGround/Map_ground.png");
    private final ImageView kernel = createKernel();
    private GameState state = GameState.MENU;
    private Trial trial;
    private Gate activeGate;
    private Label prompt;
    private final Label terminalHeader = new Label();
    private final Label expHud = new Label();
    private final Label trialHud = new Label();
    /** Progress indicator for the active trial only; it is intentionally separate from total EXP. */
    private final ProgressBar trialExpBar = new ProgressBar(0);
    private final Label trialExpLabel = new Label();
    private int trialExp;
    private boolean cursorVisible = true;
    private boolean splashReady;
    private long lastCursorToggle;
    private int totalExp;
    private boolean peekUsed, copyUsed, saveUsed, saveArmed;
    private HelperAdvisor.Advice selectedAdvice;
    private Lifeline selectedLifeline;

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
        scene.getStylesheets().add(GameApplication.class.getResource("/Styles/game.css").toExternalForm());
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        stage.setScene(scene);
        // Save immediately when the window closes so a menu or map exit keeps current progress.
        stage.setOnCloseRequest(event -> saveProgress());
        root.widthProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        root.heightProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        loadProgress();
        showSplash();
        new AnimationTimer() {
            @Override public void handle(long now) {
                if (state == GameState.WORLD || state == GameState.MENU) updateWorld();
                for (Gate gate : gates) gate.tick(now);
                if (now - lastCursorToggle > 550_000_000L) {
                    cursorVisible = !cursorVisible; lastCursorToggle = now; refreshHud();
                }
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
            cloudLayerNear
        );
        backgroundPane.setPrefSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);
        backgroundPane.setMinSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);
        backgroundPane.setMaxSize(WORLD_WIDTH, Constants.LOGICAL_HEIGHT);

        
        skyImage.setFitWidth(WORLD_WIDTH);
        skyImage.setFitHeight(0);
        skyImage.setLayoutX(0);
        skyImage.setLayoutY(-150);

        configureParallaxLayer(mountainLayerFar, 0.55, 40);
        mountainLayerFar.setLayoutX(0);
        mountainLayerFar.setLayoutY(0);

        configureParallaxLayer(mountainLayerNear, 0.74, 60);
        mountainLayerNear.setLayoutX(0);
        mountainLayerNear.setLayoutY(0);

        configureParallaxLayer(cloudLayerFar, 0.55, 40);
        cloudLayerFar.setLayoutX(0);
        cloudLayerFar.setLayoutY(0);

        configureParallaxLayer(cloudLayerNear, 0.72, 20);
        cloudLayerNear.setLayoutX(0);
        cloudLayerNear.setLayoutY(0);

        // Map_ground is a full-height strip. Display its lower six tile rows only.
        groundImage.setViewport(new Rectangle2D(0, 576 - Constants.GROUND_HEIGHT, 15360, Constants.GROUND_HEIGHT));
        groundImage.setFitWidth(WORLD_WIDTH);
        groundImage.setFitHeight(0);
        groundImage.setLayoutY(Constants.GROUND_Y);
        groundImage.setLayoutX(0);
        world.getChildren().add(groundImage);

        // Collision is defined by GROUND_SEGMENTS, so the matching visual gaps remain visible.
        for (double[] segment : GROUND_SEGMENTS) {
            Rectangle groundCollision = new Rectangle(segment[0], Constants.GROUND_Y, segment[1] - segment[0], Constants.GROUND_HEIGHT);
            groundCollision.setFill(Color.TRANSPARENT); groundCollision.setMouseTransparent(true); world.getChildren().add(groundCollision);
        }
        for (int i = 0; i < GROUND_SEGMENTS.size() - 1; i++) {
            double pitLeft = GROUND_SEGMENTS.get(i)[1];
            double pitRight = GROUND_SEGMENTS.get(i + 1)[0];
            Rectangle pit = new Rectangle(pitLeft, Constants.GROUND_Y, pitRight - pitLeft, Constants.GROUND_HEIGHT);
            pit.setFill(Color.web("#11131b")); pit.setStroke(Color.web("#34384a")); pit.setStrokeWidth(2);
            world.getChildren().add(pit);
        }

        for (Platform platform : PLATFORMS) {
            world.getChildren().add(platform.getNode());
        }
        for (LeafSprite leaf : leaves) {
            world.getChildren().add(leaf.node);
        }
        Label start = new Label("SYSTEM START");
        start.setTextFill(Color.WHITE); start.setLayoutX(45); start.setLayoutY(Constants.GROUND_Y + 4);
        for (Gate gate : gates) world.getChildren().add(gate.getNode());
        world.getChildren().add(kernel);
        world.getChildren().addAll(player.getNode(), start);
        player.setLevel(WORLD_WIDTH, GROUND_SEGMENTS, PLATFORMS);
        viewport.getChildren().setAll(backgroundPane, world);
        viewport.setPrefSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setMinSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setMaxSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        viewport.setClip(new Rectangle(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT));
    }

    /** Title card shown on launch: the splash art holds for a few seconds, then waits for any key. */
    private void showSplash() {
        state = GameState.LOADING;
        splashReady = false;

        ImageView splash = createBackgroundImage("/SPLASH SCREEN.png");
        splash.setFitWidth(Constants.LOGICAL_WIDTH);
        splash.setFitHeight(Constants.LOGICAL_HEIGHT);
        splash.setPreserveRatio(true);

        Label continuePrompt = title("PRESS ANY KEY TO CONTINUE", 20);
        continuePrompt.setVisible(false);
        StackPane.setAlignment(continuePrompt, Pos.BOTTOM_CENTER);
        StackPane.setMargin(continuePrompt, new Insets(0, 0, 40, 0));

        StackPane splashScreen = new StackPane(splash, continuePrompt);
        splashScreen.setStyle("-fx-background-color: black;");
        splashScreen.setMinSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        splashScreen.setPrefSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        splashScreen.setMaxSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        logicalRoot.getChildren().setAll(splashScreen);

        PauseTransition hold = new PauseTransition(Duration.seconds(SPLASH_SECONDS));
        hold.setOnFinished(e -> {
            splashReady = true;
            continuePrompt.setVisible(true);
            FadeTransition blink = new FadeTransition(Duration.seconds(.7), continuePrompt);
            blink.setFromValue(1); blink.setToValue(.15);
            blink.setCycleCount(FadeTransition.INDEFINITE); blink.setAutoReverse(true);
            blink.play();
        });
        hold.play();
    }

    private void showMenu() {
        state = GameState.MENU;
        prompt = null;
        createWorld();
        refreshWall();
        player.setLeft(false);
        player.setRight(false);
        Node menu = menuView.mainMenu(viewport, this::showWorld, this::showHowToPlay,
            this::showStories, this::showCredits, this::showQuestionBank,
            () -> ((Stage) root.getScene().getWindow()).close());
        configureHud();
        logicalRoot.getChildren().setAll(menu, terminalHeader, expHud);
    }

    /** Opens the locked/unlocked question catalogue. */
    private void showQuestionBank() {
        state = GameState.MENU;
        logicalRoot.getChildren().setAll(new QuestionBankView(questionBank, unlockedQuestionIds, this::showMenu));
    }

    /** Displays the dedicated help page. */
    private void showHowToPlay() {
        state = GameState.HOW_TO_PLAY;
        logicalRoot.getChildren().setAll(menuView.howToPlay(this::showMenu));
    }

    /** Opens the placeholder for future plot videos and cutscenes. */
    private void showStories() {
        state = GameState.STORIES;
        logicalRoot.getChildren().setAll(menuView.placeholder("STORIES", this::showMenu));
    }

    /** Opens the empty credits placeholder. */
    private void showCredits() {
        state = GameState.CREDITS;
        logicalRoot.getChildren().setAll(menuView.placeholder("CREDITS", this::showMenu));
    }

    private void showWorld() {
        state = GameState.WORLD;
        createWorld();
        refreshWall();
        prompt = new Label("Press 'E' to start");
        prompt.setMouseTransparent(true);
        prompt.setAlignment(Pos.CENTER);
        prompt.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13; -fx-font-weight: bold; -fx-text-fill: #112630; -fx-background-color: rgba(232,250,255,.95); -fx-background-radius: 12; -fx-padding: 8 12; -fx-border-color: #63d7e8; -fx-border-radius: 12;");
        world.getChildren().add(prompt);
        kernel.setLayoutY(Constants.GROUND_Y - Constants.TILE_SIZE * 5 + 20);
        configureHud();
        logicalRoot.getChildren().setAll(viewport, terminalHeader, expHud, trialHud, controlsHud());
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
                gateCheck.getX() - Constants.LOGICAL_WIDTH / 2.0));
        } else {
            cameraX = Math.max(0, Math.min(player.getX() - Constants.LOGICAL_WIDTH / 4,
                WORLD_WIDTH - Constants.LOGICAL_WIDTH));
        }

        world.setTranslateX(-cameraX);
        updateBackgroundParallax(cameraX);
        updateWorldColor();
        updateLeaves();
        // Menus deliberately have no gameplay prompt, but their background still animates.
        if (prompt == null) return;
        Gate near = nearbyGate();
        boolean canStart = near != null && !near.isCompleted();
        prompt.setVisible(canStart);
        if (canStart) {
            prompt.setText("Press 'E' to start Trial " + near.getTrialNumber());
            prompt.setLayoutX(near.getX() - 20);
            prompt.setLayoutY(Constants.GROUND_Y - 105);
        }
    }

    private void updateBackgroundParallax(double cameraX) {
        double mountainOffset = cameraX * Constants.MOUNTAIN_SCROLL_SPEED;
        double cloudOffset = cameraX * Constants.CLOUD_SCROLL_SPEED;
        double skyOffset = cameraX * Constants.SKY_SCROLL_SPEED;

        mountainLayerFar.setLayoutX(-mountainOffset);
        mountainLayerNear.setLayoutX(-mountainOffset * 2);
        cloudLayerFar.setLayoutX(-cloudOffset);
        cloudLayerNear.setLayoutX(-cloudOffset * 1.8);
        skyImage.setLayoutX(-skyOffset);
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
            }

            long frameDelay = leaf.hitTimer > 0 ? Constants.LEAF_HIT_FRAME_DURATION : Constants.LEAF_FRAME_DURATION;
            if (leaf.lastFrameTime == 0 || (System.nanoTime() - leaf.lastFrameTime) >= frameDelay) {
                leaf.lastFrameTime = System.nanoTime();
                leaf.frameIndex = (leaf.frameIndex + 1) % LEAF_FRAMES.length;
                leaf.node.setImage(LEAF_FRAMES[leaf.frameIndex]);
            }
        }
    }

    private Gate nearbyGate() {
        for (Gate gate : gates) {
            if (gate.isNear(player)) return gate;
        }
        return null;
    }

    /** Builds one gate for every trial present in the loaded question bank. */
    private static List<Gate> createGates(QuestionBank bank) {
        List<Gate> gates = new ArrayList<>();
        for (int trialNumber = 1; trialNumber <= bank.getTrialCount(); trialNumber++) {
            gates.add(new Gate(trialNumber, Constants.trialX(trialNumber), bank.getTrialCount()));
        }
        return gates;
    }

    private static ImageView createKernel() {
        Image image = new Image(GameApplication.class.getResource("/Kernel.png").toExternalForm());
        ImageView result = new ImageView(image);
        result.setFitWidth(Constants.TILE_SIZE * 5); 
        result.setFitHeight(Constants.TILE_SIZE * 5);
        result.setPreserveRatio(true); 
        result.setSmooth(false);
        result.setLayoutY((Constants.GROUND_Y - Constants.TILE_SIZE * 5) + 20);
        result.setLayoutX(Constants.trialX(1) + Constants.TILE_SIZE * 2);
        return result;
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
        imageView.setPreserveRatio(true);
        imageView.setSmooth(false);
        return imageView;
    }

    private void startTrial(Gate gate) {
        if (gate == null || gate.isCompleted()) return;
        activeGate = gate;
        kernel.setLayoutX(gate.getX() + Constants.TILE_SIZE * 2);
        state = GameState.TRIAL_CONFIRM;
        Label message = text("KERNEL:\n\"You've reached Trial " + gate.getTrialNumber()
            + ". Do you wish to challenge the system?\"", 23);
        Button begin = new Button("BEGIN TRIAL"); begin.setOnAction(e -> { state = GameState.TRIAL; trial = trialManager.startTrial(gate.getTrialNumber()); trialExp = 0; trialExpBar.setProgress(0); resetLifelines(); showQuestion(); });
        Button leave = new Button("NOT YET"); leave.setOnAction(e -> showWorld());
        HBox choices = new HBox(14, begin, leave);
        choices.setAlignment(Pos.CENTER);
        VBox box = new VBox(16, message, choices); 
        box.setAlignment(Pos.CENTER);
        box.setStyle("-fx-background-color: rgba(10, 20, 32, .86); -fx-padding: 28; -fx-background-radius: 20; -fx-border-color: #63d7e8; -fx-border-radius: 20;");
        logicalRoot.getChildren().setAll(viewport, box);
    }

    private void showQuestion() {
        Question question = trial.getCurrentQuestion();
        Pane overlay = new Pane();
        Label questionText = new Label("KERNEL\n" + question.getQuestionText());
        questionText.setWrapText(true);
        questionText.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: " + (question.getType() == QuestionType.PROGRAMMING ? 15 : 20) + "; -fx-text-fill: #111820; -fx-padding: 12 16 12 16;");
        ScrollPane questionScroll = new ScrollPane(questionText);
        questionScroll.setFitToWidth(true);
        questionScroll.setPrefSize(460, 330);
        questionScroll.setStyle("-fx-background: rgba(255,255,255,.95); -fx-background-radius: 18; -fx-border-color: #8a95a5; -fx-border-radius: 18;");
        Pane kernel = new Pane(questionScroll);
        kernel.setPrefSize(460, 330);
        kernel.setLayoutX(282); kernel.setLayoutY(18);
        GridPane answers = new GridPane(); answers.setHgap(16); answers.setVgap(14);
        for (int i = 0; i < 4; i++) {
            int answer = i;
            Label choice = new Label((char) ('A' + i) + ". " + question.getChoices().get(i));
            choice.setWrapText(true);
            choice.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: " + (question.getType() == QuestionType.PROGRAMMING ? 11 : 14) + "; -fx-text-fill: #111820;");
            ScrollPane choiceScroll = new ScrollPane(choice);
            choiceScroll.setFitToWidth(true);
            choiceScroll.setPrefViewportHeight(58);
            choiceScroll.setPrefViewportWidth(260);
            choiceScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            choiceScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            choiceScroll.setStyle("-fx-background-color: transparent; -fx-background: transparent;");
            Button button = new Button();
            button.setGraphic(choiceScroll);
            button.setPrefWidth(295); button.setPrefHeight(68);
            // The embedded scroll pane is the visible button face, so forward its mouse click.
            choiceScroll.setOnMouseClicked(event -> { button.fire(); event.consume(); });
            button.setOnAction(e -> answerQuestion(answer)); answers.add(button, i % 2, i / 2);
        }
        answers.setLayoutX(202); answers.setLayoutY(405);
        // Keep this trial meter inside the quiz view: it never shows the lifetime score or gate path.
        configureTrialMeter();
        VBox trialMeter = new VBox(5, trialExpLabel, trialExpBar);
        trialMeter.setLayoutX(382); trialMeter.setLayoutY(355);
        VBox lifelines = sidePanel("LIFELINES"); lifelines.setLayoutX(18); lifelines.setLayoutY(175);
        Button peek = sideButton(peekUsed ? "PEEK USED" : "PEEK", null); peek.setDisable(peekUsed); if (selectedLifeline == Lifeline.PEEK) peek.getStyleClass().add("lifeline-selected"); peek.setOnAction(e -> chooseLifeline(Lifeline.PEEK));
        Button copy = sideButton(copyUsed ? "COPY USED" : "COPY", null); copy.setDisable(copyUsed); if (selectedLifeline == Lifeline.COPY) copy.getStyleClass().add("lifeline-selected"); copy.setOnAction(e -> chooseLifeline(Lifeline.COPY));
        Button save = sideButton(saveUsed ? "SAVE USED" : "SAVE", null); save.setDisable(saveUsed); if (selectedLifeline == Lifeline.SAVE) save.getStyleClass().add("lifeline-selected"); save.setOnAction(e -> chooseLifeline(Lifeline.SAVE));
        lifelines.getChildren().addAll(peek, copy, save);
        VBox helpers = sidePanel("HELPERS"); helpers.setLayoutX(840); helpers.setLayoutY(145);
        for (HelperAdvisor.Kind kind : HelperAdvisor.Kind.values()) helpers.getChildren().add(helperButton(kind));
        overlay.getChildren().addAll(kernel, answers, lifelines, helpers, trialMeter);
        logicalRoot.getChildren().setAll(viewport, overlay); fadeIn(overlay);
    }


    /** Begins terminal-monochrome and gently restores colour as gates are cleared. */
    private void updateWorldColor() {
        int complete = 0;
        for (Gate gate : gates) if (gate.isCompleted()) complete++;
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-0.82 + Math.min(1, complete / (double) Math.max(1, questionBank.getTrialCount())) * 0.82);
        effect.setBrightness(-0.08 + complete * 0.008);
        backgroundPane.setEffect(effect);
    }

    private void answerQuestion(int answer) {
        Question question = trial.getCurrentQuestion();
        if (copyUsed && selectedAdvice != null) answer = selectedAdvice.getChoice();
        boolean correct = trial.answerWithSave(answer, saveArmed);
        boolean saveProtectedThisAnswer = saveArmed;
        saveArmed = false;
        if (correct) unlockedQuestionIds.add(question.getId());
        totalExp = Math.max(0, Math.min(maximumExp(), totalExp
            + (correct ? Constants.POINTS_PER_CORRECT_ANSWER
                : saveProtectedThisAnswer ? 0 : -Constants.POINTS_PER_CORRECT_ANSWER)));
        trialExp = Math.max(0, trialExp + (correct ? Constants.POINTS_PER_CORRECT_ANSWER
            : saveProtectedThisAnswer ? 0 : -Constants.POINTS_PER_CORRECT_ANSWER));
        saveProgress();
        refreshHud();
        Pane feedback = new Pane();
        Label playerBubble = speechBubble("PLAYER\n" + question.getChoices().get(answer), 17, 300);
        playerBubble.setLayoutX(170); playerBubble.setLayoutY(20);
        Label kernelBubble = speechBubble("KERNEL\n" + (correct ? "Correct. +" + Constants.POINTS_PER_CORRECT_ANSWER + " EXP." : saveProtectedThisAnswer ? "SAVE protected your EXP." : "Incorrect. EXP decreased.") + "\n" + question.getExplanation(), 17, 450);
        kernelBubble.setLayoutX(380); kernelBubble.setLayoutY(155);
        Button proceed = new Button("OK — CONTINUE");
        proceed.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: " + (correct ? "#3d9b61" : "#c94e4e") + ";");
        proceed.setLayoutX(450); proceed.setLayoutY(370);
        proceed.setOnAction(e -> { trial.nextQuestion(); if (trial.isFinished()) showResult(); else showQuestion(); });
        feedback.getChildren().addAll(playerBubble, kernelBubble, proceed);
        // Kernel response screens intentionally omit all HUD elements.
        logicalRoot.getChildren().setAll(viewport, feedback);
    }

    private void useHelper(HelperAdvisor.Kind kind) {
        if (selectedLifeline == null) {
            Label prompt = speechBubble(kind.name() + "\nWhat would you like me to do? Choose a lifeline first.", 18, 390);
            Button back = new Button("BACK TO QUESTION"); back.setOnAction(e -> showQuestion());
            VBox box = new VBox(15, prompt, back); box.setAlignment(Pos.TOP_CENTER); box.setLayoutY(20);
            logicalRoot.getChildren().setAll(viewport, box); return;
        }
        selectedAdvice = new HelperAdvisor(kind).advise(trial.getCurrentQuestion());
        Label advice = speechBubble(kind.name() + "\n" + selectedAdvice.getMessage() + "\nConfidence: " + selectedAdvice.getConfidence() + "%", 18, 390);
        Button back = new Button("BACK TO QUESTION"); back.setOnAction(e -> showQuestion());
        if (selectedLifeline == Lifeline.PEEK) peekUsed = true;
        if (selectedLifeline == Lifeline.COPY) copyUsed = true;
        if (selectedLifeline == Lifeline.SAVE) {
            saveUsed = true;
            saveArmed = true;
        }
        selectedLifeline = null;
        VBox box = new VBox(15, advice, back); box.setAlignment(Pos.TOP_CENTER); box.setLayoutY(18);
        logicalRoot.getChildren().setAll(viewport, box); fadeIn(box);
    }

    private void chooseLifeline(Lifeline lifeline) { selectedLifeline = lifeline; showQuestion(); }
    private void resetLifelines() { peekUsed = copyUsed = saveUsed = saveArmed = false; selectedAdvice = null; selectedLifeline = null; }

    private void showResult() {
        state = GameState.RESULT;
        int requiredExp = requiredExpForTrial(activeGate.getTrialNumber());
        boolean passed = trial.isComplete() && totalExp >= requiredExp;
        if (passed) {
            activeGate.setCompleted(true);
            player.setCheckpoint(activeGate.getLeftEdge() + 25);
            refreshWall();
            saveProgress();
        }
        int nextUncompletedIndex = gates.size();
        for (int index = 0; index < gates.size(); index++) {
            if (!gates.get(index).isCompleted()) { nextUncompletedIndex = index; break; }
        }
        double kernelDestination = nextUncompletedIndex < gates.size()
            ? gates.get(nextUncompletedIndex).getX() + 48
            : WORLD_WIDTH - Constants.TILE_SIZE * 5;
        changeKernelPosition(kernelDestination);
        if (passed && activeGate.getTrialNumber() == questionBank.getTrialCount()) {
            showVictory(); 
            return; 
        }
        if (passed) { 
            playKernelExit(); 
            return; 
        }
        Label result = text((passed ? "TRIAL COMPLETE" : "TRIAL FAILED") + "\n\nTOTAL EXP: "
            + totalExp + " / " + requiredExp + "\n\n"
            + (passed ? "Press ENTER to return to the map."
                      : "The Kernel returns you to the Trial " + trial.getNumber() + " checkpoint.\nPress ENTER to retry."), 25);
        result.setTextFill(passed ? Color.web("#8ee6a1") : Color.web("#ff9a9a"));
        logicalRoot.getChildren().setAll(viewport, result);
    }

    private void finishResult() {
        if (!trial.isComplete()) player.respawnAt(activeGate.getLeftEdge() - 30);
        showWorld();
    }

    /** Placeholder exit cutscene: the single Kernel moves right until walk frames are available. */
    private void playKernelExit() {
        state = GameState.RESULT;
        Label line = speechBubble("KERNEL\nTrial complete. I will meet you further in the system.", 19, 430);
        VBox overlay = new VBox(line); overlay.setAlignment(Pos.TOP_CENTER); overlay.setLayoutY(20);
        logicalRoot.getChildren().setAll(viewport, overlay);
        Timeline exit = new Timeline(new KeyFrame(Duration.millis(200), e -> kernel.setLayoutY(kernel.getLayoutY() + Constants.LOGICAL_HEIGHT)));
        exit.setCycleCount(20); exit.setOnFinished(e -> showWorld()); exit.play();
    }

    /** Moves the Kernel to the next uncleared gate or to the end of the map. */
    private void changeKernelPosition(double x) {
        kernel.setLayoutX(x);
        kernel.setLayoutY(Constants.GROUND_Y - Constants.TILE_SIZE * 5 + 20);
    }

    private void showVictory() {
        state = GameState.VICTORY;
        Rectangle flash = new Rectangle(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT, Color.WHITE);
        flash.setOpacity(0.0);
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        Label message = title("SYSTEM BOUNDARY BREACHED", 34);
        Label detail = text("The Kernel releases you into a world restored to color.\nAll paradigm trials completed.", 20);
        Button replay = new Button("PLAY AGAIN"); replay.setOnAction(e -> { for (Gate gate : gates) gate.setCompleted(false); totalExp = 0; player.setCheckpoint(Constants.TILE_SIZE * 3); player.respawnAt(Constants.TILE_SIZE * 3); refreshWall(); saveProgress(); showWorld(); });
        Button menu = new Button("RETURN TO MENU"); menu.setOnAction(e -> showMenu());
        box.getChildren().addAll(message, detail, replay, menu);
        logicalRoot.getChildren().setAll(viewport, box, flash, terminalHeader, expHud, trialHud);
        FadeTransition fade = new FadeTransition(Duration.seconds(.35), flash); fade.setFromValue(1); fade.setToValue(0); fade.play();
    }

    private void onKeyPressed(KeyEvent event) {
        KeyCode key = event.getCode();
        if (state == GameState.LOADING) {
            // Any key leaves the splash, but only once the hold has elapsed.
            if (splashReady) showMenu();
            return;
        }
        // F6 restarts gate progression and lifetime EXP while preserving unlocked archive entries.
        if (key == KeyCode.F6 && (state == GameState.MENU || state == GameState.WORLD)) {
            resetGateProgress();
            return;
        }
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
        } else if ((state == GameState.HOW_TO_PLAY || state == GameState.STORIES || state == GameState.CREDITS)
                && key == KeyCode.ESCAPE) {
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
        saveProgress();
    }

    private void completeAllGates() {
        for (Gate gate : gates) {
            gate.setCompleted(true);
        }
        player.clearWall();
        saveProgress();
    }

    /** Clears trial completion and score but deliberately keeps every earned question unlock. */
    private void resetGateProgress() {
        for (Gate gate : gates) gate.setCompleted(false);
        totalExp = 0;
        trialExp = 0;
        player.setCheckpoint(Constants.TILE_SIZE * 3);
        player.respawnAt(Constants.TILE_SIZE * 3);
        refreshWall();
        refreshHud();
        saveProgress();
    }

    /** Restores saved progression before the first menu/world scene is assembled. */
    private void loadProgress() {
        if (!Files.isRegularFile(savePath)) return;
        Properties saved = new Properties();
        try (InputStream input = Files.newInputStream(savePath)) {
            saved.load(input);
            totalExp = Math.max(0, Math.min(maximumExp(), Integer.parseInt(saved.getProperty("totalExp", "0"))));
            String unlocked = saved.getProperty("unlockedQuestionIds", "");
            for (String value : unlocked.split(",")) {
                try {
                    int id = Integer.parseInt(value.trim());
                    if (questionBank.getAllQuestions().stream().anyMatch(question -> question.getId() == id)) {
                        unlockedQuestionIds.add(id);
                    }
                } catch (NumberFormatException ignored) {
                    // Ignore a malformed ID without discarding other valid save data.
                }
            }
            double checkpoint = Constants.TILE_SIZE * 3;
            for (Gate gate : gates) {
                boolean completed = Boolean.parseBoolean(saved.getProperty("gate." + gate.getTrialNumber(), "false"));
                gate.setCompleted(completed);
                if (completed) checkpoint = gate.getLeftEdge() + 25;
            }
            player.setCheckpoint(checkpoint);
            player.respawnAt(checkpoint);
        } catch (IOException | IllegalArgumentException exception) {
            System.err.println("Could not load SYSTEMBOUND progress: " + exception.getMessage());
        }
    }

    /** Writes score, completed gates, and unlocked question IDs to a per-user save file. */
    private void saveProgress() {
        Properties saved = new Properties();
        saved.setProperty("totalExp", Integer.toString(totalExp));
        saved.setProperty("unlockedQuestionIds", unlockedQuestionIds.stream().sorted()
            .map(String::valueOf).collect(java.util.stream.Collectors.joining(",")));
        for (Gate gate : gates) {
            saved.setProperty("gate." + gate.getTrialNumber(), Boolean.toString(gate.isCompleted()));
        }
        try {
            Files.createDirectories(savePath.getParent());
            try (OutputStream output = Files.newOutputStream(savePath)) {
                saved.store(output, "SYSTEMBOUND progress");
            }
        } catch (IOException exception) {
            System.err.println("Could not save SYSTEMBOUND progress: " + exception.getMessage());
        }
    }

    /** Fits the fixed logical viewport into the window with centered letterboxing. */
    private void updateViewportScale() {
        if (root.getWidth() <= 0 || root.getHeight() <= 0) return;
        double scale = Math.min(root.getWidth() / Constants.LOGICAL_WIDTH,
            root.getHeight() / Constants.LOGICAL_HEIGHT);
        logicalRoot.setScaleX(scale);
        logicalRoot.setScaleY(scale);
    }

    private void configureParallaxLayer(ImageView layer, double opacity, double y) {
        // Assets are authored at 16000 x 576. Keep their native proportion and do not enlarge them.
        layer.setFitWidth(0); 
        layer.setFitHeight(0); 
        layer.setPreserveRatio(true);
        layer.setOpacity(opacity); 
        layer.setLayoutY(y);
    }

    private void configureHud() {
        String style = "-fx-font-family: 'Consolas'; -fx-text-fill: #e9f1ee; -fx-effect: dropshadow(gaussian, #101a22, 2, .9, 0, 1);";
        terminalHeader.setStyle(style + "-fx-font-size: 13;"); terminalHeader.setTranslateX(-350); terminalHeader.setTranslateY(-258);
        terminalHeader.setAlignment(Pos.CENTER_LEFT);
        terminalHeader.setMouseTransparent(true);
        expHud.setStyle(style + "-fx-font-size: 14;"); expHud.setTranslateX(-350); expHud.setTranslateY(-226);
        expHud.setAlignment(Pos.CENTER_LEFT);
        expHud.setMouseTransparent(true);
        trialHud.setStyle(style + "-fx-font-size: 15; -fx-font-weight: bold;"); trialHud.setTranslateY(252);
        refreshHud();
    }
    private VBox sidePanel(String heading) {
        VBox panel = new VBox(8); panel.setPrefWidth(165);
        panel.setStyle("-fx-background-color: rgba(255,255,255,.88); -fx-background-radius: 14; -fx-padding: 10; -fx-border-color: #596275; -fx-border-radius: 14;");
        Label title = new Label(heading); title.setStyle("-fx-font-weight: bold; -fx-text-fill: #17212b; -fx-font-size: 12;");
        panel.getChildren().add(title); return panel;
    }
    private Button helperButton(HelperAdvisor.Kind kind) {
        ImageView icon = new ImageView(new Image(GameApplication.class.getResource("/Helpers/" + helperFile(kind) + ".png").toExternalForm()));
        icon.setFitWidth(30); icon.setFitHeight(30); icon.setPreserveRatio(true);
        Button button = sideButton(kind.name(), icon); button.setOnAction(e -> useHelper(kind)); return button;
    }
    private static String helperFile(HelperAdvisor.Kind kind) {
        String name = kind.name().toLowerCase(); return Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }
    private Button sideButton(String label, ImageView icon) {
        Button button = new Button(label, icon); button.setContentDisplay(javafx.scene.control.ContentDisplay.LEFT);
        button.setPrefWidth(145); button.setMinHeight(35); button.setWrapText(true);
        return button;
    }
    private Label speechBubble(String value, int fontSize, double width) {
        Label bubble = new Label(value); bubble.setWrapText(true); bubble.setPrefWidth(width); bubble.setMaxWidth(width);
        bubble.setStyle("-fx-font-size: " + fontSize + "; -fx-text-fill: #111820; -fx-background-color: rgba(255,255,255,.95); "
            + "-fx-background-radius: 18; -fx-padding: 12 16 12 16; -fx-border-color: #8a95a5; -fx-border-radius: 18;");
        return bubble;
    }
    private Label controlsHud() {
        Label label = new Label("A / D or ← / → : Move\nSPACE : Jump\nE : Interact");
        label.setStyle("-fx-font-size: 12; -fx-text-fill: #e9f1ee; -fx-effect: dropshadow(gaussian, #101a22, 2, .9, 0, 1);");
        label.setTranslateX(-410); label.setTranslateY(220); return label;
    }
    private void refreshHud() {
        terminalHeader.setText("PS D:\\SystemBound\\ParadigmTrials> " + (cursorVisible ? "▮" : " "));
        int maximum = maximumExp();
        int filled = maximum == 0 ? 0 : (int) Math.round(12.0 * totalExp / maximum);
        StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 12; i++) bar.append(i < filled ? '▮' : '▯');
        int current = activeGate == null ? 1 : activeGate.getTrialNumber();
        for (Gate gate : gates) {
            if (!gate.isCompleted()) { current = gate.getTrialNumber(); break; }
        }
        expHud.setText("EXP " + totalExp + " / " + maximum + "  [" + bar + "]");
        trialHud.setText(current == questionBank.getTrialCount() ? "CORE TRIAL" : "TRIAL " + current);
    }

    /** Maximum score assumes every bank question is answered correctly exactly once. */
    private int maximumExp() {
        return questionBank.getTotalQuestions() * Constants.POINTS_PER_CORRECT_ANSWER;
    }

    /** Each successive trial requires another 600 points in the cumulative score. */
    private int requiredExpForTrial(int trialNumber) {
        return trialNumber * Constants.POINTS_PER_TRIAL;
    }

    /** Sets up the compact per trial meter and refreshes its requirement and fill. */
    private void configureTrialMeter() {
        int target = Constants.POINTS_PER_TRIAL;
        trialExpLabel.setText("TRIAL EXP  " + trialExp + " / " + target);
        trialExpLabel.setStyle("-fx-text-fill: #e9f1ee; -fx-font-weight: bold; -fx-font-size: 13;");
        trialExpBar.setPrefWidth(260);
        trialExpBar.setStyle("-fx-accent: #63d7e8;");
        double progressTarget = Math.min(1.0, trialExp / (double) target);
        Timeline fill = new Timeline(new KeyFrame(Duration.millis(420),
            new KeyValue(trialExpBar.progressProperty(), progressTarget, Interpolator.EASE_BOTH)));
        fill.play();
    }

    private static void fadeIn(javafx.scene.Node node) { FadeTransition transition = new FadeTransition(Duration.millis(180), node); transition.setFromValue(0); transition.setToValue(1); transition.play(); }

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
