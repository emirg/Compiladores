import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AnalizadorSintactico {

    private AnalizadorLexico lexico;
    private Token ultimoToken;

    public AnalizadorSintactico(AnalizadorLexico lexico){
        this.lexico = lexico;
        this.ultimoToken = null;
    }

    public void match(Token t){
        if(ultimoToken.equals(t)){
            this.ultimoToken = lexico.obtenerToken();
        }else{
            // ERROR - Throw Exception
        }
        
    }

    public void program(){
        this.ultimoToken = lexico.obtenerToken();
        match(new Token("tk_program"));
        match(new Token("tk_id"));
        match(new Token("tk_puntocoma"));
        variables();
    }

    public void bloque(){
        
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