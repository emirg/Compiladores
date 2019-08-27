public class AnalizadorSintactico {

    private AnalizadorLexico lexico;
    private Token ultimoToken;

    public AnalizadorSintactico(AnalizadorLexico lexico){
        this.lexico = lexico;
        this.ultimoToken = null;
    }

    public boolean match(Token t) throws UnexpectedToken{
        boolean tokenMatched=ultimoToken.equals(t);
        System.out.println("parametro    :"+t.getNombreToken());
        System.out.println("ultimo token :"+ultimoToken.getNombreToken());
        System.out.println(tokenMatched);
        if(tokenMatched){
            this.ultimoToken = lexico.obtenerToken();
            System.out.println(ultimoToken.getNombreToken());
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
        while(ultimoToken.equals(new Token("tk_var"))){
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedimiento"))) {
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
                if (!ultimoToken.equals(new Token("tk_end"))) {
                    bloque();
                }
                
            }
        }else{
            sentencia();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                if (!ultimoToken.equals(new Token("tk_end"))) {
                    bloque();
                }
            }
        }
    }

    public void sentencia() throws UnexpectedToken {
        switch(ultimoToken.getNombreToken()){
            // Asignacion y llamada a subprograma
            case "tk_id":
                //match(new Token("tk_id"));
                asignacion();
                break;

            // Alternativa
            case "tk_if":
                alternativa();
                break;
        
            // Repetitiva
            case "tk_while":
                repetitiva();    
                break; 
                
            // Leer
            case "tk_read":
                leer();
                break; 

            // Escribir
            case "tk_write":
                escribir();
                break;
            
            default:
                throw new UnexpectedToken("sentence");                   
        }
    }

    public void asignacion() throws UnexpectedToken {
        match(new Token("tk_id"));
        match(new Token("tk_asignacion"));
        expresion();
    }

    public void funcion() throws UnexpectedToken {
        match(new Token("tk_function"));
        match(new Token("tk_id"));
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        match(new Token("tk_tipo"));
        match(new Token("tk_puntocoma"));
        if(ultimoToken.equals(new Token("tk_var"))){
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedimiento"))) {
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
        match(new Token("tk_procedure"));
        match(new Token("tk_id"));
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        variables();
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedimiento"))) {
            if(ultimoToken.equals(new Token("tk_function"))){
                funcion();
            }else{
                procedimiento();
            }
        }
        sentenciaCompuesta();
        match(new Token("tk_dospuntos"));
    }

    public void variables() throws UnexpectedToken {
        if(ultimoToken.equals(new Token("tk_var"))){
            match(new Token("tk_var"));
            listaIdentificadores();
        }
    }

    public void params() throws UnexpectedToken{
        while(ultimoToken.getNombreToken()=="tk_id"){
            match(new Token("tk_id"));
            listaIdentificadores();
        }
    }

    public void listaIdentificadores() throws UnexpectedToken {
        match(new Token("tk_id"));
        while(ultimoToken.equals(new Token("tk_coma"))){
            match(new Token("tk_coma"));
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

    public void alternativaAux() throws UnexpectedToken{
        if(ultimoToken.equals(new Token("tk_else"))){
            match(new Token("tk_else"));
            bloque();
        }
    }

    public void repetitiva() throws UnexpectedToken {
        match(new Token("tk_while"));
        expresion();
        match(new Token("tk_do"));
        bloque();
    }

    public void llamadaSub() throws UnexpectedToken{
        match(new Token("tk_id")); // no tenemos metodo identificador?
        if(ultimoToken.equals(new Token("tk_parentesis_izq"))){
            match(new Token("tk_parentesis_izq"));
            expresion();
            while(ultimoToken.equals(new Token("tk_coma"))){
                match(new Token("tk_coma"));
                expresion();
            }
            match(new Token("tk_parentesis_der"));
        }
    }

    public void leer() throws UnexpectedToken{
        match(new Token("tk_read"));
        match(new Token("tk_parentesis_izq"));
        match(new Token("tk_id"));
        match(new Token("tk_parentesis_der"));
    }

    public void escribir() throws UnexpectedToken{
        match(new Token("tk_write"));
        match(new Token("tk_parentesis_izq"));
        match(new Token("tk_id"));
        match(new Token("tk_parentesis_der"));
    }

    public void sentenciaCompuesta() throws UnexpectedToken {
        match(new Token("tk_begin"));
        bloque();
        match(new Token("tk_end"));
    }

    public void expresion() throws UnexpectedToken{
        expresion_1();
        expresionAux();
    }

    public void expresionAux() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_or"))) {
            match(new Token("tk_op_or"));
            expresion_1();
            expresionAux();
        }
    }

    public void expresion_1() throws UnexpectedToken{
        expresion_2();
        expresion_1_aux();
    }

    public void expresion_1_aux() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_and"))) {
            match(new Token("tk_op_and"));
            expresion_2();
            expresion_1_aux();
        }
    }

    public void expresion_2() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_not"))) {
            match(new Token("tk_op_not"));
        }
        expresion_3();
    }

    public void expresion_3() throws UnexpectedToken{
        expresion_4();
        expresion_3_aux();
    }

    public void expresion_3_aux() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_relacional"))) {
            operador_comparacion();
            expresion_4();
            expresion_3_aux();
        }
    }

    public void expresion_4() throws UnexpectedToken{
        expresion_5();
        expresion_4_aux();
    }

    public void expresion_4_aux() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            expresion_5();
            expresion_4_aux();
        }else{
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                expresion_5();
                expresion_4_aux();
            }
        }
    }

    public void expresion_5() throws UnexpectedToken{
        expresion_6();
        expresion_5_aux();
    }

    public void expresion_5_aux() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_mult"))) {
            match(new Token("tk_op_mult"));
            expresion_6();
            expresion_5_aux();
        }else{
            if (ultimoToken.equals(new Token("tk_op_div"))) {
                match(new Token("tk_op_div"));
                expresion_6();
                expresion_5_aux();
            }
        }
    }

    public void expresion_6() throws UnexpectedToken{
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            factor();
        }else{
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                factor();
            }else{
                factor();
            }
        }
    }

    public void operador_comparacion() throws UnexpectedToken {
        switch (ultimoToken.getAtributoToken()) {
            case "op_igual":
                match(new Token("tk_op_relacional","op_igual"));
                break;

            case "op_mayor":
                match(new Token("tk_op_relacional","op_mayor"));
                break;
            
            case "op_menor":
                match(new Token("tk_op_relacional","op_menor"));
                break;

            case "op_distinto":
                match(new Token("tk_op_relacional","op_distinto")); 
                break;
            
            case "op_mayor_igual":
                match(new Token("tk_op_relacional","op_mayor_igual"));   
                break;

            case "op_menor_igual":
                match(new Token("tk_op_relacional","op_menor_igual"));  
                break;
        }
    }

    public void factor() throws UnexpectedToken{
        switch(ultimoToken.getNombreToken()){
            // Identificador o llamada subprograma
            case "tk_id":
                match(new Token("tk_id"));
                break;
            case "tk_numero":
                match(new Token("tk_numero"));
                break;
            case "tk_boolean":
                match(new Token("tk_boolean"));
                break;
            case "tk_parentesis_izq":
                match(new Token("tk_parentesis_izq"));
                expresion();
                match(new Token("tk_parentesis_der"));
                break;
        }
    }
	
}