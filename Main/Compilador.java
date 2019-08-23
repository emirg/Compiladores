public class Compilador {

    public static void main(String[] args) {
        // AnalizadorLexico lexico = new AnalizadorLexico(args[0]);
        AnalizadorLexico lexico = new AnalizadorLexico("C:\\Users\\emi_r\\Documents\\Git\\Compiladores\\Analizador Sintactico\\tests\\Ej01a.pas");
        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
        try {
            sintactico.program();
        } catch (UnexpectedToken e) {
            System.out.println(e.getMessage());
        }

    }
}
