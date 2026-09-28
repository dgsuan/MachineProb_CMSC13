package cmsc13.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/** Creates a fresh, independently shuffled question sequence for each trial attempt. */
public final class TrialManager {
    private final QuestionBank questionBank;
    private final Random random;

    /** Uses an unseeded shuffle for normal gameplay. */
    public TrialManager(QuestionBank questionBank) {
        this(questionBank, new Random());
    }

    /** Allows a seeded random source when repeatable question order is useful. */
    TrialManager(QuestionBank questionBank, Random random) {
        this.questionBank = questionBank;
        this.random = random;
    }

    /** Starts a trial with its own shuffled copy; the question bank itself is never reordered. */
    public Trial startTrial(int trialId) {
        List<Question> questions = new ArrayList<>(questionBank.getQuestionsForTrial(trialId));
        if (questions.isEmpty()) throw new IllegalArgumentException("No questions for trial " + trialId);
        Collections.shuffle(questions, random);
        return new Trial(trialId, questions);
    }
}
