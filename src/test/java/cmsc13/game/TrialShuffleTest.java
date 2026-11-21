package cmsc13.game;

import java.util.List;
import java.util.Random;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TrialShuffleTest {
    @Test
    void attemptsShuffleChoicesWithoutChangingBankOrCorrectAnswers() {
        QuestionBank bank = new QuestionBank();
        TrialManager manager = new TrialManager(bank, new Random(42));
        boolean changed = false;
        for (int trialId = 1; trialId <= bank.getTrialCount(); trialId++) {
            List<Question> originals = bank.getQuestionsForTrial(trialId);
            Trial attempt = manager.startTrial(trialId);
            for (Question shuffled : attempt.getQuestions()) {
                Question source = originals.stream().filter(q -> q.getId() == shuffled.getId()).findFirst().orElseThrow();
                assertNotSame(source, shuffled);
                assertEquals(source.getCorrectChoiceText(), shuffled.getCorrectChoiceText());
                assertEquals(source.getChoices().size(), shuffled.getChoices().size());
                assertTrue(shuffled.getChoices().containsAll(source.getChoices()));
                assertTrue(shuffled.isCorrectAnswer(shuffled.getCorrectAnswerIndex()));
                for (int choice = 0; choice < shuffled.getChoices().size(); choice++) {
                    assertEquals(choice == shuffled.getCorrectAnswerIndex(), shuffled.isCorrectAnswer(choice));
                }
                for (HelperAdvisor.Kind kind : HelperAdvisor.Kind.values()) {
                    HelperAdvisor.Advice advice = new HelperAdvisor(kind).advise(shuffled);
                    assertTrue(advice.getChoice() >= 0 && advice.getChoice() < shuffled.getChoices().size());
                    assertTrue(advice.getMessage().endsWith(shuffled.getChoiceLetter(advice.getChoice())
                            + ". " + shuffled.getChoices().get(advice.getChoice())));
                }
                changed |= !source.getChoices().equals(shuffled.getChoices());
            }
            assertSame(attempt.getCurrentQuestion(), attempt.getCurrentQuestion());
        }
        assertTrue(changed);
    }
}
