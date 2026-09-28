package cmsc13.game;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/** Presents the question catalogue and reveals full details only for questions answered correctly. */
final class QuestionBankView extends BorderPane {
    private final QuestionBank bank;
    private final Set<Integer> unlockedIds;
    private final VBox details = new VBox(12);
    private final ListView<Question> questionList = new ListView<>();
    private QuestionType selectedType = QuestionType.THEORY;

    /** Creates the catalogue with category switching and a destination callback for the back button. */
    QuestionBankView(QuestionBank bank, Set<Integer> unlockedIds, Runnable onBack) {
        this.bank = bank;
        this.unlockedIds = unlockedIds;
        setPadding(new Insets(24));
        setStyle("-fx-background-color: linear-gradient(to bottom right, #111927, #263d4a);");

        Label title = new Label("SYSTEMBOUND  /  QUESTION ARCHIVE");
        title.setStyle("-fx-text-fill: #e8f4ef; -fx-font-size: 24; -fx-font-weight: bold; -fx-font-family: 'Consolas';");
        Label subtitle = new Label("Questions unlock in this archive after you answer them correctly in a trial.");
        subtitle.setStyle("-fx-text-fill: #b7c8cf; -fx-font-size: 13;");
        VBox heading = new VBox(6, title, subtitle);

        Button theory = categoryButton("THEORETICAL  (" + bank.getTheoryQuestions().size() + ")", QuestionType.THEORY);
        Button programming = categoryButton("PROGRAMMING  (" + bank.getProgrammingQuestions().size() + ")", QuestionType.PROGRAMMING);
        Button back = new Button("BACK TO MENU");
        back.setOnAction(event -> onBack.run());
        HBox categories = new HBox(10, theory, programming, back);
        categories.setAlignment(Pos.CENTER_LEFT);
        VBox top = new VBox(18, heading, categories);
        setTop(top);

        questionList.setPrefWidth(410);
        questionList.setCellFactory(list -> new ListCell<Question>() {
            @Override protected void updateItem(Question question, boolean empty) {
                super.updateItem(question, empty);
                if (empty || question == null) { setText(null); setGraphic(null); setStyle(""); return; }
                setText(null);
                int index = getIndex();
                boolean firstInTrial = index == 0 || list.getItems().get(index - 1).getTrialId() != question.getTrialId();
                VBox row = new VBox(3);
                if (firstInTrial) {
                    Label trialHeading = new Label("TRIAL " + question.getTrialId());
                    trialHeading.setMaxWidth(Double.MAX_VALUE);
                    trialHeading.setStyle("-fx-background-color: #183f4d; -fx-text-fill: #8ce8f2; -fx-font-family: 'Consolas'; -fx-font-weight: bold; -fx-padding: 8 10;");
                    row.getChildren().add(trialHeading);
                }
                Label line = new Label(unlockedIds.contains(question.getId())
                    ? String.format("UNLOCKED  #%03d  %s", question.getId(), clip(question.getQuestionText(), 74))
                    : String.format("LOCKED  #%03d", question.getId()));
                line.setWrapText(true); line.setMaxWidth(Double.MAX_VALUE);
                line.setStyle("-fx-padding: 8 10; -fx-font-family: 'Consolas'; -fx-text-fill: " + (unlockedIds.contains(question.getId()) ? "#17332c" : "#71808b") + ";");
                row.getChildren().add(line); setGraphic(row); setStyle("-fx-padding: 2 0;");
            }
        });
        questionList.getSelectionModel().selectedItemProperty().addListener((obs, oldQuestion, question) -> showDetails(question));
        questionList.setStyle("-fx-background-color: #e8eff0; -fx-background-radius: 12;");
        details.setPadding(new Insets(18));
        details.setStyle("-fx-background-color: rgba(245,249,247,.97); -fx-background-radius: 12;");
        details.getChildren().add(styled("Choose an unlocked question to view its answer and explanation.", 16, "#52616b"));
        ScrollPane detailScroll = new ScrollPane(details);
        detailScroll.setFitToWidth(true);
        detailScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        HBox content = new HBox(16, questionList, detailScroll);
        content.setPadding(new Insets(20, 0, 0, 0));
        HBox.setHgrow(detailScroll, javafx.scene.layout.Priority.ALWAYS);
        setCenter(content);
        loadCategory(selectedType);
    }

    /** Builds a category switch and refreshes the list for that question type. */
    private Button categoryButton(String label, QuestionType type) {
        Button button = new Button(label);
        button.setOnAction(event -> {
            selectedType = type;
            loadCategory(type);
        });
        return button;
    }

    /** Loads a category without changing the underlying question bank. */
    private void loadCategory(QuestionType type) {
        List<Question> questions = bank.getAllQuestions().stream()
            .filter(question -> question.getType() == type)
            .sorted(java.util.Comparator.comparingInt(Question::getTrialId).thenComparingInt(Question::getId))
            .collect(Collectors.toList());
        questionList.getItems().setAll(questions);
        questionList.getSelectionModel().clearSelection();
        details.getChildren().setAll(styled("Choose an unlocked question to view its answer and explanation.", 16, "#52616b"));
    }

    /** Shows full question metadata only after its ID has been unlocked by gameplay. */
    private void showDetails(Question question) {
        details.getChildren().clear();
        if (question == null) return;
        if (!unlockedIds.contains(question.getId())) {
            details.getChildren().addAll(
                styled("QUESTION LOCKED", 21, "#a65346"),
                styled("Answer this question correctly in its trial to reveal the question, choices, answer, and explanation here.", 15, "#52616b"));
            return;
        }
        details.getChildren().addAll(
            styled("QUESTION " + question.getId() + "  ·  TRIAL " + question.getTrialId(), 13, "#28736a"),
            styled(question.getQuestionText(), 19, "#17252d"),
            styled("Topic: " + question.getTopic() + "     Difficulty: " + question.getDifficulty() + " / 5", 14, "#52616b")
        );
        if (question.getType() == QuestionType.PROGRAMMING) {
            details.getChildren().add(styled("Programming type: " + question.getProgType() + "     Language: " + question.getProgLang(), 14, "#52616b"));
        }
        for (int index = 0; index < question.getChoices().size(); index++) {
            String choice = (char) ('A' + index) + ". " + question.getChoices().get(index);
            details.getChildren().add(styled(choice, 14, index == question.getCorrectAnswerIndex() ? "#24764e" : "#273640"));
        }
        details.getChildren().addAll(
            styled("Correct answer: " + question.getCorrectChoiceText(), 15, "#24764e"),
            styled("Explanation: " + question.getExplanation(), 15, "#354750"));
    }

    /** Creates a wrapped text block with consistent archive typography. */
    private static Label styled(String value, int size, String color) {
        Label label = new Label(value);
        label.setWrapText(true);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setStyle("-fx-font-size: " + size + "; -fx-text-fill: " + color + "; -fx-font-family: 'Consolas';");
        return label;
    }

    /** Shortens locked-list labels without revealing question content. */
    private static String clip(String text, int maxLength) {
        return text.length() <= maxLength ? text : text.substring(0, maxLength - 1) + "…";
    }
}
