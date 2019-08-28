package Compilador;

import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import Exceptions.UnexpectedToken;

public class Compilador {

    public static void main(String[] args) {
        
        AnalizadorLexico lexico = new AnalizadorLexico(args[0]);

        AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
        try {
            sintactico.program();
        } catch (UnexpectedToken e) {
            System.err.println(e.getMessage());
        }

    }
}
