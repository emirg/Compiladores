package AnalizadorSintactico;

import AnalizadorLexico.Token;
import AnalizadorLexico.AnalizadorLexico;
import AnalizadorSemantico.ElementosTS.Fila;
import AnalizadorSemantico.ElementosTS.FilaFuncion;
import AnalizadorSemantico.ElementosTS.FilaProcedimiento;
import AnalizadorSemantico.ElementosTS.FilaVariable;
import AnalizadorSemantico.TablaSimbolo;
import Exceptions.IdentifierAlreadyDefinedException;
import Exceptions.IdentifierNotDefinedException;
import Exceptions.UnclosedCommentException;
import Exceptions.UnexpectedChar;
import Exceptions.UnexpectedToken;
import Exceptions.UnopenedCommentException;
import Exceptions.WrongConstructorException;
import Exceptions.WrongTypeException;
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

    public void program() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
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

    public void bloque() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
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

    public void sentencia() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        switch (ultimoToken.getNombreToken()) {
            // Asignacion y llamada a subprograma
            case "tk_id":
                // match(new Token("tk_id")); // no respeta la gramatica pero quitarlo complica el if
                // el control con respecto a los parametros se debe hacer aca porque pierdo el nombre de 
                // la funcion o procedemiento
                //System.out.println("ultimo token " + ultimoToken.getAtributoToken());
                Fila simbolo = this.tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
                if (simbolo != null) {
                    System.out.println("simbolo " + simbolo.getTipoConstructor());
                    if (simbolo.getTipoConstructor().equalsIgnoreCase("function")) {
                        Fila simboloRetorno = this.tablasSimbolo.peek().obtenerSimbolo("retorno");

                        if (simboloRetorno != null && simbolo.getNombre().equalsIgnoreCase(simboloRetorno.getNombre())) {
                            //System.out.println("simbolo retorno " + simboloRetorno.getNombre());
                            //System.out.println("entre llamada sub funcion retorno");
                            asignacion();
                        } else {
                            //System.out.println("entre llamada sub funcion");
                            FilaProcedimiento funcion = (FilaProcedimiento) simbolo;
                            System.out.println("parametros " + funcion.getListaParametros().size());
                            llamadaSub();
                        }
                    } else {
                        if (simbolo.getTipoConstructor().equalsIgnoreCase("procedure")) {
                            //System.out.println("entre llamada sub procedure");
                            llamadaSub();
                        } else {

                            //System.out.println("entre asig");
                            asignacion();
                        }
                    }
                } else {
                    throw new IdentifierNotDefinedException(ultimoToken.getAtributoToken(), alcanceActual);
                }
                //try {
                //    asignacion();
                //} catch (UnexpectedToken e) {
                //    llamadaSub();
                //}
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

    public void asignacion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        Token identificador = match(new Token("tk_id"));
        match(new Token("tk_asignacion"));
        String tipo = expresion();
        Fila simbolo = this.tablasSimbolo.peek().obtenerSimbolo(identificador.getAtributoToken());
        // este simbolo == null ya no cumple la funcion de anticipar el retorno
        // ya que se carga la funcion en la TS 
        if (simbolo == null) {
            //simbolo = this.tablasSimbolo.peek().obtenerSimbolo("retorno");
            //if (simbolo == null || !simbolo.getNombre().equalsIgnoreCase(identificador.getNombreToken())) {
            throw new IdentifierNotDefinedException(identificador.getAtributoToken(), lexico.obtenerNumeroLinea());
            //} else {
            //if (simbolo.getTipoConstructor().equalsIgnoreCase("var")) {
            // if (!((FilaVariable) simbolo).getTipo().equalsIgnoreCase(tipo)) {
            //   throw new WrongTypeException(((FilaVariable) simbolo).getTipo(), lexico.obtenerNumeroLinea());
            // }
            // }
            // }
        } else {
            if (simbolo.getTipoConstructor().equalsIgnoreCase("var")) {
                if (!((FilaVariable) simbolo).getTipo().equalsIgnoreCase(tipo)) {
                    throw new WrongTypeException(((FilaVariable) simbolo).getTipo(), lexico.obtenerNumeroLinea());
                }
            } else {
                Fila simboloRetorno = this.tablasSimbolo.peek().obtenerSimbolo("retorno");
                if (simboloRetorno != null) {
                    if (simbolo.getNombre().equalsIgnoreCase(simboloRetorno.getNombre())) {
                        if (!((FilaVariable) simboloRetorno).getTipo().equalsIgnoreCase(tipo)) {
                            throw new WrongTypeException(((FilaVariable) simboloRetorno).getTipo(), lexico.obtenerNumeroLinea());
                        }
                    } else {
                        throw new WrongConstructorException(simbolo.getTipoConstructor(), lexico.obtenerNumeroLinea());
                    }
                } else {
                    // si en una funcion o procedimiento seria incorrecto asignarle un valor
                    // se podria plantear otro tipo de excepcion o agregar que se esperaba un var
                    throw new WrongConstructorException(simbolo.getTipoConstructor(), lexico.obtenerNumeroLinea());
                }
            }
        }
    }

    public void funcion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        match(new Token("tk_function"));

        // Guarda los datos para agregarlos a la tabla de simbolos 
        Token nuevaFuncion = match(new Token("tk_id"));
        ArrayList parametros = new ArrayList();

        /* los parenteisis son condicionales? o solo los parametros internos  */
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            parametros.addAll(params());
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        Token tipo = match(new Token("tk_tipo"));
        match(new Token("tk_puntocoma"));

        // Agrega el funcion a la tabla de simbolos junto con sus parametros
        this.tablasSimbolo.peek().agregarSimbolo(nuevaFuncion.getAtributoToken(), new FilaFuncion("function", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, tipo.getAtributoToken()));

        // Agrega una nueva tabla de simbolos para entrar en un nuevo alcance
        this.tablasSimbolo.add(new TablaSimbolo());

        // Agrega funcion a la nueva tabla para recursividad
        this.tablasSimbolo.peek().agregarSimbolo(nuevaFuncion.getAtributoToken(), new FilaFuncion("function", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, tipo.getAtributoToken()));

        // Agrega a la tabla para retorno funcion
        this.tablasSimbolo.peek().agregarSimbolo("retorno", new FilaVariable("var", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), tipo.getAtributoToken(), false));

        // Agrega los parametros como identificadores dentro del nuevo alcance
        this.tablasSimbolo.peek().agregarColeccionSimbolos(parametros);

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
        //System.out.println("fin funcion");
        match(new Token("tk_puntocoma"));
    }

    public void procedimiento() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
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

        // Agrega funcion a la nueva tabla para recursividad
        this.tablasSimbolo.peek().agregarSimbolo(nuevoProcedimiento.getAtributoToken(), new FilaProcedimiento("procedure", nuevoProcedimiento.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros));

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
        ArrayList nuevosParametros = new ArrayList();

        nuevosParametros.addAll(listaIdentificadores(true));
        ArrayList parametrosAux = paramsAux();
        if (!contieneIdentificador(nuevosParametros, parametrosAux)) {
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
        } else if (esParametro) {
            nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", esParametro));

        } else {
            throw new IdentifierAlreadyDefinedException(nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea());

        }

        while (ultimoToken.equals(new Token("tk_coma"))) {
            match(new Token("tk_coma"));
            nuevoIdentificador = match(new Token("tk_id"));
            if (!this.tablasSimbolo.peek().existeSimbolo(nuevoIdentificador.getAtributoToken()) && !nuevosIdentificadores.contains(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", esParametro))) {
                nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", esParametro));
            } else {
                throw new IdentifierAlreadyDefinedException(nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea());
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

    public void alternativa() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        match(new Token("tk_if"));
        expresion();
        match(new Token("tk_then"));
        bloque();
        alternativaAux();
    }

    public void alternativaAux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        if (ultimoToken.equals(new Token("tk_else"))) {
            match(new Token("tk_else"));
            bloque();
        }
    }

    public void repetitiva() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        match(new Token("tk_while"));
        expresion();
        match(new Token("tk_do"));
        bloque();
    }

    public void llamadaSub() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        Fila simbolo = this.tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
        FilaProcedimiento filaOprocedimiento = (FilaProcedimiento) simbolo;
        int cantParametros = 0;
        String tipo = "";
        match(new Token("tk_id")); // segun gramatica se haria aca pero por codigo se hace antes 
        // falta tener en cuanta cantidad de parametros y tipo
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            System.out.println("ultimo " + ultimoToken.getNombreToken());
            //paramatros de llamada
            FilaVariable parametrollamada = (FilaVariable) this.tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
            //System.out.println("paramtro " + parametrollamada);
            expresion();
            //paramametros guardados en TS
            FilaVariable parametro = (FilaVariable) filaOprocedimiento.getListaParametros().get(cantParametros);
            tipo = parametro.getTipo();
            System.out.println("Nombre var definida <" + parametro.getNombre() + "> tipo esperado " + tipo);
            if (parametrollamada != null) {
                System.out.println("parametro llamada <" + parametrollamada.getNombre() + "> tipo " + parametrollamada.getTipo());
                if (!parametrollamada.getTipo().equalsIgnoreCase(tipo)) {
                    throw new WrongTypeException(tipo, lexico.obtenerNumeroLinea());
                }
            } else {
                //tipo boolean o integer puro no guardado 
                System.out.println("entre por tipo puro");
                if (!ultimoToken.getNombreToken().equalsIgnoreCase("tk_boolean") && !tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException(tipo, lexico.obtenerNumeroLinea());
                }
            }
            while (ultimoToken.equals(new Token("tk_coma"))) {
                if (cantParametros < filaOprocedimiento.getListaParametros().size()-1) {
                    match(new Token("tk_coma"));
                    //simbolo = this.tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
                    parametrollamada = (FilaVariable) this.tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
                    System.out.println("parametro llamada <" + parametrollamada.getNombre() + "> tipo " + parametrollamada.getTipo());
                    expresion();
                    parametro = (FilaVariable) filaOprocedimiento.getListaParametros().get(cantParametros);
                    tipo = parametro.getTipo();
                    System.out.println("tipo parametro en TS" + tipo);
                    if (parametrollamada != null) {
                        System.out.println("parametro llamada " + parametrollamada.getNombre() + " tipo " + parametrollamada.getTipo());
                        if (!parametrollamada.getTipo().equalsIgnoreCase(tipo)) {
                            throw new WrongTypeException(tipo, lexico.obtenerNumeroLinea());
                        }
                    } else {
                        if (!ultimoToken.getNombreToken().equalsIgnoreCase("tk_boolean") && !tipo.equalsIgnoreCase("tipo_boolean")) {
                            throw new WrongTypeException(tipo, lexico.obtenerNumeroLinea());
                        }
                    }

                } else {
                    //si la cantidad de parametros es erronea
                    throw new UnexpectedToken("tk_parentesis_der", ultimoToken.getAtributoToken(), lexico.obtenerNumeroLinea());
                }
                cantParametros++;
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

    public void sentenciaCompuesta() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        match(new Token("tk_begin"));
        bloque();
        /* modificacion reciente de la gramatica*/
        if (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
        }
        match(new Token("tk_end"));
    }

    public String expresion() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = expresion_1();
        String tipoAux = expresionAux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;
    }

    public String expresionAux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_or"))) {
            match(new Token("tk_op_or"));
            expresion_1();
            expresionAux();
            if (tipo.equalsIgnoreCase("tipo_integer")) {
                throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

            }
        }
        return tipo;
    }

    public String expresion_1() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = expresion_2();
        String tipoAux = expresion_1_aux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;

    }

    public String expresion_1_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        String tipoAux = "";
        if (ultimoToken.equals(new Token("tk_op_and"))) {
            match(new Token("tk_op_and"));
            tipo = expresion_2();
            expresion_1_aux();
            if (tipo.equalsIgnoreCase("tipo_integer")) {
                throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

            }
        }
        return tipo;
    }

    public String expresion_2() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        String tipoAux = "";
        if (ultimoToken.equals(new Token("tk_op_not"))) {
            match(new Token("tk_op_not"));
            tipoAux = "tipo_boolean";
        }
        tipo = expresion_3();
        if (!tipoAux.equals("") && !tipo.equalsIgnoreCase(tipoAux)) {
            throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

        }
        return tipo;
    }

    public String expresion_3() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = expresion_4();
        //System.out.println("exp_4 " + tipo);
        String tipoAux = expresion_3_aux();
        //System.out.println("exp_3AUX " + tipoAux);
        if (!tipoAux.equals("")) {
            if (tipo.equalsIgnoreCase(tipoAux)) {
                //tipo = tipoAux;
                tipo = "tipo_boolean";
            } else {
                throw new WrongTypeException(tipoAux, alcanceActual);
            }
        }
        return tipo;
    }

    public String expresion_3_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_relacional"))) {
            operador_comparacion();
            tipo = expresion_4();
            // hacer case para los diferentes op_realaciones
            //if (tipo.equalsIgnoreCase("tipo_boolean")) {
            //    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            //}

            //tipo = expresion_3_aux();
            //tipo = "tipo_boolean";
            //System.out.println("tipo "+tipo);
        }
        return tipo;
    }

    public String expresion_4() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = expresion_5();
        String tipoAux = expresion_4_aux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;
    }

    public String expresion_4_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            tipo = expresion_5();
            if (tipo.equalsIgnoreCase("tipo_boolean")) {
                throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            }
            expresion_4_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                tipo = expresion_5();
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
                expresion_4_aux();
            }
        }
        return tipo;
    }

    public String expresion_5() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = expresion_6();
        String tipoAux = expresion_5_aux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;
    }

    public String expresion_5_aux() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_mult"))) {
            match(new Token("tk_op_mult"));
            tipo = expresion_6();
            if (tipo.equalsIgnoreCase("tipo_boolean")) {
                throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            }
            expresion_5_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_div"))) {
                match(new Token("tk_op_div"));
                tipo = expresion_6();
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
                expresion_5_aux();
            }
        }
        return tipo;
    }

    public String expresion_6() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo;
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            tipo = factor();
            if (tipo.equalsIgnoreCase("tipo_boolean")) {
                throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            }
        } else {
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                tipo = factor();
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
            } else {
                tipo = factor();
            }
        }
        return tipo;
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

    public String factor() throws UnexpectedToken, UnexpectedChar, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException {
        String tipo = "";
        switch (ultimoToken.getNombreToken()) {
            // Identificador o llamada subprograma
            case "tk_id":
                //Token identificador = match(new Token("tk_id"));
                if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
                    llamadaSub();
                    //problemas cuando se llama una funcion o precedimiento dentro de otra
                    FilaFuncion fila = (FilaFuncion) tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
                    if (fila != null) {
                        tipo = fila.getTipoRetorno();
                    } else {
                        throw new IdentifierNotDefinedException(ultimoToken.getAtributoToken(), lexico.obtenerNumeroLinea());
                    }

                } else {
                    Fila fila = tablasSimbolo.peek().obtenerSimbolo(ultimoToken.getAtributoToken());
                    if (fila != null) {
                        //llamada a funcion sin parametros 
                        if (fila.getTipoConstructor().equalsIgnoreCase("function")) {
                            llamadaSub();
                            tipo = ((FilaFuncion) fila).getTipoRetorno();
                        } else if (fila.getTipoConstructor().equalsIgnoreCase("var")) {
                            match(new Token("tk_id"));
                            tipo = ((FilaVariable) fila).getTipo();
                        } else {
                            throw new WrongConstructorException(fila.getTipoConstructor(), lexico.obtenerNumeroLinea());
                        }
                    } else {
                        throw new IdentifierNotDefinedException(ultimoToken.getAtributoToken(), lexico.obtenerNumeroLinea());
                    }

                }
                break;
            case "tk_numero":
                match(new Token("tk_numero"));
                tipo = "tipo_integer";
                break;
            case "tk_boolean":
                match(new Token("tk_boolean"));
                tipo = "tipo_boolean";
                break;
            case "tk_parentesis_izq":
                match(new Token("tk_parentesis_izq"));
                tipo = expresion();
                match(new Token("tk_parentesis_der"));
                break;
            default:
                String tokenEncontrado = ultimoToken.getAtributoToken();
                if (tokenEncontrado.equals("")) {
                    tokenEncontrado = ultimoToken.getNombreToken();
                }

                throw new UnexpectedToken("factor", this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
        return tipo;
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

    private boolean contieneIdentificador(ArrayList<FilaVariable> parametros, ArrayList identificadores) throws IdentifierAlreadyDefinedException {
        boolean contiene = false;
        int i = 0;
        while (!contiene && parametros.size() > i) {
            contiene = identificadores.contains(parametros.get(i));
            if (contiene) {
                throw new IdentifierAlreadyDefinedException(parametros.get(i).getNombre(), lexico.obtenerNumeroLinea());
            }
            i++;
        }
        return contiene;
    }

}
