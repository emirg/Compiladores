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
            throw new UnexpectedToken(tokenEsperado);
        }
        return tokenMatched;
    }

    public void program() throws UnexpectedToken {
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

    public void bloque() throws UnexpectedToken {
        if(ultimoToken.equals(new Token("tk_begin"))){
            sentenciaCompuesta();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                bloque();
            }
        }else{
            sentencia();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                bloque();
            }
        }
    }

    public void sentencia() throws UnexpectedToken {
        switch(ultimoToken.getNombreToken()){
            // Asignacion y llamada a subprograma
            case "tk_id":
                match(new Token("tk_id"));
                break;

            // Alternativa
            case "tk_if":
                alternativa();
                break;
        
            // Repetitiva
            case "tk_while":
                break; 
            
            // Leer
            case "tk_read":
                break; 
            
            // Escribir
            case "tk_write":
                break;
            
            default:                     
        }
    }

    public void asignacion(){

    }

    public void funcion() throws UnexpectedToken {
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

    public void procedimiento() throws UnexpectedToken {
        match(new Token("procedure"));
        identificador();
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        variables();
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

    public void variables() throws UnexpectedToken {
        if(ultimoToken.equals(new Token("tk_var"))){
            match(new Token("tk_var"));
            listaIdentificadores();
        }
    }

    public void params(){
        

    }

    public void listaIdentificadores() throws UnexpectedToken {
        match(new Token("tk_id"));
        while(ultimoToken.equals(new Token("tk_puntocoma"))){
            match(new Token("tk_puntocoma"));
            match(new Token("tk_id"));
        }
        match(new Token("tk_dospuntos"));
        // tipo();
        match(new Token("tk_tipo"));
        match(new Token("tk_puntocoma"));
    }

    public void alternativa() throws UnexpectedToken {
        match(new Token("tk_if"));
        expresion();
        match(new Token("tk_then"));
        bloque();
        alternativaAux();
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

    public void sentenciaCompuesta() throws UnexpectedToken {
        match(new Token("tk_begin"));
        bloque();
        match(new Token("tk_end"));
    }

    public void expresion(){

    }

    public void tipo(){
        
    }
	
}