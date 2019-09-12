package Compilador;

import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import Exceptions.UnclosedCommentException;
import Exceptions.UnexpectedChar;
import Exceptions.UnexpectedToken;
import Exceptions.UnopenedCommentException;
import java.io.FileNotFoundException;
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class Compilador {

    public static void main(String[] args) {

        AnalizadorLexico lexico;
        try {
            lexico = new AnalizadorLexico("/home/emiliano/Programacion/Compiladores/test/AnalizadorSintactico/Ej10a.pas");
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico);
            sintactico.program();
            System.out.println("Compilation successful");
        } catch (FileNotFoundException ex) {
            // Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("File not found");
        } catch (UnexpectedToken | UnexpectedChar | UnopenedCommentException | UnclosedCommentException e) {
            System.err.println(e.getMessage());
            System.err.println("Compilation failed");
        }

    }
}
