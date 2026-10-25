package cmsc13.game;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;

/** Loads the question bank from JSON and selects questions for the game. */
public class QuestionBank {
    private static final Set<String> VALID_TOPICS = new HashSet<>(Arrays.asList(
        "Introduction to Programming Paradigms",
        "Procedural Programming",
        "Functional Programming",
        "Object-Oriented Programming",
        "Imperative vs Declarative Programming",
        "Event-Driven Programming",
        "Component Mappings between Programming Paradigms",
        "Comprehensive Programming Mastery"
    ));
    
    private final List<Question> allQuestions;

    public QuestionBank() {
        this(openQuestionFile());
    }

    // Accept a reader so loading failures can also be tested without changing the file.
    QuestionBank(Reader reader) {
        this.allQuestions = loadQuestions(reader);
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            throw new IllegalStateException("Invalid questions.json: " + String.join("; ", errors));
        }
    }

    private static Reader openQuestionFile() {
        InputStream input = QuestionBank.class.getResourceAsStream("/questions.json");
        if (input == null) {
            throw new IllegalStateException("Cannot find questions.json in game resources");
        }
        return new InputStreamReader(input, StandardCharsets.UTF_8);
    }

    private static List<Question> loadQuestions(Reader source) {
        try (Reader reader = source) {
            JsonElement root = JsonParser.parseReader(reader);
            if (!root.isJsonArray() || root.getAsJsonArray().size() == 0) {
                throw new IllegalStateException("questions.json must contain a non-empty array");
            }

            List<Question> questions = new ArrayList<>();
            Gson gson = new Gson();
            for (JsonElement entry : root.getAsJsonArray()) {
                int position = questions.size() + 1;
                if (!entry.isJsonObject()) {
                    throw new IllegalStateException("Question entry " + position + " must be an object");
                }
                JsonObject object = entry.getAsJsonObject();
                // Gson otherwise defaults absent integer fields to zero, which can hide mistakes.
                for (String field : Arrays.asList("id", "type", "topic", "questionText",
                        "choices", "correctAnswerIndex", "explanation", "difficulty", "trialId")) {
                    if (!object.has(field) || object.get(field).isJsonNull()) {
                        throw new IllegalStateException(
                            "Question entry " + position + " is missing " + field);
                    }
                }
                questions.add(gson.fromJson(object, Question.class));
            }
            return questions;
        } catch (IOException | JsonParseException e) {
            throw new IllegalStateException("Could not read questions.json: " + e.getMessage(), e);
        }
    }

    // Getters
    public List<Question> getAllQuestions() {
        return new ArrayList<>(allQuestions);
    }
    
    public List<Question> getQuestionsForTrial(int trialId) {
        return allQuestions.stream()
            .filter(q -> q.getTrialId() == trialId)
            .collect(Collectors.toCollection(ArrayList::new));
    }

    /** Number of trials represented by the loaded question data. */
    public int getTrialCount() {
        return allQuestions.stream().mapToInt(Question::getTrialId).max().orElse(0);
    }
    
    public List<Question> getTheoryQuestions() {
        return allQuestions.stream()
            .filter(q -> q.getType() == QuestionType.THEORY)
            .collect(Collectors.toList());
    }
    
    public List<Question> getProgrammingQuestions() {
        return allQuestions.stream()
            .filter(q -> q.getType() == QuestionType.PROGRAMMING)
            .collect(Collectors.toList());
    }
    
    public int getTotalQuestions() {
        return allQuestions.size();
    }

    /** Validation checks ensuring distribution meets all requirements. */
    public List<String> validate() {
        List<String> errors = new ArrayList<>();
        if (allQuestions.size() != 145) {
            errors.add("Expected 145 questions total, found " + allQuestions.size());
        }
        if (getTheoryQuestions().size() != 70 || getProgrammingQuestions().size() != 75) {
            errors.add("Expected 70 theory and 75 programming questions; found "
                + getTheoryQuestions().size() + " theory and " + getProgrammingQuestions().size() + " programming");
        }
        
        // Validate theory distribution (Trials 1-7 must have exactly 10 theory each)
        for (int trialId = 1; trialId <= 7; trialId++) {
            final int tId = trialId;
            long theory = getTheoryQuestions().stream().filter(q -> q.getTrialId() == tId).count();
            if (theory != 10) {
                errors.add("Trial " + trialId + " must contain exactly 10 theory questions");
            }
        }
        
        Set<Integer> ids = new HashSet<>();
        // Check the data before any trial or screen uses it.
        for (Question question : allQuestions) {
            String label = "Question " + question.getId();
            if (question.getId() <= 0 || !ids.add(question.getId())) {
                errors.add(label + " must have a unique positive ID");
            }
            if (question.getType() == null) {
                errors.add(label + " must use THEORY or PROGRAMMING");
            }
            if (isBlank(question.getQuestionText()) || isBlank(question.getExplanation())) {
                errors.add(label + " must have question text and an explanation");
            }
            if (question.getDifficulty() < 1 || question.getDifficulty() > 5) {
                errors.add(label + " must have difficulty from 1 to 5");
            }
            if (question.getType() == QuestionType.PROGRAMMING
                    && (isBlank(question.getProgType()) || isBlank(question.getProgLang()))) {
                errors.add(label + " must have progType and progLang");
            }
            if (question.getChoices() != null) {
                if (question.getChoices().stream().anyMatch(QuestionBank::isBlank)) {
                    errors.add(label + " has an empty choice");
                }
                if (question.getCorrectAnswerIndex() < 0
                        || question.getCorrectAnswerIndex() >= question.getChoices().size()) {
                    errors.add(label + " has an invalid correctAnswerIndex");
                }
            }
            if (question.getTrialId() < 1 || question.getTrialId() > 8) {
                errors.add("Question " + question.getId() + " has invalid trial ID " + question.getTrialId());
            }
            if (!VALID_TOPICS.contains(question.getTopic())) {
                errors.add("Question " + question.getId() + " has invalid topic: " + question.getTopic());
            }
            if (question.getChoices() == null || question.getChoices().size() != 4) {
                errors.add("Question " + question.getId() + " must have exactly four choices");
            }
        }
        return errors;
    }
    private static boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}