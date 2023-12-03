@SuppressWarnings("serial")
public class LexicalError extends AnalysisError {
    private String lexeme;

    public LexicalError(String msg, int position, String lexeme) {
        super(msg, position);
        this.lexeme = lexeme;
    }

    public LexicalError(String msg, int position) {
        super(msg, position);
    }

    public LexicalError(String msg) {
        super(msg);
    }

    public String getLexeme() {
        return this.lexeme;
    }
}
