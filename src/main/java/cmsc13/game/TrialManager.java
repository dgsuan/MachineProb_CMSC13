// package cmsc13.game;

// // import cmsc13.game.core.GameData;
// // import cmsc13.game.util.Constants;
// import java.util.ArrayList;
// import java.util.Collections;
// import java.util.List;

// /**
//  * Manages trials and trial progression
//  */
// public class TrialManager {
    
//     private QuestionBank questionBank;
//     private GameData gameData;
//     private int currentTrialId;
//     private List<Question> currentQuestions;
//     private int currentQuestionIndex;
//     private int currentTrialExp;
//     private boolean lastAnswerCorrect;
    
//     public TrialManager(QuestionBank questionBank, GameData gameData) {
//         this.questionBank = questionBank;
//         this.gameData = gameData;
//     }
    
//     public void startTrial(int trialId) {
//         this.currentTrialId = trialId;
//         this.currentQuestions = new ArrayList<>(questionBank.getQuestionsForTrial(trialId));
//         Collections.shuffle(this.currentQuestions);
//         this.currentQuestionIndex = 0;
//         this.currentTrialExp = 0;
//         this.lastAnswerCorrect = false;
//     }
    
//     public Question getCurrentQuestion() {
//         if (currentQuestions != null && currentQuestionIndex < currentQuestions.size()) {
//             return currentQuestions.get(currentQuestionIndex);
//         }
//         return null;
//     }
    
//     public boolean submitAnswer(int answerIndex) {
//         Question q = getCurrentQuestion();
//         lastAnswerCorrect = q != null && q.isCorrectAnswer(answerIndex);
//         if (lastAnswerCorrect) {
//             currentTrialExp += Constants.CORRECT_ANSWER_EXP;
//         }
//         return lastAnswerCorrect;
//     }
    
//     public void nextQuestion() {
//         if (currentQuestions != null && currentQuestionIndex < currentQuestions.size()) {
//             currentQuestionIndex++;
//         }
//     }
    
//     public void showFeedback(java.util.function.Consumer<Boolean> callback) {
//         callback.accept(lastAnswerCorrect);
//     }
    
//     public boolean hasMoreQuestions() {
//         return currentQuestions != null && currentQuestionIndex < currentQuestions.size();
//     }
    
//     public boolean didTrialPass() {
//         return currentTrialExp >= Constants.REQUIRED_EXP_PER_TRIAL;
//     }
    
//     public void completeTrial() {
//         gameData.completedTrials.add(currentTrialId);
//     }
    
//     public void resetCurrentTrial() {
//         startTrial(currentTrialId);
//     }
    
//     public void addExp(int amount) {
//         currentTrialExp += amount;
//     }
    
//     public int getCurrentTrialId() { return currentTrialId; }
//     public int getCurrentTrialExp() { return currentTrialExp; }
//     public int getQuestionCount() { return currentQuestions == null ? 0 : currentQuestions.size(); }
//     public int getCurrentQuestionNumber() { return currentQuestionIndex + 1; }
// }
