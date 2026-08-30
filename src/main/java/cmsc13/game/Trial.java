package cmsc13.game;

import java.util.List;

/** Tracks one attempt at Trial 1 and deliberately has no UI code. */
public final class Trial {
    public static final int REQUIRED_EXP = 3;
    private final int number;
    private final List<Question> questions;
    private int questionIndex;
    private int exp;

    public Trial(int number, List<Question> questions) {
        this.number = number;
        this.questions = questions;
    }

    public int getNumber() { return number; }

    public Question getCurrentQuestion() { return questions.get(questionIndex); }
    public boolean answer(int selectedAnswer) {
        boolean correct = getCurrentQuestion().isCorrect(selectedAnswer);
        if (correct) exp++;
        return correct;
    }
    public void nextQuestion() { questionIndex++; }
    public boolean isFinished() { return questionIndex >= questions.size(); }
    public boolean isComplete() { return exp >= REQUIRED_EXP; }
    public int getExp() { return exp; }
    public int getQuestionNumber() { return questionIndex + 1; }
}
