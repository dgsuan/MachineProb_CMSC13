package cmsc13.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
    private final List<Gate> gates = createGates();
    private final List<LeafSprite> leaves = createLeaves();
    private final QuestionBank questionBank = new QuestionBank();
    private static final Image[] LEAF_FRAMES = loadLeafFrames();
    private final ImageView skyImage = createBackgroundImage("/BackGround/Map_sky-copy.png");
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
    private boolean cursorVisible = true;
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
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);
        stage.setScene(scene);
        root.widthProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        root.heightProperty().addListener((observable, oldValue, newValue) -> updateViewportScale());
        showMenu();
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
        skyImage.setLayoutY(-200);

        configureParallaxLayer(mountainLayerFar, 0.62, 40);
        mountainLayerFar.setLayoutX(0);
        mountainLayerFar.setLayoutY(0);

        configureParallaxLayer(mountainLayerNear, 0.74, 60);
        mountainLayerNear.setLayoutX(0);
        mountainLayerNear.setLayoutY(0);

        configureParallaxLayer(cloudLayerFar, 0.60, 0);
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
        configureHud();
        logicalRoot.getChildren().setAll(viewport, prompt, terminalHeader, expHud, trialHud, controlsHud());
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
        if (near == null) {
            prompt.setText("");
        } else if (near.isCompleted()) {
            prompt.setText("TRIAL " + near.getTrialNumber() + " COMPLETE");
        } else {
            prompt.setText("[ Press E to begin " + (near.getTrialNumber() == Constants.TRIAL_COUNT ? "the Core Trial" : "Trial " + near.getTrialNumber()) + " ]");
        }
    }

    private void updateBackgroundParallax(double cameraX) {
        double mountainOffset = cameraX * Constants.MOUNTAIN_SCROLL_SPEED;
        double cloudOffset = cameraX * Constants.CLOUD_SCROLL_SPEED;
        double skyOffset = cameraX * Constants.SKY_SCROLL_SPEED;

        mountainLayerFar.setLayoutX(-mountainOffset);
        mountainLayerNear.setLayoutX(-mountainOffset * 1.5);
        cloudLayerFar.setLayoutX(-cloudOffset);
        cloudLayerNear.setLayoutX(-cloudOffset * 1.5);
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

    private static ImageView createKernel() {
        Image image = new Image(GameApplication.class.getResource("/Kernel.png").toExternalForm());
        ImageView result = new ImageView(image);
        result.setFitWidth(Constants.TILE_SIZE * 5); result.setFitHeight(Constants.TILE_SIZE * 5);
        result.setPreserveRatio(true); result.setSmooth(false);
        result.setLayoutY(Constants.GROUND_Y - Constants.TILE_SIZE * 5);
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
        kernel.setLayoutX(gate.getX() + Constants.TILE_SIZE * 2);
        state = GameState.TRIAL_CONFIRM;
        Label message = text("KERNEL:\n\"You've reached Trial " + gate.getTrialNumber()
            + ". Do you wish to challenge the system?\"", 23);
        Button begin = new Button("BEGIN TRIAL"); begin.setOnAction(e -> { state = GameState.TRIAL; trial = new Trial(questionBank, gate.getTrialNumber()); resetLifelines(); showQuestion(); });
        Button leave = new Button("NOT YET"); leave.setOnAction(e -> showWorld());
        VBox box = new VBox(16, message, new HBox(14, begin, leave)); box.setAlignment(Pos.CENTER);
        logicalRoot.getChildren().setAll(viewport, box);
    }

    private void showQuestion() {
        Question question = trial.getCurrentQuestion();
        Pane overlay = new Pane();
        Label kernel = speechBubble("KERNEL\n" + question.getQuestionText(), 20, 460);
        kernel.setLayoutX(282); kernel.setLayoutY(18);
        GridPane answers = new GridPane(); answers.setHgap(16); answers.setVgap(14);
        for (int i = 0; i < 4; i++) {
            int answer = i;
            Button button = new Button((char) ('A' + i) + ". " + question.getChoices().get(i));
            button.setPrefWidth(295); button.setPrefHeight(52); button.setWrapText(true);
            button.setOnAction(e -> answerQuestion(answer)); answers.add(button, i % 2, i / 2);
        }
        answers.setLayoutX(202); answers.setLayoutY(405);
        VBox lifelines = sidePanel("LIFELINES"); lifelines.setLayoutX(18); lifelines.setLayoutY(175);
        Button peek = sideButton(peekUsed ? "PEEK USED" : "PEEK", null); peek.setDisable(peekUsed); peek.setOnAction(e -> chooseLifeline(Lifeline.PEEK));
        Button copy = sideButton(copyUsed ? "COPY USED" : "COPY", null); copy.setDisable(copyUsed); copy.setOnAction(e -> chooseLifeline(Lifeline.COPY));
        Button save = sideButton(saveUsed ? "SAVE USED" : "SAVE", null); save.setDisable(saveUsed); save.setOnAction(e -> chooseLifeline(Lifeline.SAVE));
        lifelines.getChildren().addAll(peek, copy, save);
        VBox helpers = sidePanel("HELPERS"); helpers.setLayoutX(840); helpers.setLayoutY(145);
        for (HelperAdvisor.Kind kind : HelperAdvisor.Kind.values()) helpers.getChildren().add(helperButton(kind));
        overlay.getChildren().addAll(kernel, answers, lifelines, helpers);
        logicalRoot.getChildren().setAll(viewport, overlay); fadeIn(overlay);
    }

    private void legacyShowQuestion() {
        Question question = trial.getCurrentQuestion();
        VBox box = new VBox(14);
        box.setAlignment(Pos.CENTER); box.setMaxWidth(880);
        Label exp = text("TRIAL " + trial.getNumber() + "  •  Question " + trial.getQuestionNumber() + "/14    EXP: "
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
        HBox helpers = new HBox(8); helpers.setAlignment(Pos.CENTER);
        for (HelperAdvisor.Kind kind : HelperAdvisor.Kind.values()) {
            Button helper = new Button(kind.name()); helper.setOnAction(e -> useHelper(kind)); helpers.getChildren().add(helper);
        }
        HBox lifelines = new HBox(10); lifelines.setAlignment(Pos.CENTER);
        Button peek = new Button(peekUsed ? "PEEK USED" : "PEEK"); peek.setDisable(peekUsed); peek.setOnAction(e -> chooseLifeline(Lifeline.PEEK));
        Button copy = new Button(copyUsed ? "COPY USED" : "COPY"); copy.setDisable(copyUsed); copy.setOnAction(e -> chooseLifeline(Lifeline.COPY));
        Button save = new Button(saveUsed ? "SAVE USED" : "SAVE"); save.setDisable(saveUsed); save.setOnAction(e -> chooseLifeline(Lifeline.SAVE));
        lifelines.getChildren().addAll(peek, copy, save);
        box.getChildren().addAll(title(trial.getNumber() == Constants.TRIAL_COUNT ? "CORE GATE" : "TRIAL " + trial.getNumber(), 28), exp, kernel, answers, helpers, lifelines);
        logicalRoot.getChildren().setAll(viewport, box);
        fadeIn(box);
    }

    /** Begins terminal-monochrome and gently restores colour as gates are cleared. */
    private void updateWorldColor() {
        int complete = 0;
        for (Gate gate : gates) if (gate.isCompleted()) complete++;
        ColorAdjust effect = new ColorAdjust();
        effect.setSaturation(-0.82 + Math.min(1, complete / (double) Constants.TRIAL_COUNT) * 0.82);
        effect.setBrightness(-0.08 + complete * 0.008);
        backgroundPane.setEffect(effect);
    }

    private void answerQuestion(int answer) {
        Question question = trial.getCurrentQuestion();
        if (copyUsed && selectedAdvice != null) answer = selectedAdvice.getChoice();
        boolean correct = trial.answerWithSave(answer, saveArmed);
        totalExp = Math.max(0, totalExp + (correct ? 100 : saveArmed ? 0 : -100));
        refreshHud();
        Label playerLine = text("PLAYER:\n\"" + question.getChoices().get(answer) + "\"\n\n"
            + "PLAYER: \"" + (correct ? "Yes!" : "Oh no...") + "\"\n\n"
            + "KERNEL: \"" + (correct ? "Correct. +1 EXP." : saveArmed ? "SAVE protected your EXP." : "Incorrect. EXP decreased.") + "\"\n\n"
            + question.getExplanation(), 19);
        Pane feedback = new Pane();
        Label playerBubble = speechBubble("PLAYER\n" + question.getChoices().get(answer), 17, 300);
        playerBubble.setLayoutX(170); playerBubble.setLayoutY(20);
        Label kernelBubble = speechBubble("KERNEL\n" + (correct ? "Correct. +1 EXP." : saveArmed ? "SAVE protected your EXP." : "Incorrect. EXP decreased.") + "\n" + question.getExplanation(), 17, 450);
        kernelBubble.setLayoutX(380); kernelBubble.setLayoutY(155);
        Button proceed = new Button("OK — CONTINUE");
        proceed.setStyle("-fx-font-weight: bold; -fx-text-fill: white; -fx-background-color: " + (correct ? "#3d9b61" : "#c94e4e") + ";");
        proceed.setLayoutX(450); proceed.setLayoutY(370);
        proceed.setOnAction(e -> { trial.nextQuestion(); if (trial.isFinished()) showResult(); else showQuestion(); });
        feedback.getChildren().addAll(playerBubble, kernelBubble, proceed);
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
        if (selectedLifeline == Lifeline.SAVE) { saveUsed = true; saveArmed = true; }
        selectedLifeline = null;
        VBox box = new VBox(15, advice, back); box.setAlignment(Pos.TOP_CENTER); box.setLayoutY(18);
        logicalRoot.getChildren().setAll(viewport, box); fadeIn(box);
    }

    private void chooseLifeline(Lifeline lifeline) { selectedLifeline = lifeline; showQuestion(); }
    private void resetLifelines() { peekUsed = copyUsed = saveUsed = saveArmed = false; selectedAdvice = null; selectedLifeline = null; }

    private void showResult() {
        state = GameState.RESULT;
        boolean passed = trial.isComplete();
        if (passed) {
            activeGate.setCompleted(true);
            player.setCheckpoint(activeGate.getLeftEdge() + 25);
            refreshWall();
        }
        if (passed && activeGate.getTrialNumber() == Constants.TRIAL_COUNT) { showVictory(); return; }
        if (passed) { playKernelExit(); return; }
        Label result = text((passed ? "TRIAL COMPLETE" : "TRIAL FAILED") + "\n\nTRIAL EXP: "
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

    /** Placeholder exit cutscene: the single Kernel moves right until walk frames are available. */
    private void playKernelExit() {
        state = GameState.RESULT;
        Label line = speechBubble("KERNEL\nTrial complete. I will meet you further in the system.", 19, 430);
        VBox overlay = new VBox(line); overlay.setAlignment(Pos.TOP_CENTER); overlay.setLayoutY(20);
        logicalRoot.getChildren().setAll(viewport, overlay);
        Timeline exit = new Timeline(new KeyFrame(Duration.millis(45), e -> kernel.setLayoutY(kernel.getLayoutY() + Constants.LOGICAL_HEIGHT)));
        exit.setCycleCount(20); exit.setOnFinished(e -> showWorld()); exit.play();
    }

    private void changeKernelPosition(double x) {
        kernel.setLayoutX(x);
    }

    private void showVictory() {
        state = GameState.VICTORY;
        Rectangle flash = new Rectangle(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT, Color.WHITE);
        flash.setOpacity(0.0);
        VBox box = new VBox(16);
        box.setAlignment(Pos.CENTER);
        Label message = title("SYSTEM BOUNDARY BREACHED", 34);
        Label detail = text("The Kernel releases you into a world restored to color.\nAll paradigm trials completed.", 20);
        Button replay = new Button("PLAY AGAIN"); replay.setOnAction(e -> { for (Gate gate : gates) gate.setCompleted(false); totalExp = 0; player.respawnAt(Constants.TILE_SIZE * 3); refreshWall(); showWorld(); });
        Button menu = new Button("RETURN TO MENU"); menu.setOnAction(e -> showMenu());
        box.getChildren().addAll(message, detail, replay, menu);
        logicalRoot.getChildren().setAll(viewport, box, flash);
        FadeTransition fade = new FadeTransition(Duration.seconds(.35), flash); fade.setFromValue(1); fade.setToValue(0); fade.play();
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

    private void configureParallaxLayer(ImageView layer, double opacity, double y) {
        // Assets are authored at 16000 x 576. Keep their native proportion and do not enlarge them.
        layer.setFitWidth(Constants.WORLD_WIDTH); 
        layer.setFitHeight(0); 
        layer.setPreserveRatio(false);
        layer.setOpacity(opacity); 
        layer.setLayoutY(y);
    }

    private void configureHud() {
        String style = "-fx-font-family: 'Consolas'; -fx-text-fill: #e9f1ee; -fx-effect: dropshadow(gaussian, #101a22, 2, .9, 0, 1);";
        terminalHeader.setStyle(style + "-fx-font-size: 13;"); terminalHeader.setTranslateX(-350); terminalHeader.setTranslateY(-258);
        expHud.setStyle(style + "-fx-font-size: 14;"); expHud.setTranslateX(-362); expHud.setTranslateY(-226);
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
        int filled = Math.min(12, totalExp / 100); StringBuilder bar = new StringBuilder();
        for (int i = 0; i < 12; i++) bar.append(i < filled ? '█' : '░');
        expHud.setText("EXP " + totalExp + " / 1200  [" + bar + "]");
        int current = activeGate == null ? 1 : activeGate.getTrialNumber();
        trialHud.setText(current == Constants.TRIAL_COUNT ? "CORE TRIAL" : "TRIAL " + current);
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
