package cmsc13.game;

import java.util.List;

/** Immutable data for one reusable multiple-choice question. */
public final class Question {
    private final String topic;
    private final QuestionType type;
    private final String text;
    private final List<String> choices;
    private final int correctAnswer;

    public Question(String topic, QuestionType type, String text, List<String> choices, int correctAnswer) {
        this.topic = topic;
        this.type = type;
        this.text = text;
        this.choices = List.copyOf(choices);
        this.correctAnswer = correctAnswer;
    }

    public String getTopic() { return topic; }
    public QuestionType getType() { return type; }
    public String getText() { return text; }
    public List<String> getChoices() { return choices; }
    public boolean isCorrect(int answer) { return answer == correctAnswer; }
}
