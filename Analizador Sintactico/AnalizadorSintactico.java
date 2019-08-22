import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.rmi.UnexpectedException;
import java.util.HashMap;

import com.sun.tools.example.debug.expr.Token;

public class AnalizadorSintactico {

    private AnalizadorLexico lexico;
    private Token ultimoToken;

    public AnalizadorSintactico(AnalizadorLexico lexico){
        this.lexico = lexico;
        this.ultimoToken = null;
    }

    public boolean match(Token t) throws UnexpectedToken{
        boolean tokenMatched= ultimoToken.equals(t);
        if(tokenMatched){
            this.ultimoToken = lexico.obtenerToken();
        }else{
            // ERROR - Throw Exception
            String tokenEsperado = t.getAtributoToken();
            if (tokenEsperado.equals("")) {
                tokenEsperado = t.getNombreToken();
            }
            throw new UnexpectedToken(s);
        }
        return tokenMatched;
    }

    public void program(){
        this.ultimoToken = lexico.obtenerToken();
        match(new Token("tk_program"));
        match(new Token("tk_id"));
        match(new Token("tk_puntocoma"));
        variables();
        while (!match(new Token("tk_begin"))) {
            funcion();
            procedimiento();
        }
        bloque();
        while (!match(new Token("tk_end"))) {
            match(new Token("tk_puntocoma"));
            bloque();
        }
        match(new Token("tk_punto"));
    }

    public void bloque(){
        sentencia();
        
        while (match(new Token("tk_puntocoma"))) {
            bloque();
        }
        sentenciaCompuesta();
        while (condition) {
            
        }

        
    }

    public void sentencia(){

    }

    public void asignacion(){

    }

    public void funcion(){

    }

    public void procedimiento(){
        
    }

    public void identificador(){

    }

    public void variables(){
        while (match(new Token("tk_var"))) {
            listaIdentificadores();
        }

    }

    public void params(){

    }

    public void listaIdentificadores(){

    }

    public void alternativa(){

    }

    public void alternativaAux(){

    }

    public void repetitiva(){

    }

    public void llamadaSub(){

    }

    public void leer(){

    }

    public void escribir(){

    }

    public void sentenciaCompuesta(){

    }
	
}