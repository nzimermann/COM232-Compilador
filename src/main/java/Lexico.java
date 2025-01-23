public class Lexico implements Constants {
    private int position;
    private String input;
    private Token currentToken;
    private static String[] palavrasReservadas = {
            "do",
            "else",
            "false",
            "fun",
            "if",
            "in",
            "main",
            "out",
            "repeat",
            "true",
            "while"
    };

    public Lexico() {
        this("");
    }

    public Lexico(String input) {
        setInput(input);
    }

    public void setInput(String input) {
        this.input = input;
        setPosition(0);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    public String getParserConstant(int id) {
        return PARSER_ERROR[id];
    }

    public Token nextToken() throws LexicalError {
        if (!hasInput())
            return null;

        int start = position;

        int state = 0;
        int lastState = 0;
        int endState = -1;
        int end = -1;

        while (hasInput()) {
            lastState = state;
            state = nextState(nextChar(), state);

            if (state < 0)
                break;

            else {
                if (tokenForState(state) >= 0) {
                    endState = state;
                    end = position;
                }
            }
        }
        if (endState < 0 || (endState != state && tokenForState(lastState) == -2)
                && !this.getCurrentToken().equals(Constants.t_palavra_reservada))
            throw new LexicalError(SCANNER_ERROR[lastState], start, input.substring(start, position));

        position = end;

        int token = tokenForState(endState);

        if (token == 0)
            return nextToken();
        else {
            String lexeme = input.substring(start, end);
            if (isPalavraReservada(lexeme)) {
                return new Token(getIdFromPalavraReservada(lexeme), lexeme, start);
            } else {
                if (isIdentificador(lexeme)) {
                    return new Token(Constants.t_identificador, lexeme, start);
                } else {

                }
            }
            token = lookupToken(token, lexeme);
            return new Token(token, lexeme, start);
        }
    }

    private int getIdFromPalavraReservada(String lexeme) {
        for (int i = 0; i < palavrasReservadas.length; i++) {
            if (lexeme.equals(palavrasReservadas[i]))
                return (i + 7);
        }
        if (isPalavraReservada(lexeme)) {
            return Constants.t_palavra_reservada;
        } else {
            return Constants.t_while;
        }
    }

    private boolean isIdentificador(String lexeme) {
        return lexeme.matches("(_i|_f|_s)[a-z][a-z0-9]*") && !isPalavraReservada(lexeme);
    }

    private boolean isPalavraReservada(String lexeme) {
        for (String palavra : palavrasReservadas) {
            if (palavra.equals(lexeme)) {
                return true;
            }
        }
        return false;
    }

    private int nextState(char c, int state) {
        int start = SCANNER_TABLE_INDEXES[state];
        int end = SCANNER_TABLE_INDEXES[state + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;

            if (SCANNER_TABLE[half][0] == c)
                return SCANNER_TABLE[half][1];
            else if (SCANNER_TABLE[half][0] < c)
                start = half + 1;
            else // (SCANNER_TABLE[half][0] > c)
                end = half - 1;
        }

        return -1;
    }

    private int tokenForState(int state) {
        if (state < 0 || state >= TOKEN_STATE.length)
            return -1;

        return TOKEN_STATE[state];
    }

    public int lookupToken(int base, String key) {
        int start = SPECIAL_CASES_INDEXES[base];
        int end = SPECIAL_CASES_INDEXES[base + 1] - 1;

        while (start <= end) {
            int half = (start + end) / 2;
            int comp = SPECIAL_CASES_KEYS[half].compareTo(key);

            if (comp == 0)
                return SPECIAL_CASES_VALUES[half];
            else if (comp < 0)
                start = half + 1;
            else // (comp > 0)
                end = half - 1;
        }

        return base;
    }

    private boolean hasInput() {
        return position < input.length();
    }

    private char nextChar() {
        if (hasInput())
            return input.charAt(position++);
        else
            return (char) -1;
    }
}
