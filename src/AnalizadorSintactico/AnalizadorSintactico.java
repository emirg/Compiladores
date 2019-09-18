package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSemantico.ElementosTS.Fila;
import AnalizadorSemantico.ElementosTS.FilaProcedimiento;
import AnalizadorSemantico.ElementosTS.FilaVariable;
import AnalizadorSemantico.TablaSimbolo;
import Exceptions.IdentifierAlreadyDefinedException;
import Exceptions.UnclosedCommentException;
import Exceptions.UnexpectedChar;
import Exceptions.UnexpectedToken;
import Exceptions.UnopenedCommentException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class AnalizadorSintactico {

    private final AnalizadorLexico lexico;
    private Stack<TablaSimbolo> tablasSimbolo;
    private HashMap<String, String> tablaNombresTokens; // Utilizado para los mensajes de errores
    private Token ultimoToken;
    private final boolean debugging; // Utilizada para mostrar mensajes de debugging
    private int alcanceActual;

    public AnalizadorSintactico(AnalizadorLexico lexico, boolean debugging) {
        this.lexico = lexico;
        this.ultimoToken = null;
        this.tablasSimbolo = new Stack();
        this.debugging = debugging;
        this.alcanceActual = 0;
        this.tablaNombresTokens = new HashMap();
        this.cargarNombresTokens();
    }

    public Token match(Token t) throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        boolean hasMatched = ultimoToken.equals(t);
        Token tokenMatched = ultimoToken.clone();

        if (debugging) {
            System.out.println("parametro    :" + t.getNombreToken());
            System.out.println("ultimo token :" + ultimoToken.getNombreToken());
            System.out.println(hasMatched);
        }

        if (hasMatched) {
            this.ultimoToken = lexico.obtenerToken();
            //System.out.println(ultimoToken.getNombreToken());
        } else {
            // ERROR - Throw Exception
            String tokenEsperado = t.getAtributoToken();
            if (tokenEsperado.equals("")) {
                tokenEsperado = t.getNombreToken();
            }

            String tokenEncontrado = ultimoToken.getAtributoToken();
            if (tokenEncontrado.equals("")) {
                tokenEncontrado = ultimoToken.getNombreToken();
            }

            throw new UnexpectedToken(this.tablaNombresTokens.getOrDefault(tokenEsperado, tokenEsperado), this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
        return tokenMatched;
    }

    public void program() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        this.ultimoToken = lexico.obtenerToken();
        match(new Token("tk_program"));

        // Inicializa la tabla de simbolos inicial
        this.tablasSimbolo.add(new TablaSimbolo());

        // Guarda el nombre del programa (identificador)
        Token nuevoIdentificador = match(new Token("tk_id"));
        this.tablasSimbolo.peek().agregarSimbolo(nuevoIdentificador.getAtributoToken(), new Fila("program", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea()));

        // this.tablasSimbolo.peek().agregarSimbolo("test", new Fila("test", "test", lexico.obtenerNumeroLinea()));
        match(new Token("tk_puntocoma"));
        if (ultimoToken.equals(new Token("tk_var"))) {
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedure"))) {
            if (ultimoToken.equals(new Token("tk_function"))) {
                funcion();
            } else {
                procedimiento();
            }
        }

        match(new Token("tk_begin"));
        bloque();
        while (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
            bloque();
        }
        if (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
        }
        match(new Token("tk_end"));
        match(new Token("tk_punto"));
    }

    public void bloque() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_begin"))) {
            sentenciaCompuesta();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                if (!ultimoToken.equals(new Token("tk_end"))) {
                    bloque();
                }
            }
        } else {
            sentencia();
            while (ultimoToken.equals(new Token("tk_puntocoma"))) {
                match(new Token("tk_puntocoma"));
                if (!ultimoToken.equals(new Token("tk_end"))) {
                    bloque();
                }
            }
        }
    }

    public void sentencia() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        switch (ultimoToken.getNombreToken()) {
            // Asignacion y llamada a subprograma
            case "tk_id":
                match(new Token("tk_id")); // no respeta la gramatica pero quitarlo complica el if
                if (ultimoToken.equals(new Token("tk_asignacion"))) {
                    asignacion();
                } else {
                    llamadaSub();
                }
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
                String tokenEncontrado = ultimoToken.getAtributoToken();
                if (tokenEncontrado.equals("")) {
                    tokenEncontrado = ultimoToken.getNombreToken();
                }

                throw new UnexpectedToken("sentence", this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
    }

    public void asignacion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        //match(new Token("tk_id"));
        match(new Token("tk_asignacion"));
        expresion();
    }

    public void funcion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        match(new Token("tk_function"));
        match(new Token("tk_id"));
        /* los parenteisis son condicionales? o solo los parametros internos  */
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            params();
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        match(new Token("tk_tipo"));
        match(new Token("tk_puntocoma"));
        if (ultimoToken.equals(new Token("tk_var"))) {
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedimiento"))) {
            if (ultimoToken.equals(new Token("tk_function"))) {
                funcion();
            } else {
                procedimiento();
            }
        }
        sentenciaCompuesta();
        match(new Token("tk_puntocoma"));
    }

    public void procedimiento() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        match(new Token("tk_procedure"));

        // Guarda los datos para agregarlos a la tabla de simbolos
        Token nuevoProcedimiento = match(new Token("tk_id"));
        ArrayList parametros = new ArrayList();
        
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            parametros.addAll(params());
            match(new Token("tk_parentesis_der"));
        }
        
        // Agrega el procedimiento a la tabla de simbolos junto con sus parametros
        this.tablasSimbolo.peek().agregarSimbolo(nuevoProcedimiento.getAtributoToken(), new FilaProcedimiento("procedure", nuevoProcedimiento.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros));
        
        // Agrega una nueva tabla de simbolos para entrar en un nuevo alcance
        this.tablasSimbolo.add(new TablaSimbolo());
        
        // Agrega los parametros como identificadores dentro del nuevo alcance
        this.tablasSimbolo.peek().agregarColeccionSimbolos(parametros);
        
        match(new Token("tk_puntocoma"));
        /* la gramatica dice que es condicional y no lo estaba */
        if (ultimoToken.equals(new Token("tk_var"))) {
            variables();
        }
        while (ultimoToken.equals(new Token("tk_function")) || ultimoToken.equals(new Token("tk_procedimiento"))) {
            if (ultimoToken.equals(new Token("tk_function"))) {
                funcion();
            } else {
                procedimiento();
            }
        }
        sentenciaCompuesta();
        
        // Se termina el alcance por lo que se saca la tabla de simbolos de la pila
        this.tablasSimbolo.pop();
        match(new Token("tk_puntocoma"));
    }

    public void variables() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        if (ultimoToken.equals(new Token("tk_var"))) {
            match(new Token("tk_var"));
            do {
                listaIdentificadores(false);
                match(new Token("tk_puntocoma"));
            } while (ultimoToken.equals(new Token("tk_id")));

        }
    }

    public ArrayList params() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        /*while (ultimoToken.equals(new Token("tk_id"))) {
            listaIdentificadores();
        }*/
        ArrayList nuevosParametros = new ArrayList();
        if (ultimoToken.equals(new Token("tk_id"))) {
            nuevosParametros.addAll(listaIdentificadores(true));
            nuevosParametros.addAll(paramsAux());
        }
        return nuevosParametros;
    }

    public ArrayList paramsAux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        ArrayList nuevosParametros = new ArrayList();
        while (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
            nuevosParametros.addAll(listaIdentificadores(true));
        }
        
        return nuevosParametros;
    }

    public ArrayList listaIdentificadores(boolean esParametro) throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        ArrayList<FilaVariable> nuevosIdentificadores = new ArrayList();
        Token nuevoIdentificador = match(new Token("tk_id"));

        if (!this.tablasSimbolo.peek().existeSimbolo(nuevoIdentificador.getAtributoToken())) {
            nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", esParametro));
        }else{
            throw new IdentifierAlreadyDefinedException(nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea());
        }

        while (ultimoToken.equals(new Token("tk_coma"))) {
            match(new Token("tk_coma"));
            nuevoIdentificador = match(new Token("tk_id"));
            if (!this.tablasSimbolo.peek().existeSimbolo(nuevoIdentificador.getAtributoToken())) {
                nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", esParametro));
            }

        }
        match(new Token("tk_dospuntos"));
        Token tipo = match(new Token("tk_tipo"));
        for (FilaVariable identificador : nuevosIdentificadores) {
            identificador.setTipo(tipo.getAtributoToken());
        }
        if (!esParametro) {
            this.tablasSimbolo.peek().agregarColeccionSimbolos(nuevosIdentificadores);
        }
        return nuevosIdentificadores;
    }

    public void alternativa() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_if"));
        expresion();
        match(new Token("tk_then"));
        bloque();
        alternativaAux();
    }

    public void alternativaAux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_else"))) {
            match(new Token("tk_else"));
            bloque();
        }
    }

    public void repetitiva() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_while"));
        expresion();
        match(new Token("tk_do"));
        bloque();
    }

    public void llamadaSub() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        //match(new Token("tk_id")); // segun gramatica se haria aca pero por codigo se hace antes 
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            expresion();
            while (ultimoToken.equals(new Token("tk_coma"))) {
                match(new Token("tk_coma"));
                expresion();
            }
            match(new Token("tk_parentesis_der"));
        }
    }

    public void leer() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_read"));
        match(new Token("tk_parentesis_izq"));
        match(new Token("tk_id"));
        match(new Token("tk_parentesis_der"));
    }

    public void escribir() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_write"));
        match(new Token("tk_parentesis_izq"));
        match(new Token("tk_id")); // TODO: Agregar las modificaciones hechas en la gramática
        match(new Token("tk_parentesis_der"));
    }

    public void sentenciaCompuesta() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_begin"));
        bloque();
        /* modificacion reciente de la gramatica*/
        if (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
        }
        match(new Token("tk_end"));
    }

    public void expresion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        expresion_1();
        expresionAux();
    }

    public void expresionAux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_or"))) {
            match(new Token("tk_op_or"));
            expresion_1();
            expresionAux();
        }
    }

    public void expresion_1() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        expresion_2();
        expresion_1_aux();
    }

    public void expresion_1_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_and"))) {
            match(new Token("tk_op_and"));
            expresion_2();
            expresion_1_aux();
        }
    }

    public void expresion_2() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_not"))) {
            match(new Token("tk_op_not"));
        }
        expresion_3();
    }

    public void expresion_3() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        expresion_4();
        expresion_3_aux();
    }

    public void expresion_3_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_relacional"))) {
            operador_comparacion();
            expresion_4();
            expresion_3_aux();
        }
    }

    public void expresion_4() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        expresion_5();
        expresion_4_aux();
    }

    public void expresion_4_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            expresion_5();
            expresion_4_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                expresion_5();
                expresion_4_aux();
            }
        }
    }

    public void expresion_5() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        expresion_6();
        expresion_5_aux();
    }

    public void expresion_5_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_mult"))) {
            match(new Token("tk_op_mult"));
            expresion_6();
            expresion_5_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_div"))) {
                match(new Token("tk_op_div"));
                expresion_6();
                expresion_5_aux();
            }
        }
    }

    public void expresion_6() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            factor();
        } else {
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                factor();
            } else {
                factor();
            }
        }
    }

    public void operador_comparacion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        switch (ultimoToken.getAtributoToken()) {
            case "op_igual":
                match(new Token("tk_op_relacional", "op_igual"));
                break;

            case "op_mayor":
                match(new Token("tk_op_relacional", "op_mayor"));
                break;

            case "op_menor":
                match(new Token("tk_op_relacional", "op_menor"));
                break;

            case "op_distinto":
                match(new Token("tk_op_relacional", "op_distinto"));
                break;

            case "op_mayor_igual":
                match(new Token("tk_op_relacional", "op_mayor_igual"));
                break;

            case "op_menor_igual":
                match(new Token("tk_op_relacional", "op_menor_igual"));
                break;

            default:
                String tokenEncontrado = ultimoToken.getAtributoToken();
                if (tokenEncontrado.equals("")) {
                    tokenEncontrado = ultimoToken.getNombreToken();
                }

                throw new UnexpectedToken("comparison operator", tokenEncontrado, lexico.obtenerNumeroLinea());

        }
    }

    public void factor() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException {
        switch (ultimoToken.getNombreToken()) {
            // Identificador o llamada subprograma
            case "tk_id":
                match(new Token("tk_id"));
                if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
                    llamadaSub();
                }
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
            default:
                String tokenEncontrado = ultimoToken.getAtributoToken();
                if (tokenEncontrado.equals("")) {
                    tokenEncontrado = ultimoToken.getNombreToken();
                }

                throw new UnexpectedToken("factor", this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
    }

    private void cargarNombresTokens() {
        // Palabras reservadas
        tablaNombresTokens.put("tk_op_and", "and");
        tablaNombresTokens.put("tk_op_or", "or");
        tablaNombresTokens.put("tk_op_not", "not");
        tablaNombresTokens.put("tk_program", "program");
        tablaNombresTokens.put("tk_begin", "begin");
        tablaNombresTokens.put("tk_end", "end");
        tablaNombresTokens.put("tk_function", "function");
        tablaNombresTokens.put("tk_procedure", "procedure");
        tablaNombresTokens.put("tk_var", "var");
        tablaNombresTokens.put("tk_if", "if");
        tablaNombresTokens.put("tk_then", "then");
        tablaNombresTokens.put("tk_else", "else");
        tablaNombresTokens.put("tk_while", "while");
        tablaNombresTokens.put("tk_do", "do");
        tablaNombresTokens.put("tk_read", "read");
        tablaNombresTokens.put("tk_write", "write");
        tablaNombresTokens.put("tipo_boolean", "boolean");
        tablaNombresTokens.put("tipo_integer", "integer");
        tablaNombresTokens.put("valor_true", "true");
        tablaNombresTokens.put("valor_false", "false");

        // Resto de tokens
        tablaNombresTokens.put("tk_coma", ",");
        tablaNombresTokens.put("tk_puntocoma", ";");
        tablaNombresTokens.put("tk_dospuntos", ":");
        tablaNombresTokens.put("tk_puntocoma", ";");
        tablaNombresTokens.put("tk_asignacion", ":=");
        tablaNombresTokens.put("tk_punto", ".");
        tablaNombresTokens.put("tk_parentesis_izq", "(");
        tablaNombresTokens.put("tk_parentesis_der", ")");
        tablaNombresTokens.put("tk_op_resta", "-");
        tablaNombresTokens.put("tk_op_suma", "+");
        tablaNombresTokens.put("tk_op_mult", "*");
        tablaNombresTokens.put("tk_op_div", "/");
        tablaNombresTokens.put("op_mayor", ">");
        tablaNombresTokens.put("op_mayor_igual", ">=");
        tablaNombresTokens.put("op_menor_igual", "<=");
        tablaNombresTokens.put("op_menor", "<");
        tablaNombresTokens.put("op_distinto", "<>");
        tablaNombresTokens.put("op_igual", "=");
        tablaNombresTokens.put("tk_id", "identifier");
    }

}
