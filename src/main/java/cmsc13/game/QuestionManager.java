package cmsc13.game;

import java.util.Arrays;
import java.util.List;

/** Stores the five Phase 1 questions; later this can load a larger question bank. */
public final class QuestionManager {
    private final List<Question> questions = Arrays.asList(
        question("What is a programming paradigm?", "A style or approach to programming", "A type of computer hardware", "A programming error", "A language compiler", 0),
        question("Why do programming paradigms exist?", "Different problems benefit from different approaches", "To replace all programming languages", "To make code longer", "To avoid testing", 0),
        question("Which paradigm organizes programs around objects with data and behavior?", "Object-oriented programming", "Procedural programming", "Assembly programming", "Machine code", 0),
        question("Which style usually describes what result is wanted instead of every step?", "Declarative programming", "Imperative programming", "Procedural programming", "Debugging", 0),
        question("Which snippet most clearly reacts to a mouse click?", "button.setOnAction(e -> save())", "int total = 0;", "for (int i = 0; i < 5; i++) { }", "class Player { }", 0)
    );

    private Question question(String text, String a, String b, String c, String d, int answer) {
        return new Question("Introduction to Programming Paradigms", QuestionType.THEORETICAL,
            text, Arrays.asList(a, b, c, d), answer);
    }

    public List<Question> getTrialOneQuestions() { return questions; }
}
