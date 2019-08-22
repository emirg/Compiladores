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
        if(ultimoToken.equals(new Token("tk_var"))){
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function"))||ultimoToken.equals(new Token("tk_procedimiento"))) {
            if(ultimoToken.equals(new Token("tk_function"))){
                funcion();
            }else{
                procedimiento();
            }
        }

        match(new Token("tk_begin"));
        bloque(); 
        while(ultimoToken.equals(new Token("tk_puntocoma"))){
            match(new Token("tk_puntocoma"));
            bloque();
        }
        match(new Token("tk_end"));
        match(new Token("tk_punto"));
    }

    public void bloque(){
        switch(ultimoToken.getNombreToken()){
            case "tk_id":
                break;
            
            case "tk_if":
                break;

            case "tk_while"
        
        }
        
            sentencia();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                bloque();
            }
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
        match(new Token("tk_function"));
        identificador();
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        tipo();
        match(new Token("tk_puntocoma"));
        if(ultimoToken.equals(new Token("tk_var"))){
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function"))||ultimoToken.equals(new Token("tk_procedimiento"))) {
            if(ultimoToken.equals(new Token("tk_function"))){
                funcion();
            }else{
                procedimiento();
            }
        }
        sentenciaCompuesta();
        match(new Token("tk_puntocoma"));
    }

    public void procedimiento(){
        match(new Token("procedure"));
        identificador();
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        variable();
        while (ultimoToken.equals(new Token("tk_function"))||ultimoToken.equals(new Token("tk_procedimiento"))) {
            if(ultimoToken.equals(new Token("tk_function"))){
                funcion();
            }else{
                procedimiento();
            }
        }
        sentenciaCompuesta();
        match(new Token("tk_dospuntos"));
    }

    public void identificador(){

    }

    public void variables(){
        if(ultimoToken.equals(new Token("tk_var"))){
            match(new Token("tk_var"));
            listaIdentificadores();
        }

            
        

    }

    public void params(){
        

    }

    public void listaIdentificadores(){
        match(new Token("tk_id"));
        while(ultimoToken.equals(new Token("tk_puntocoma"))){
            match(new Token("tk_puntocoma"));
            match(new Token("tk_id"));
        }
        match(new Token("tk_dospuntos"));
        tipo();
        match(new Token("tk_puntocoma"));
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