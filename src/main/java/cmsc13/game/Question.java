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
    
    // New Getters for programming properties
    public String getProgType() { return progType; }
    public String getProgLang() { return progLang; }
    
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