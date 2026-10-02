package cmsc13.game;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/** Builds menu, help, story, and credits screens separately from gameplay control. */
final class GameMenuView {
    private static final Font TITLE_FONT = loadTitleFont();

    /** Creates the title menu over the live world backdrop. */
    Node mainMenu(Node backdrop, Runnable onPlay, Runnable onHowToPlay,
                  Runnable onStories, Runnable onCredits, Runnable onQuestionBank,
                  Runnable onExit) {
        Label title = new Label("SYSTEMBOUND");
        title.setFont(TITLE_FONT);
        // Explicitly restate the loaded family because the global label rule uses Consolas.
        title.setStyle("-fx-font-family: '" + TITLE_FONT.getFamily() + "'; -fx-font-size: 54; -fx-text-fill: #e9fbff;");
        title.setEffect(new DropShadow(24, Color.web("#66dfff")));
        Label subtitle = new Label("The Paradigm Trials");
        subtitle.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 18; -fx-text-fill: #d1e7ef;");
        VBox menuItems = new VBox(13, title, subtitle,
            button("PLAY", onPlay), button("HOW TO PLAY", onHowToPlay),
            button("CREDITS", onCredits), button("EXIT", onExit));
        menuItems.setAlignment(Pos.CENTER);
        // The viewport is only a visual backdrop. Let pointer events pass through to menu controls.
        backdrop.setMouseTransparent(true);
        StackPane menu = new StackPane(backdrop, menuItems);
        Button bank = button("QUESTION BANK", onQuestionBank);
        Button stories = button("STORIES", onStories);
        HBox archiveLinks = new HBox(10, stories, bank);
        archiveLinks.setMouseTransparent(false);
        archiveLinks.setPickOnBounds(false);
        StackPane.setAlignment(archiveLinks, Pos.BOTTOM_RIGHT);
        archiveLinks.setAlignment(Pos.BOTTOM_RIGHT);
        StackPane.setMargin(archiveLinks, new Insets(0, 20, 20, 0));
        menu.getChildren().add(archiveLinks);
        return menu;
    }

    /** Creates the illustrated controls/help screen. */
    Node howToPlay(Runnable onBack) {
        Label heading = heading("HOW TO PLAY");
        Label controls = new Label("MOVE\nA / D or ← / →\n\nJUMP\nSPACE\n\nTRIAL GATES\nWalk close to a gate, then press E.\nAnswer questions to earn EXP and unlock the next gate.\n\nPITFALLS\nA fall sends you back to your last checkpoint.");
        controls.setWrapText(true);
        controls.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 17; -fx-text-fill: #e4eff4; -fx-text-alignment: center; -fx-line-spacing: 5;");
        VBox card = new VBox(22, heading, controls, button("BACK TO MENU", onBack));
        card.setAlignment(Pos.CENTER);
        card.setMaxWidth(760);
        card.setPadding(new Insets(32));
        card.setStyle("-fx-background-color: rgba(13, 25, 38, .88); -fx-background-radius: 20; -fx-border-color: #61d5e8; -fx-border-radius: 20;");
        return new StackPane(card);
    }

    /** Creates an intentionally empty placeholder page for future story or credits content. */
    Node placeholder(String screenTitle, Runnable onBack) {
        VBox content = new VBox(24, heading(screenTitle),
            button("BACK TO MENU", onBack));
        content.setAlignment(Pos.CENTER);
        content.setPrefSize(Constants.LOGICAL_WIDTH, Constants.LOGICAL_HEIGHT);
        content.setStyle("-fx-background-color: linear-gradient(to bottom, #132333, #09121e);");
        return content;
    }

    /** Creates a shared asset-skinned menu action. */
    private static Button button(String label, Runnable action) {
        Button button = new Button(label);
        button.setMinWidth(245);
        button.setMinHeight(44);
        button.setOnAction(event -> action.run());
        return button;
    }

    /** Styles a screen heading with the shared interface font. */
    private static Label heading(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 30; -fx-font-weight: bold; -fx-text-fill: #e9fbff;");
        label.setEffect(new DropShadow(12, Color.web("#2089a4")));
        return label;
    }

    /** Loads the supplied display font, falling back safely if the resource is unavailable. */
    private static Font loadTitleFont() {
        try {
            return Font.loadFont(GameMenuView.class.getResourceAsStream("/Fonts/Title_font.ttf"), 54);
        } catch (RuntimeException exception) {
            return Font.font("Consolas", 54);
        }
    }
}
