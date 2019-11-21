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
import MEPA.MEPAManager;
import java.io.FileNotFoundException;
import java.io.IOException;
// import java.util.logging.Level;
// import java.util.logging.Logger;

public class Compilador {

    public static void main(String[] args) throws InterruptedException {

        AnalizadorLexico lexico;
        MEPAManager mepa;
        try {
            //lexico = new AnalizadorLexico("/home/german/Escritorio/Compiladores/test/AnalizadorSintactico/testError.pas");
            //lexico = new AnalizadorLexico("/home/german/Escritorio/Compiladores/test/MEPA/ejemplo.pas");
            //AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, false);
            lexico = new AnalizadorLexico(args[0]);

            //EMI
            //mepa = new MEPAManager("/home/emiliano/Git/Facultad/Compiladores/test/MEPA/codigoMEPA.txt");
            //GER
            //mepa = new MEPAManager("/home/german/Escritorio/Compiladores/test/MEPA/codigoMEPA.mep");
            mepa = new MEPAManager(args[0].substring(0, args[0].length()-3).concat("mep"));

            AnalizadorSintactico sintactico = new AnalizadorSintactico(lexico, mepa, false);
            sintactico.program();
            System.out.println("Compilation successful");

        } catch (FileNotFoundException e) {
            // Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("File not found");
        } catch (IOException e) {
            System.err.println("IO Error");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Logger.getLogger(Compilador.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Please select file to compile");
        } catch (UnexpectedTokenException | UnexpectedCharException | UnopenedCommentException | UnclosedCommentException | IdentifierAlreadyDefinedException | IdentifierNotDefinedException | WrongTypeException | WrongConstructorException | WrongArgumentsException e) {
            System.err.println(e.getMessage());
            System.err.println("Compilation failed");
        }

    }
}
