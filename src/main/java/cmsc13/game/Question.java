package cmsc13.game;

import java.util.List;

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
    
    public Question(
        int id,
        QuestionType type,
        String topic,
        String questionText,
        List<String> choices,
        int correctAnswerIndex,
        String explanation,
        int difficulty,
        int trialId
    ) {
        this.id = id;
        this.type = type;
        this.topic = topic;
        this.questionText = questionText;
        this.choices = choices;
        this.correctAnswerIndex = correctAnswerIndex;
        this.explanation = explanation;
        this.difficulty = difficulty;
        this.trialId = trialId;
    }
    
    public boolean isCorrectAnswer(int selectedIndex) {
        return selectedIndex == correctAnswerIndex;
    }
    
    // Getters
    public int getId() { return id; }
    public QuestionType getType() { return type; }
    public String getTopic() { return topic; }
    public String getQuestionText() { return questionText; }
    public List<String> getChoices() { return choices; }
    public int getCorrectAnswerIndex() { return correctAnswerIndex; }
    public String getExplanation() { return explanation; }
    public int getDifficulty() { return difficulty; }
    public int getTrialId() { return trialId; }
    public String getCorrectChoiceText() { return choices.get(correctAnswerIndex); }
    
    public String getChoiceLetter(int index) {
        char[] letters = {'A', 'B', 'C', 'D'};
        return String.valueOf(letters[Math.min(index, 3)]);
    }
}
