package cmsc13.game;

import java.util.List;
import java.util.ArrayList;

/** Tracks question order, answers, and earned EXP for one gameplay attempt. */
public final class Trial {
    /** Minimum correctly answered questions required in addition to the cumulative score target. */
    public static final int REQUIRED_CORRECT_ANSWERS = 3;
    private final int number;
    private final List<Question> questions;
    private int questionIndex;
    private int correctAnswerCount;
    private final int trialID;

    public Trial(QuestionBank questionBank, int trialID) {
        this(trialID, questionBank.getQuestionsForTrial(trialID));
    }

    /** Builds an attempt from the order prepared by TrialManager. */
    Trial(int trialID, List<Question> orderedQuestions) {
        this.trialID = trialID;
        this.number = trialID;
        this.questions = new ArrayList<>(orderedQuestions);
    }

    public int getNumber() { return number; }
    /** Returns all questions in the order used for this attempt. */
    public List<Question> getQuestions() { return new ArrayList<>(questions); }

    public Question getCurrentQuestion() { return questions.get(questionIndex); }
    public boolean answer(int selectedAnswer) {
        boolean correct = getCurrentQuestion().isCorrectAnswer(selectedAnswer);
        if (correct) correctAnswerCount++;
        else correctAnswerCount = Math.max(0, correctAnswerCount - 1);
        return correct;
    }

    /** Applies a lifeline-backed answer without duplicating question correctness logic. */
    public boolean answerWithSave(int selectedAnswer, boolean saveActive) {
        boolean correct = getCurrentQuestion().isCorrectAnswer(selectedAnswer);
        if (correct) correctAnswerCount++;
        else if (!saveActive) correctAnswerCount = Math.max(0, correctAnswerCount - 1);
        return correct;
    }
    public void nextQuestion() { questionIndex++; }
    public boolean isFinished() { return questionIndex >= questions.size(); }
    /** Whether this attempt met the minimum correct-answer condition. */
    public boolean isComplete() { return correctAnswerCount >= REQUIRED_CORRECT_ANSWERS; }
    /** Number of net correct answers earned during this attempt. */
    public int getCorrectAnswerCount() { return correctAnswerCount; }
    public int getQuestionNumber() { return questionIndex + 1; }
}
