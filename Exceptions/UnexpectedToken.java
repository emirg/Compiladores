public class UnexpectedToken extends Exception{
    private static final long serialVersionUID = 1L;

    public UnexpectedToken(String message, int numLine) {
        super("Unexpected token. '" + message + "' was expected at line " + numLine + ".");
    }
}