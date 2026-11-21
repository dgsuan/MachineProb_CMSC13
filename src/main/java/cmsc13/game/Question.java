package cmsc13.game;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Represents a single question
 */
public class Question {

    private int id;
    private QuestionType type;
    private String topic;
    private String questionText;
    private List<String> choices;
    private int correctAnswerIndex;
    private String explanation;
    private int difficulty; // 1-5
    private int trialId;

    // New fields for programming questions
    private String progType;
    private String progLang;

    public Question(
            int id,
            QuestionType type2,
            String topic,
            String questionText,
            List<String> choices,
            int correctAnswerIndex,
            String explanation,
            int difficulty,
            int trialId,
            String progType,
            String progLang
    ) {
        this.id = id;
        this.type = type2;
        this.topic = topic;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.trialId = trialId;
        this.progType = progType;
        this.progLang = progLang;
    }

    public boolean isCorrectAnswer(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }

    /** Copy choices per attempt, tracking indices even when choice texts match. */
    public Question withShuffledChoices(Random random) {
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < choices.size(); i++) order.add(i);
        Collections.shuffle(order, random);
        List<String> shuffled = new ArrayList<>();
        for (int index : order) shuffled.add(choices.get(index));
        return new Question(id, type, topic, questionText, shuffled,
                order.indexOf(correctAnswerIndex), explanation, difficulty, trialId, progType, progLang);
    }

    public String getInstruction() {
        if (type != QuestionType.PROGRAMMING) return "Choose the best answer.";
        if ("Type A".equals(progType)) return "What does the following code output when run as described?";
        if ("Type B".equals(progType)) return "Which code produces the expected output below?";
        if ("Type C".equals(progType)) return "Which code fills the blank to achieve the expected result?";
        return "Read the code and choose the best answer.";
    }

    // Getters
    public int getId() {
        return id;
    }

    public QuestionType getType() {
        return type;
    }

    public String getTopic() {
        return topic;
    }

    public String getQuestionText() {
        return questionText;
    }

    public List<String> getChoices() {
        return choices;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public String getExplanation() {
        return explanation;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public int getTrialId() {
        return trialId;
    }

    // New Getters for programming properties
    public String getProgType() {
        return progType;
    }

    public String getProgLang() {
        return progLang;
    }

    public String getCorrectChoiceText() {
        if (choices != null && correctAnswerIndex >= 0 && correctAnswerIndex < choices.size()) {
            return choices.get(correctAnswerIndex);
        }
        return null;
    }

    public String getChoiceLetter(int index) {
        // Dynamically returns 'A', 'B', 'C', 'D' depending on the index provided
        return String.valueOf((char) ('A' + index));
    }
}
