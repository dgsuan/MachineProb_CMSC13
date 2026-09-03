package cmsc13.game;

import java.util.List;

/** Tracks one attempt at Trial 1 and deliberately has no UI code. */
public final class Trial {
    public static final int REQUIRED_EXP = 3;
    private  int number;
    private  List<Question> questions;
    private int questionIndex;
    private int exp;
    private int trialID;

    public Trial(QuestionBank questionBank, int trialID) {
        this.number = trialID;
        this.trialID = trialID;
        this.questions = questionBank.getQuestionsForTrial(this.trialID);
    }

    public int getNumber() { return number; }

    public Question getCurrentQuestion() { return questions.get(questionIndex); }
    public boolean answer(int selectedAnswer) {
        boolean correct = getCurrentQuestion().isCorrectAnswer(selectedAnswer);
        if (correct) exp++;
        else exp = Math.max(0, exp - 1);
        return correct;
    }

    /** Applies a lifeline-backed answer without duplicating question correctness logic. */
    public boolean answerWithSave(int selectedAnswer, boolean saveActive) {
        boolean correct = getCurrentQuestion().isCorrectAnswer(selectedAnswer);
        if (correct) exp++;
        else if (!saveActive) exp = Math.max(0, exp - 1);
        return correct;
    }
    public void nextQuestion() { questionIndex++; }
    public boolean isFinished() { return questionIndex >= questions.size(); }
    public boolean isComplete() { return exp >= REQUIRED_EXP; }
    public int getExp() { return exp; }
    public int getQuestionNumber() { return questionIndex + 1; }
}
