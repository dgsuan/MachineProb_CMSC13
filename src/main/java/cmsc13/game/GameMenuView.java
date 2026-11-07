package cmsc13.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Builds menu, help, story, and credits screens separately from gameplay
 * control.
 */
final class GameMenuView {

    private static final Font TITLE_FONT = loadTitleFont();

    /**
     * Creates the title menu over the live world backdrop.
     */
    Node mainMenu(Node backdrop, Runnable onPlay, Runnable onHowToPlay,
            Runnable onStories, Runnable onCredits, Runnable onQuestionBank,
            Runnable onExit) {
        Label title = new Label("SYSTEMBOUND");
        title.setFont(TITLE_FONT);
        // Explicitly restate the loaded family because the global label rule uses
        // Consolas.
        title.setStyle("-fx-font-family: '" + TITLE_FONT.getFamily() + "'; -fx-font-size: 54; -fx-text-fill: #e9fbff;");
        title.setEffect(new DropShadow(24, Color.web("#66dfff")));
        Label subtitle = new Label("The Paradigm Trials");
        subtitle.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18; -fx-text-fill: #d1e7ef;");
        VBox menuItems = new VBox(13, title, subtitle,
                button("PLAY / CONTINUE", onPlay), button("STORY", onStories), button("HOW TO PLAY", onHowToPlay),
                button("CREDITS", onCredits), button("EXIT", onExit));
        menuItems.setAlignment(Pos.CENTER);
        // The viewport is only a visual backdrop. Let pointer events pass through to
        // menu controls.
        backdrop.setMouseTransparent(true);
        StackPane menu = new StackPane(backdrop, menuItems);
        Button bank = button("REVIEW TOPICS", onQuestionBank);
        HBox archiveLinks = new HBox(10, bank);
        archiveLinks.setMouseTransparent(false);
        archiveLinks.setPickOnBounds(false);
        StackPane.setAlignment(archiveLinks, Pos.BOTTOM_RIGHT);
        archiveLinks.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setMargin(archiveLinks, new Insets(0, 20, 20, 0));
        menu.getChildren().add(archiveLinks);
        return menu;
    }

    /** Reuses the menu callback with one instructional section per page. */
    Node howToPlay(Runnable onBack) {
        VBox body = new VBox(14);
        Label counter = paragraph("");
        javafx.scene.control.ScrollPane scroll = scroll(body);
        Button previous = button("PREVIOUS", () -> {
        });
        Button next = button("NEXT", () -> {
        });
        int[] index = { 0 };
        Runnable render = () -> {
            body.getChildren().setAll(tutorial(index[0]));
            counter.setText("FIELD GUIDE   /   " + (index[0] + 1) + " / 5");
            previous.setDisable(index[0] == 0);
            next.setText(index[0] == 4 ? "FINISH" : "NEXT");
            scroll.setVvalue(0);
        };
        previous.setOnAction(e -> {
            index[0]--;
            render.run();
        });
        next.setOnAction(e -> {
            if (index[0] == 4)
                onBack.run();
            else {
                index[0]++;
                render.run();
            }
        });
        render.run();
        HBox navigation = new HBox(18, previous, next);
        navigation.setAlignment(Pos.CENTER);
        return page("HOW TO PLAY", new VBox(10, counter, scroll), navigation, onBack);
    }

    private Node tutorial(int index) {
        VBox body = new VBox(14);
        switch (index) {
            case 0:
                body.getChildren().addAll(heading("MOVEMENT & EXPLORATION"),
                        new HBox(14, key("A / LEFT", "Move left"), key("D / RIGHT", "Move right"), key("SPACE", "Jump"),
                                key("E", "Interact")),
                        new HBox(24, image("/characters/player/jump/jump1.png", 70),
                                image("/Gate/incompleted1.png", 90)),
                        paragraph(
                                "Navigate platforms, avoid pitfalls, and approach a minigate. Press E nearby to start its trial."),
                        paragraph("ESC returns to the menu. F7 mutes or restores music and effects."));
                break;
            case 1:
                body.getChildren().addAll(heading("THE PARADIGM TRIALS"),
                        paragraph(
                                "Seven topic trials lead to an eighth, comprehensive Core trial. The Kernel presents questions; select one of four answers."),
                        paragraph(
                                "1  Introduction to Programming Paradigms\n2  Procedural Programming\n3  Functional Programming\n4  Object-Oriented Programming\n5  Imperative vs. Declarative Programming\n6  Event-Driven Programming\n7  Component Mappings between Programming Paradigms"),
                        new HBox(20, key("+" + Constants.POINTS_PER_CORRECT_ANSWER + " EXP", "Correct answer"),
                                key("-" + Constants.POINTS_PER_CORRECT_ANSWER + " EXP", "Incorrect; floor is zero")),
                        paragraph("Pass with cumulative EXP of at least trial number x " + Constants.POINTS_PER_TRIAL
                                + " and at least " + Trial.REQUIRED_CORRECT_ANSWERS
                                + " net correct answers this attempt. Unprotected mistakes reduce the net count. Trial 1 requires 600 EXP; the Core requires 4800. Retry if either condition is unmet."));
                break;
            case 2:
                body.getChildren().add(heading("MEET YOUR HELPERS"));
                String[] names = { "Genius", "Bookworm", "Hacker", "Encourager", "Trickster" };
                String[] details = { "Analytical reasoning, patterns and logic; a strong all-round thinker.",
                        "Definitions and principles; strongest with theory.",
                        "Practical execution flow and code; strongest with programming.",
                        "Optimistic support and intuitive suggestions when you feel uncertain.",
                        "Unpredictable suggestions that can mislead. Think carefully before trusting them." };
                for (int i = 0; i < names.length; i++) {
                    Label description = paragraph(names[i].toUpperCase() + " - " + details[i]);
                    HBox row = new HBox(16, image("/Helpers/" + names[i] + ".png", 48), description);
                    row.setAlignment(Pos.CENTER_LEFT);
                    body.getChildren().add(row);
                }
                body.getChildren().add(paragraph("No helper is guaranteed to be right. Evaluate the advice yourself."));
                break;
            case 3:
                body.getChildren().addAll(heading("UNDERSTANDING THE LIFELINES"),
                        paragraph(
                                "Choose a lifeline, then select a helper. Return to the question to submit an answer."),
                        lifeline("PEEK",
                                "See a helper's suggestion. You still choose the answer; nothing is locked in."),
                        lifeline("COPY",
                                "After reading the advice, click an answer to submit. The helper's suggested choice overrides your selection. Normal EXP rewards and penalties apply."),
                        lifeline("SAVE",
                                "After reading the advice, submit your own answer. A correct answer earns EXP; an incorrect answer loses no EXP or net correct count."),
                        paragraph(
                                "With SAVE, you make the final selection with protection from a wrong answer. With COPY, your submission uses the helper's choice."));
                break;
            default:
                body.getChildren().addAll(heading("CHECKPOINTS & THE CORE GATE"), progression(),
                        paragraph(
                                "Pitfalls return you to your checkpoint. Clearing a trial updates the checkpoint and opens the next stretch of the system."),
                        paragraph(
                                "A failed trial costs one of five lives. RETRY TRIAL returns to the map at or near that minigate; press E for a fresh attempt. When all lives are spent, the previous trial onward is relocked, EXP is adjusted, and five lives are restored."),
                        paragraph(
                                "The world gradually gains color as gates are cleared. Pass the seven topic trials and the final Core trial to escape. The ending lets you view credits, return to the menu, or reenter to explore and retake trials."));
        }
        return body;
    }

    Node story(Runnable onBack) {
        VBox record = new VBox(18,
                paragraph("ARCHIVE SB-001 / RECOVERED MEMORY\nTIMESTAMP: UNKNOWN / SIGNAL: UNSTABLE"),
                heading("SYSTEMBOUND"), paragraph("THE PARADIGM TRIALS"));
        String[] story = {
                "You wake up in a strange digital world, surrounded by endless platforms, unfamiliar structures, and fragments of a system you do not recognize.",
                "At first, everything appears monochromatic, as if the world has not yet finished loading. A mysterious Kernel appears and explains that you are trapped inside a system that is slowly losing its connection to reality.",
                "To escape, you must travel across the system and overcome seven Paradigm Trials. Each trial represents a different programming concept, testing your understanding of how programs work, how they are structured, and how different programming paradigms approach problems.",
                "Along the way, you will encounter five unusual helpers. Each has a different way of thinking and may offer suggestions when you are unsure of an answer. However, their advice is not always guaranteed to be correct. The final decision is yours.",
                "As you complete the trials, the world gradually regains its color. What initially appeared to be a broken and lifeless system slowly becomes something more familiar.",
                "Your ultimate destination is the Core Gate, the final gateway separating the digital world from reality. One comprehensive trial awaits at the boundary.",
                "Pass the trials, reach the Core Gate, and find your way back.",
                "Every system has a story. Every problem has a solution. Your journey begins here."
        };
        for (String text : story)
            record.getChildren().add(paragraph(text));
        record.getStyleClass().add("archive-record");
        return page("STORY / SYSTEM ARCHIVE", scroll(record), null, onBack);
    }

    Node credits(Runnable onBack) {
        VBox entries = new VBox(22, heading("SYSTEMBOUND: THE PARADIGM TRIALS"),
                paragraph("Developed as a Machine Problem for CMSC 13 - Survey of Programming Paradigms."),
                heading("PROGRAMMING & DEVELOPMENT"));
        HBox developers = new HBox(20,
                credit("Benjamin A. Deypalan",
                        "Co-Programmer\nArt and Visual Design\nUser Interface Design\nCharacter and Environment Artwork\nVisual Presentation and Game Aesthetics"),
                credit("Dave Javenn Suan",
                        "Co-Programmer\nMusic Composition and Production\nBackground Music\nAudio Design and Integration"));
        entries.getChildren().addAll(developers, heading("SPECIAL THANKS"),
                paragraph(
                        "To our instructor and the Department of Computer Science and Mathematics for their guidance throughout the course."),
                paragraph(
                        "To our classmates, friends, and testers who provided feedback and helped us improve the game."),
                heading("PROJECT INFORMATION"),
                paragraph(
                        "Course: CMSC 13 - Survey of Programming Paradigms\nInstitution: University of the Philippines Tacloban College\nCollege: College of Science\nAcademic Year: 2026\u20132027"),
                heading("FINAL MESSAGE"),
                paragraph(
                        "Every system has a story.\nEvery problem has a solution.\nAnd every programmer starts somewhere."),
                heading("THANK YOU FOR PLAYING!"), paragraph("END OF TRANSMISSION."));
        return page("CREDITS", scroll(entries), null, onBack);
    }

    private static Node credit(String name, String roles) {
        VBox card = new VBox(14, paragraph(name), paragraph(roles));
        card.getStyleClass().add("credit-card");
        card.setPrefWidth(440);
        HBox.setHgrow(card, javafx.scene.layout.Priority.ALWAYS);
        return card;
    }

    private static Node key(String text, String action) {
        Label cap = paragraph(text);
        cap.getStyleClass().add("keycap");
        return new VBox(8, cap, paragraph(action));
    }

    private static Node lifeline(String name, String text) {
        Button sample = button(name, () -> {
        });
        sample.setFocusTraversable(false);
        sample.setMouseTransparent(true);
        return new HBox(18, sample, paragraph(text));
    }

    private static javafx.scene.image.ImageView image(String path, double size) {
        javafx.scene.image.ImageView view = new javafx.scene.image.ImageView(
                new javafx.scene.image.Image(GameMenuView.class.getResource(path).toExternalForm()));
        view.setFitWidth(size);
        view.setFitHeight(size);
        view.setPreserveRatio(true);
        return view;
    }

    private static Node progression() {
        HBox strip = new HBox(20);
        String[] paths = {
            "/Preview/loading.png",
            "/Preview/recovering.png",
            "/Preview/restored.png"
        };
        for (int i = 0; i < 3; i++) {
            javafx.scene.image.ImageView view = image(paths[i], 250);
            view.setPreserveRatio(true);
            view.setFitWidth(250);
            view.setFitHeight(140);
            StackPane landscape = new StackPane(view);
            landscape.setPrefSize(250, 140);
            strip.getChildren()
                    .add(new VBox(6, landscape, paragraph(new String[] { "LOADING", "RECOVERING", "RESTORED" }[i])));
        }
        return strip;
    }

    private static javafx.scene.control.ScrollPane scroll(Node content) {
        javafx.scene.control.ScrollPane scroll = new javafx.scene.control.ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(javafx.scene.control.ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setMinHeight(0);
        scroll.getStyleClass().add("menu-scroll");
        VBox.setVgrow(scroll, javafx.scene.layout.Priority.ALWAYS);
        return scroll;
    }

    private static Node page(String title, Node content, Node navigation, Runnable onBack) {
        VBox page = new VBox(12, heading(title), content);
        VBox.setVgrow(content, javafx.scene.layout.Priority.ALWAYS);
        if (navigation != null)
            page.getChildren().add(navigation);
        page.getChildren().add(button("BACK TO MENU", onBack));
        page.setAlignment(Pos.CENTER);
        page.setPadding(new Insets(22, 40, 20, 40));
        page.getStyleClass().add("information-page");
        javafx.animation.FadeTransition fade = new javafx.animation.FadeTransition(javafx.util.Duration.millis(350),
                content);
        fade.setFromValue(0);
        fade.setToValue(1);
        page.sceneProperty().addListener((obs, oldScene, scene) -> {
            if (scene == null) {
                fade.stop();
                content.setOpacity(1);
            } else
                fade.playFromStart();
        });
        return page;
    }

    private static Label paragraph(String text) {
        Label label = new Label(text);
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.getStyleClass().add("information-text");
        return label;
    }

    /**
     * Creates a shared asset-skinned menu action.
     */
    private static Button button(String label, Runnable action) {
        Button button = new Button(label);
        button.setMinWidth(245);
        button.setMinHeight(44);
        button.setOnAction(event -> action.run());
        return button;
    }

    /**
     * Styles a screen heading with the shared interface font.
     */
    private static Label heading(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #e9fbff;");
        label.setEffect(new DropShadow(12, Color.web("#2089a4")));
        return label;
    }

    /**
     * Loads the supplied display font, falling back safely if the resource is
     * unavailable.
     */
    private static Font loadTitleFont() {
        try {
            return Font.loadFont(GameMenuView.class.getResourceAsStream("/Fonts/Title_font.ttf"), 54);
        } catch (RuntimeException exception) {
            return Font.font("Consolas", 54);
        }
    }
}
