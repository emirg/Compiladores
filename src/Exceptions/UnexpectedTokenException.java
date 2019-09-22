package Exceptions;

public class UnexpectedTokenException extends SyntaxException {
    private static final long serialVersionUID = 1L;

    public UnexpectedTokenException(String expected, String found, int numLine) {
        super("Unexpected token. '" + expected + "' was expected but found '" + found + "'", numLine);
    }
}