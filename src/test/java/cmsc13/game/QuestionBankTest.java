package cmsc13.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class QuestionBankTest {
    private JsonArray questionData() throws Exception {
        try (InputStreamReader reader = new InputStreamReader(
                getClass().getResourceAsStream("/questions.json"), StandardCharsets.UTF_8)) {
            return JsonParser.parseReader(reader).getAsJsonArray();
        }
    }

    private IllegalStateException rejects(JsonArray data) {
        return assertThrows(IllegalStateException.class,
            () -> new QuestionBank(new StringReader(data.toString())));
    }

    @Test
    void loadsAllTrialsAndPreservesCodeSnippets() {
        QuestionBank bank = new QuestionBank();
        assertEquals(145, bank.getTotalQuestions());
        assertEquals(70, bank.getTheoryQuestions().size());
        assertEquals(75, bank.getProgrammingQuestions().size());
        int[] counts = {14, 15, 16, 17, 18, 19, 21, 25};
        for (int trial = 1; trial <= 8; trial++) {
            assertEquals(counts[trial - 1], bank.getQuestionsForTrial(trial).size());
        }
        Question code = bank.getAllQuestions().get(10);
        assertEquals("Given:\nint a = 5;\nint b = 3;\nprintf(\"%d\", a + b * 2);",
            code.getQuestionText());
        assertEquals("11", code.getCorrectChoiceText());
        assertTrue(bank.validate().isEmpty());
    }

    @Test
    void rejectsMissingAnswerInsteadOfDefaultingToFirstChoice() throws Exception {
        JsonArray data = questionData();
        data.get(0).getAsJsonObject().remove("correctAnswerIndex");
        assertTrue(rejects(data).getMessage().contains("missing correctAnswerIndex"));
    }

    @Test
    void rejectsOutOfRangeAnswers() throws Exception {
        JsonArray data = questionData();
        data.get(0).getAsJsonObject().addProperty("correctAnswerIndex", 4);
        assertTrue(rejects(data).getMessage().contains("invalid correctAnswerIndex"));
    }

    @Test
    void rejectsDuplicateIds() throws Exception {
        JsonArray data = questionData();
        data.get(1).getAsJsonObject().addProperty("id", 1);
        assertTrue(rejects(data).getMessage().contains("unique positive ID"));
    }

    @Test
    void rejectsUnknownQuestionTypes() throws Exception {
        JsonArray data = questionData();
        data.get(0).getAsJsonObject().addProperty("type", "TYPO");
        assertTrue(rejects(data).getMessage().contains("THEORY or PROGRAMMING"));
    }

    @Test
    void rejectsNullEntriesAndEmptyBanks() {
        assertThrows(IllegalStateException.class,
            () -> new QuestionBank(new StringReader("[null]")));
        assertThrows(IllegalStateException.class,
            () -> new QuestionBank(new StringReader("[]")));
    }

    @Test
    void rejectsMalformedJson() {
        assertThrows(IllegalStateException.class,
            () -> new QuestionBank(new StringReader("[{\"id\":")));
    }
}
