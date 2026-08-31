package cmsc13.game;

import java.util.EnumSet;
import java.util.Random;

/** Configurable advisor logic.  Advice is calculated from question metadata, never a question-id table. */
public final class HelperAdvisor {
    public enum Kind { GENIUS, BOOKWORM, HACKER, ENCOURAGER, TRICKSTER }

    public static final class Advice {
        private final int choice;
        private final int confidence;
        private final String message;
        Advice(int choice, int confidence, String message) {
            this.choice = choice; this.confidence = confidence; this.message = message;
        }
        public int getChoice() { return choice; }
        public int getConfidence() { return confidence; }
        public String getMessage() { return message; }
    }

    private final Kind kind;
    private final Random random = new Random();
    private final int baseConfidence;
    private final EnumSet<QuestionType> strengths;

    public HelperAdvisor(Kind kind) {
        this.kind = kind;
        switch (kind) {
            case GENIUS: baseConfidence = 82; strengths = EnumSet.allOf(QuestionType.class); break;
            case BOOKWORM: baseConfidence = 68; strengths = EnumSet.of(QuestionType.THEORY); break;
            case HACKER: baseConfidence = 68; strengths = EnumSet.of(QuestionType.PROGRAMMING); break;
            case ENCOURAGER: baseConfidence = 57; strengths = EnumSet.noneOf(QuestionType.class); break;
            default: baseConfidence = 48; strengths = EnumSet.noneOf(QuestionType.class); break;
        }
    }

    public Kind getKind() { return kind; }

    public Advice advise(Question question) {
        int confidence = baseConfidence + (strengths.contains(question.getType()) ? 16 : 0)
            - (question.getDifficulty() - 1) * 5 + random.nextInt(13) - 6;
        if (kind == Kind.TRICKSTER) confidence += random.nextBoolean() ? 18 : -20;
        confidence = Math.max(25, Math.min(96, confidence));
        int choice = random.nextInt(100) < confidence ? question.getCorrectAnswerIndex() : wrongChoice(question);
        String lead;
        switch (kind) {
            case GENIUS: lead = "Let's reason this out. The key concept points to"; break;
            case BOOKWORM: lead = "Remember the definition; I would choose"; break;
            case HACKER: lead = "Look at the execution flow. My pick is"; break;
            case ENCOURAGER: lead = "You've got this! I'm leaning toward"; break;
            default: lead = confidence > 55 ? "A delightful trap, perhaps. Try" : "The system feels suspiciously drawn to";
        }
        return new Advice(choice, confidence, lead + " " + question.getChoiceLetter(choice) + ". " + question.getChoices().get(choice));
    }

    private int wrongChoice(Question q) {
        int pick = random.nextInt(3);
        return pick >= q.getCorrectAnswerIndex() ? pick + 1 : pick;
    }
}
