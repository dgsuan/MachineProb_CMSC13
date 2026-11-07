package cmsc13.game;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

/**
 * Exercises actual JavaFX button callbacks and layout without opening a window.
 */
class MenuViewSmokeTest {
    @Test
    void pagesNavigateResetAndFitViewport() throws Exception {
        CountDownLatch ready = new CountDownLatch(1);
        try {
            Platform.startup(ready::countDown);
        } catch (IllegalStateException alreadyStarted) {
            ready.countDown();
        }
        assertTrue(ready.await(15, TimeUnit.SECONDS));
        CountDownLatch done = new CountDownLatch(1);
        AtomicReference<Throwable> failure = new AtomicReference<>();
        Platform.runLater(() -> {
            try {
                GameMenuView menus = new GameMenuView();
                int[] backs = { 0 };
                StackPane root = new StackPane();
                Scene scene = new Scene(root, 1024, 576);
                scene.getStylesheets().add(getClass().getResource("/Styles/game.css").toExternalForm());
                Node help = menus.howToPlay(() -> backs[0]++);
                mount(root, help);
                assertTrue(text(help).contains("1 / 5"));
                assertTrue(button(help, "PREVIOUS").isDisabled());
                String[] titles = { "MOVEMENT & EXPLORATION", "THE PARADIGM TRIALS", "MEET YOUR HELPERS",
                        "UNDERSTANDING THE LIFELINES", "CHECKPOINTS & THE CORE GATE" };
                for (int i = 0; i < 5; i++) {
                    root.applyCss();
                    root.layout();
                    snapshot(root, "tutorial-" + (i + 1));
                    String current = text(help);
                    assertTrue(current.contains((i + 1) + " / 5"));
                    for (int j = 0; j < 5; j++)
                        assertEquals(i == j, current.contains(titles[j]));
                    Button action = button(help, i == 4 ? "FINISH" : "NEXT");
                    assertTrue(action.getBoundsInParent().getHeight() > 0);
                    assertTrue(action.localToScene(action.getBoundsInLocal()).getMaxY() <= 576);
                    if (i < 4)
                        action.fire();
                }
                button(help, "PREVIOUS").fire();
                assertTrue(text(help).contains("4 / 5"));
                button(help, "NEXT").fire();
                button(help, "FINISH").fire();
                assertEquals(1, backs[0]);
                help = menus.howToPlay(() -> backs[0]++);
                mount(root, help);
                assertTrue(text(help).contains("1 / 5"));
                Node story = menus.story(() -> backs[0]++);
                mount(root, story);
                snapshot(root, "story");
                assertTrue(text(story).contains("Your journey begins here."));
                button(story, "BACK TO MENU").fire();
                Node credits = menus.credits(() -> backs[0]++);
                mount(root, credits);
                snapshot(root, "credits");
                assertTrue(text(credits).contains("Benjamin A. Deypalan"));
                assertTrue(text(credits).contains("Dave Javenn Suan"));
                assertTrue(text(credits).contains("END OF TRANSMISSION."));
                button(credits, "BACK TO MENU").fire();
                assertEquals(3, backs[0]);
                assertEquals(SoundManager.Music.VICTORY, SoundManager.musicFor(GameState.CREDITS, false));
                assertEquals(SoundManager.Music.MENU, SoundManager.musicFor(GameState.MENU, false));
                root.getChildren().clear();
            } catch (Throwable error) {
                failure.set(error);
            } finally {
                done.countDown();
            }
        });
        assertTrue(done.await(20, TimeUnit.SECONDS));
        if (failure.get() != null)
            throw new AssertionError(failure.get());
    }

    private static void snapshot(StackPane root, String name) throws java.io.IOException {
        // Finish the entry fade for a deterministic layout capture.
        for (Node node : nodes(root))
            node.setOpacity(1);
        javafx.scene.image.WritableImage image = root.snapshot(null, null);
        java.awt.image.BufferedImage output = new java.awt.image.BufferedImage(1024, 576,
                java.awt.image.BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < 576; y++)
            for (int x = 0; x < 1024; x++)
                output.setRGB(x, y, image.getPixelReader().getArgb(x, y));
        javax.imageio.ImageIO.write(output, "png", new java.io.File("target/menu-" + name + ".png"));
    }

    private static void mount(StackPane root, Node page) {
        root.getChildren().setAll(page);
        root.applyCss();
        root.layout();
    }

    private static List<Node> nodes(Node root) {
        List<Node> result = new ArrayList<>();
        result.add(root);
        if (root instanceof Parent)
            for (Node child : ((Parent) root).getChildrenUnmodifiable())
                result.addAll(nodes(child));
        return result;
    }

    private static String text(Node root) {
        StringBuilder result = new StringBuilder();
        for (Node node : nodes(root))
            if (node instanceof Label)
                result.append(((Label) node).getText()).append('\n');
        return result.toString();
    }

    private static Button button(Node root, String title) {
        return nodes(root).stream().filter(n -> n instanceof Button && ((Button) n).getText().equals(title))
                .map(n -> (Button) n).findFirst().orElseThrow();
    }
}
