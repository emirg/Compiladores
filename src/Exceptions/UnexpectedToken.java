package Exceptions;

public class UnexpectedToken extends SyntaxException {
    private static final long serialVersionUID = 1L;

    public UnexpectedToken(String expected, String found, int numLine) {
        super("Unexpected token. '" + expected + "' was expected but found '" + found + "'", numLine);
    }
}