public class Compilador{

    public static void main(String[] args) {
        AnalizadorLexico lexico = new AnalizadorLexico(args[0]);
        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);

    }
}
