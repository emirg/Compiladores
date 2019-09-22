package Compilador;

import AnalizadorSintactico.AnalizadorSintactico;
import AnalizadorLexico.AnalizadorLexico;
import Exceptions.IdentifierAlreadyDefinedException;
import Exceptions.IdentifierNotDefinedException;
import Exceptions.UnclosedCommentException;
import Exceptions.UnexpectedCharException;
import Exceptions.UnexpectedTokenException;
import Exceptions.UnopenedCommentException;
import Exceptions.WrongArgumentsException;
import Exceptions.WrongConstructorException;
import Exceptions.WrongTypeException;
import java.io.FileNotFoundException;
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class Compilador {

    public static void main(String[] args) {

        AnalizadorLexico lexico;
        try {
            lexico = new AnalizadorLexico("/home/emiliano/Git/Facultad/Compiladores/test/AnalizadorSintactico/Ej10a.pas");
            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, false);
            sintactico.program();
            System.out.println("Compilation successful");
        } catch (FileNotFoundException ex) {
            // Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("File not found");
        } catch (UnexpectedTokenException | UnexpectedCharException | UnopenedCommentException | UnclosedCommentException | IdentifierAlreadyDefinedException | IdentifierNotDefinedException | WrongTypeException | WrongConstructorException | WrongArgumentsException e) {
            System.err.println(e.getMessage());
            System.err.println("Compilation failed");
        }

    }
}
