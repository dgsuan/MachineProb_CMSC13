package cmsc13.game;

/**
 * Question type enumeration
 */
public enum QuestionType {
    THEORY("Theoretical"),
    PROGRAMMING("Programming/Practical");
    
    private final String displayName;
    
    QuestionType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}
