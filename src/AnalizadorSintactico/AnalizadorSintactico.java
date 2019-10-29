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
import Exceptions.UnexpectedCharException;
import Exceptions.UnexpectedTokenException;
import Exceptions.UnopenedCommentException;
import Exceptions.WrongArgumentsException;
import Exceptions.WrongConstructorException;
import Exceptions.WrongTypeException;
import MEPA.MEPAManager;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class AnalizadorSintactico {

    private final AnalizadorLexico lexico;
    private Stack<TablaSimbolo> tablasSimbolo;
    private HashMap<String, String> tablaNombresTokens; // Utilizado para los mensajes de errores
    private Token ultimoToken;
    private final boolean debugging; // Utilizada para mostrar mensajes de debugging
    private final MEPAManager mepaManager;
    private int nivel;
    private int labelCounter;
    private int offset;
    private int offsetParams;

    public AnalizadorSintactico(AnalizadorLexico lexico, MEPAManager mepa, boolean debugging) throws IOException {
        this.lexico = lexico;
        this.ultimoToken = null;
        this.tablasSimbolo = new Stack();
        this.debugging = debugging;
        this.tablaNombresTokens = new HashMap();
        this.cargarNombresTokens();
        this.mepaManager = mepa;
        this.nivel = 0;
        this.labelCounter = 1;
        this.offset = 0;
        this.offsetParams = -3;
    }

    public Token match(Token t) throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException {
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

            throw new UnexpectedTokenException(this.tablaNombresTokens.getOrDefault(tokenEsperado, tokenEsperado), this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
        return tokenMatched;
    }

    public void program() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        this.ultimoToken = lexico.obtenerToken();
        match(new Token("tk_program"));
        // Inicializa la tabla de simbolos inicial
        this.tablasSimbolo.add(new TablaSimbolo());

        // Guarda el nombre del programa (identificador)
        Token nuevoIdentificador = match(new Token("tk_id"));
        this.tablasSimbolo.peek().agregarSimbolo(nuevoIdentificador.getAtributoToken(), new Fila("program", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "l" + labelCounter++));

        // this.tablasSimbolo.peek().agregarSimbolo("test", new Fila("test", "test", lexico.obtenerNumeroLinea()));
        match(new Token("tk_puntocoma"));

        this.mepaManager.INPP();

        if (ultimoToken.equals(new Token("tk_var"))) {
            variables();
        }

        this.mepaManager.DSVS(tablasSimbolo.peek().obtenerSimbolo(nuevoIdentificador.getAtributoToken()).getLabel()); // TODO: Definir como se trabajan los labels

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
        this.mepaManager.PARA();
        this.mepaManager.closeWriter();
    }

    public void bloque() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
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

    public void sentencia() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        switch (ultimoToken.getNombreToken()) {
            // Asignacion y llamada a subprograma
            case "tk_id":
                Token identificador = match(new Token("tk_id"));
                Fila simbolo = this.obtenerIdentificador(identificador.getAtributoToken());
                if (simbolo != null) {
                    if (debugging) {
                        System.out.println("Simbolo " + simbolo.getTipoConstructor());
                    }
                    if (simbolo.getTipoConstructor().equalsIgnoreCase("function")) {
                        // Fila simboloRetorno = this.obtenerIdentificador("retorno");

                        //if (simboloRetorno != null) {
                        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
                           
                            llamadaSub(identificador);
                            this.mepaManager.LLPR(simbolo.getLabel());
                        } else {
                            asignacion(identificador);
                        }
                        //} else {
                        //  throw new IdentifierNotDefinedException(ultimoToken.getAtributoToken(), lexico.obtenerNumeroLinea());
                        //}
                    } else {
                        if (simbolo.getTipoConstructor().equalsIgnoreCase("procedure")) {
                            llamadaSub(identificador);
                        } else {
                            asignacion(identificador);
                        }
                    }
                } else {
                    throw new IdentifierNotDefinedException(ultimoToken.getAtributoToken(), lexico.obtenerNumeroLinea());
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

                throw new UnexpectedTokenException("sentence", this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
        }
    }

    public void asignacion(Token identificador) throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {

        match(new Token("tk_asignacion"));
        String tipo = expresion();
        Fila simbolo = this.obtenerIdentificador(identificador.getAtributoToken());
        // este simbolo == null ya no cumple la funcion de anticipar el retorno
        // ya que se carga la funcion en la TS 
        if (simbolo == null) {
            //simbolo = this.obtenerIdentificador("retorno");
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
                    throw new WrongTypeException(this.tablaNombresTokens.get(((FilaVariable) simbolo).getTipo()), lexico.obtenerNumeroLinea());
                } else {
                    this.mepaManager.ALVL(nivel, ((FilaVariable) simbolo).getOffset()); // TODO: Definir como obtener el offset
                }
            } else {
                Fila simboloRetorno = this.obtenerIdentificador("retorno");
                if (simboloRetorno != null) {
                    if (simbolo.getNombre().equalsIgnoreCase(simboloRetorno.getNombre())) {
                        if (!((FilaVariable) simboloRetorno).getTipo().equalsIgnoreCase(tipo)) {
                            throw new WrongTypeException(this.tablaNombresTokens.get(((FilaVariable) simboloRetorno).getTipo()), lexico.obtenerNumeroLinea());
                        } else {
                            this.mepaManager.ALVL(nivel, ((FilaVariable) simboloRetorno).getOffset()); // TODO: Definir como obtener el offset
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

    public void funcion() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_function"));
        this.nivel++;
        this.offset = 0;
        String label = "l" + this.labelCounter++;
        mepaManager.ENPR(label, nivel);

        // Guarda los datos para agregarlos a la tabla de simbolos 
        Token nuevaFuncion = match(new Token("tk_id"));
        ArrayList<FilaVariable> parametros = new ArrayList();

        /* los parenteisis son condicionales? o solo los parametros internos  */
        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            parametros.addAll(params());
            for (FilaVariable parametro : parametros) {
                String nombreParametro = parametro.getNombre();
                if (nombreParametro.equalsIgnoreCase(nuevaFuncion.getAtributoToken())) {
                    throw new IdentifierAlreadyDefinedException(nombreParametro, lexico.obtenerNumeroLinea());
                }
            }
            match(new Token("tk_parentesis_der"));
        }
        match(new Token("tk_dospuntos"));
        Token tipo = match(new Token("tk_tipo"));
        match(new Token("tk_puntocoma"));

        // Agrega el funcion a la tabla de simbolos junto con sus parametros
        this.tablasSimbolo.peek().agregarSimbolo(nuevaFuncion.getAtributoToken(), new FilaFuncion("function", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, label, tipo.getAtributoToken()));

        // Agrega una nueva tabla de simbolos para entrar en un nuevo alcance
        this.tablasSimbolo.add(new TablaSimbolo());

        // Agrega funcion a la nueva tabla para recursividad
        this.tablasSimbolo.peek().agregarSimbolo(nuevaFuncion.getAtributoToken(), new FilaFuncion("function", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, label, tipo.getAtributoToken()));

        // Agrega los parametros como identificadores dentro del nuevo alcance
        this.tablasSimbolo.peek().agregarColeccionSimbolos(parametros);

        // Agrega a la tabla para retorno funcion
        this.tablasSimbolo.peek().agregarSimbolo("retorno", new FilaVariable("var", nuevaFuncion.getAtributoToken(), lexico.obtenerNumeroLinea(), "", tipo.getAtributoToken(), false, offsetParams));

        if (ultimoToken.equals(new Token("tk_var"))) {
            variables();
        }

        while ((ultimoToken.equals(new Token("tk_function"))) || (ultimoToken.equals(new Token("tk_procedure")))) {
            if (ultimoToken.equals(new Token("tk_function"))) {
                funcion();
            } else if (ultimoToken.equals(new Token("tk_procedure"))) {
                procedimiento();
            }
        }
        sentenciaCompuesta();

        // Se termina el alcance por lo que se saca la tabla de simbolos de la pila
        this.tablasSimbolo.pop();
        //System.out.println("fin funcion");
        match(new Token("tk_puntocoma"));
        nivel--;
    }

    public void procedimiento() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_procedure"));

        // Guarda los datos para agregarlos a la tabla de simbolos
        Token nuevoProcedimiento = match(new Token("tk_id"));
        this.nivel++;
        this.offset = 0;
        String label = "l" + this.labelCounter++;
        mepaManager.ENPR(label, nivel);
        ArrayList<FilaVariable> parametros = new ArrayList();

        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));
            parametros.addAll(params());
            for (FilaVariable parametro : parametros) {
                String nombreParametro = parametro.getNombre();
                if (nombreParametro.equalsIgnoreCase(nuevoProcedimiento.getAtributoToken())) {
                    throw new IdentifierAlreadyDefinedException(nombreParametro, lexico.obtenerNumeroLinea());
                }
            }
            match(new Token("tk_parentesis_der"));
        }

        // Agrega el procedimiento a la tabla de simbolos junto con sus parametros
        this.tablasSimbolo.peek().agregarSimbolo(nuevoProcedimiento.getAtributoToken(), new FilaProcedimiento("procedure", nuevoProcedimiento.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, label));

        // Agrega una nueva tabla de simbolos para entrar en un nuevo alcance
        this.tablasSimbolo.add(new TablaSimbolo());

        // Agrega funcion a la nueva tabla para recursividad
        this.tablasSimbolo.peek().agregarSimbolo(nuevoProcedimiento.getAtributoToken(), new FilaProcedimiento("procedure", nuevoProcedimiento.getAtributoToken(), lexico.obtenerNumeroLinea(), parametros, label));

        // Agrega los parametros como identificadores dentro del nuevo alcance
        this.tablasSimbolo.peek().agregarColeccionSimbolos(parametros);

        match(new Token("tk_puntocoma"));
        /* la gramatica dice que es condicional y no lo estaba */
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
        sentenciaCompuesta();

        // Se termina el alcance por lo que se saca la tabla de simbolos de la pila
        this.tablasSimbolo.pop();
        match(new Token("tk_puntocoma"));
        nivel--;
    }

    public void variables() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException, IOException {
        if (ultimoToken.equals(new Token("tk_var"))) {
            int contadorVariables = 0;
            match(new Token("tk_var"));
            do {
                contadorVariables = contadorVariables + listaIdentificadores(false).size();
                match(new Token("tk_puntocoma"));
            } while (ultimoToken.equals(new Token("tk_id")));
            this.mepaManager.RMEM(contadorVariables);
        }
    }

    public ArrayList<FilaVariable> params() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        ArrayList nuevosParametros = new ArrayList();
        offsetParams = -3;
        nuevosParametros.addAll(listaIdentificadores(true));
        ArrayList parametrosAux = paramsAux();
        if (!contieneIdentificador(nuevosParametros, parametrosAux)) {
            nuevosParametros.addAll(parametrosAux);
        }
        return nuevosParametros;
    }

    public ArrayList paramsAux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        ArrayList nuevosParametros = new ArrayList();
        while (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
            nuevosParametros.addAll(listaIdentificadores(true));
        }

        return nuevosParametros;
    }

    public ArrayList<FilaVariable> listaIdentificadores(boolean esParametro) throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, IdentifierAlreadyDefinedException {
        ArrayList<FilaVariable> nuevosIdentificadores = new ArrayList();
        Token nuevoIdentificador = match(new Token("tk_id"));

        if (!this.tablasSimbolo.peek().existeSimbolo(nuevoIdentificador.getAtributoToken())) {
            if (!esParametro) {
                nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offset++));
            } else {
                nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offsetParams--));
            }
        } else if (esParametro) {
            nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offsetParams--));
        } else {
            throw new IdentifierAlreadyDefinedException(nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea());
        }

        while (ultimoToken.equals(new Token("tk_coma"))) {
            match(new Token("tk_coma"));
            nuevoIdentificador = match(new Token("tk_id"));
            if (!this.tablasSimbolo.peek().existeSimbolo(nuevoIdentificador.getAtributoToken()) && !nuevosIdentificadores.contains(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, 0))) {
                if (!esParametro) {
                    nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offset++));
                } else {
                    nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offsetParams--));
                }
            } else if (esParametro && !nuevosIdentificadores.contains(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, 0))) {
                nuevosIdentificadores.add(new FilaVariable("var", nuevoIdentificador.getAtributoToken(), lexico.obtenerNumeroLinea(), "", "", esParametro, offsetParams--));
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

    public void alternativa() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_if"));
        String tipo = expresion();
        if (!tipo.equalsIgnoreCase("tipo_boolean")) {
            throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
        }
        int labelAltCounter = labelCounter;
        String label = "l" + labelAltCounter;
        this.mepaManager.DSVF(label);
        match(new Token("tk_then"));
        bloque();
        alternativaAux(labelAltCounter);
        labelAltCounter++;
        this.mepaManager.NADA("l" + labelAltCounter);
    }

    public void alternativaAux(int labelAltCounter) throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        if (ultimoToken.equals(new Token("tk_else"))) {
            match(new Token("tk_else"));
            int nextLabelCounter = labelAltCounter + 1;
            this.mepaManager.DSVS("l" + nextLabelCounter);
            this.mepaManager.NADA("l" + labelAltCounter);
            bloque();

        }
    }

    public void repetitiva() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_while"));
        String labelTrue = "l" + this.labelCounter++;
        String labelFalse = "l" + this.labelCounter++;
        this.mepaManager.NADA(labelTrue);
        String tipo = expresion();
        this.mepaManager.DSVF(labelFalse);
        if (!tipo.equalsIgnoreCase("tipo_boolean")) {
            throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
        }
        match(new Token("tk_do"));
        bloque();
        this.mepaManager.DSVS(labelTrue);
        this.mepaManager.NADA(labelFalse);
    }

    public void llamadaSub(Token identificadorSubprograma) throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        int numeroParametroActual = 0;
        String tipoParametro = "";
        String tipoParametroEsperado = "";

        // Token identificadorSubprograma = match(new Token("tk_id"));
        FilaProcedimiento simboloSubprograma = (FilaProcedimiento) this.obtenerIdentificador(identificadorSubprograma.getAtributoToken());
        ArrayList listaParametros = simboloSubprograma.getListaParametros();

        if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
            match(new Token("tk_parentesis_izq"));

            if (listaParametros.size() > 0) {
                // Parametros de llamada
                Fila parametroLlamada = this.obtenerIdentificador(ultimoToken.getAtributoToken());

                // Si el identificador existe pero no es una función ni una variable (por lo tanto no puede ser un parametro)
                if (parametroLlamada != null && !parametroLlamada.getTipoConstructor().equalsIgnoreCase("function") && !parametroLlamada.getTipoConstructor().equalsIgnoreCase("var")) {
                    throw new WrongConstructorException(parametroLlamada.getNombre(), lexico.obtenerNumeroLinea());
                }

                // Consumimos el token del parametro
                tipoParametro = expresion();

                // Obtenemos el primer parametro
                FilaVariable parametro = (FilaVariable) listaParametros.get(numeroParametroActual);
                tipoParametroEsperado = parametro.getTipo();

                if (!tipoParametro.equalsIgnoreCase(tipoParametroEsperado)) {
                    throw new WrongTypeException(this.tablaNombresTokens.get(tipoParametroEsperado), lexico.obtenerNumeroLinea());
                }

                while (ultimoToken.equals(new Token("tk_coma"))) {

                    if (numeroParametroActual < listaParametros.size() - 1) {
                        numeroParametroActual++;
                        match(new Token("tk_coma"));
                        // Parametros de llamada
                        parametroLlamada = this.obtenerIdentificador(ultimoToken.getAtributoToken());

                        // Si el identificador existe pero no es una función ni una variable (por lo tanto no puede ser un parametro)
                        if (parametroLlamada != null && !parametroLlamada.getTipoConstructor().equalsIgnoreCase("function") && !parametroLlamada.getTipoConstructor().equalsIgnoreCase("var")) {
                            throw new WrongConstructorException(parametroLlamada.getNombre(), lexico.obtenerNumeroLinea());
                        }

                        // Consumimos el token del parametro
                        tipoParametro = expresion();

                        // Obtenemos el siguiente parametro
                        parametro = (FilaVariable) listaParametros.get(numeroParametroActual);
                        tipoParametroEsperado = parametro.getTipo();

                        if (!tipoParametro.equalsIgnoreCase(tipoParametroEsperado)) {
                            throw new WrongTypeException(this.tablaNombresTokens.get(tipoParametroEsperado), lexico.obtenerNumeroLinea());
                        }

                    } else {
                        // Si la cantidad de parametros es erronea
                        throw new WrongArgumentsException(identificadorSubprograma.getAtributoToken(), lexico.obtenerNumeroLinea());
                    }

                }

                if (listaParametros.size() - 1 > numeroParametroActual) {
                    throw new WrongArgumentsException(identificadorSubprograma.getAtributoToken(), lexico.obtenerNumeroLinea());
                }

                match(new Token("tk_parentesis_der"));
            } else {
                throw new WrongArgumentsException(identificadorSubprograma.getAtributoToken(), lexico.obtenerNumeroLinea());
            }
        } else {
            if (listaParametros.size() > 0) {
                throw new WrongArgumentsException(identificadorSubprograma.getAtributoToken(), lexico.obtenerNumeroLinea());
            }
        }
    }

    public void leer() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException {
        match(new Token("tk_read"));
        match(new Token("tk_parentesis_izq"));
        match(new Token("tk_id"));
        match(new Token("tk_parentesis_der"));
    }

    public void escribir() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_write"));
        match(new Token("tk_parentesis_izq"));
        expresion();
        match(new Token("tk_parentesis_der"));
    }

    public void sentenciaCompuesta() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        match(new Token("tk_begin"));
        bloque();
        /* modificacion reciente de la gramatica*/
        if (ultimoToken.equals(new Token("tk_puntocoma"))) {
            match(new Token("tk_puntocoma"));
        }
        match(new Token("tk_end"));
    }

    public String expresion() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = expresion_1();
        String tipoAux = expresionAux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;
    }

    public String expresionAux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_or"))) {
            match(new Token("tk_op_or"));
            expresion_1();
            this.mepaManager.DISJ(); // TODO: Va aca o depues de expresionAux?
            expresionAux();
            if (tipo.equalsIgnoreCase("tipo_integer")) {
                throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

            }
        }
        return tipo;
    }

    public String expresion_1() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = expresion_2();
        String tipoAux = expresion_1_aux();
        if (!tipoAux.equals("")) {
            tipo = tipoAux;
        }
        return tipo;

    }

    public String expresion_1_aux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        String tipoAux = "";
        if (ultimoToken.equals(new Token("tk_op_and"))) {
            match(new Token("tk_op_and"));
            tipo = expresion_2();
            this.mepaManager.CONJ(); // TODO: Va aca o depues de expresion_1_aux?
            expresion_1_aux();
            if (tipo.equalsIgnoreCase("tipo_integer")) {
                throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

            }
        }
        return tipo;
    }

    public String expresion_2() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        String tipoAux = "";
        if (ultimoToken.equals(new Token("tk_op_not"))) {
            match(new Token("tk_op_not"));
            tipoAux = "tipo_boolean";
            this.mepaManager.NEGA();
        }
        tipo = expresion_3();
        if (!tipoAux.equals("") && !tipo.equalsIgnoreCase(tipoAux)) {
            throw new WrongTypeException("boolean", lexico.obtenerNumeroLinea());

        }
        return tipo;
    }

    public String expresion_3() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = expresion_4();
        //System.out.println("exp_4 " + tipo);
        String tipoAux = expresion_3_aux();
        //System.out.println("exp_3AUX " + tipoAux);
        if (!tipoAux.equals("")) {
            if (tipo.equalsIgnoreCase(tipoAux)) {
                //tipo = tipoAux;
                tipo = "tipo_boolean";
            } else {
                throw new WrongTypeException(this.tablaNombresTokens.get(tipoAux), lexico.obtenerNumeroLinea());
            }
        }
        return tipo;
    }

    public String expresion_3_aux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_relacional"))) {
            Token tokenMatcheado = operador_comparacion();
            tipo = expresion_4();
            switch (tokenMatcheado.getAtributoToken()) {
                case "op_igual":
                    this.mepaManager.CMIG();
                    break;

                case "op_mayor":
                    this.mepaManager.CMMA();
                    break;

                case "op_menor":
                    this.mepaManager.CMME();
                    break;

                case "op_distinto":
                    this.mepaManager.CMDG();
                    break;

                case "op_mayor_igual":
                    this.mepaManager.CMYI();
                    break;

                case "op_menor_igual":
                    this.mepaManager.CMNI();
                    break;
            }
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

    public String expresion_4() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = expresion_5();
        String tipoAux = expresion_4_aux();
        if (!tipoAux.equals("")) {
            if (tipo.equalsIgnoreCase(tipoAux)) {
                //tipo = tipoAux;
                tipo = tipoAux;
            } else {
                throw new WrongTypeException(this.tablaNombresTokens.get(tipoAux), lexico.obtenerNumeroLinea());
            }

        }
        return tipo;
    }

    public String expresion_4_aux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_suma"))) {
            match(new Token("tk_op_suma"));
            tipo = expresion_5();
            this.mepaManager.SUMA();
            if (tipo.equalsIgnoreCase("tipo_boolean")) {
                throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            }
            expresion_4_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_resta"))) {
                match(new Token("tk_op_resta"));
                tipo = expresion_5();
                this.mepaManager.SUST();
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
                expresion_4_aux();
            }
        }
        return tipo;
    }

    public String expresion_5() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = expresion_6();
        String tipoAux = expresion_5_aux();
        if (!tipoAux.equals("")) {
            if (tipo.equalsIgnoreCase(tipoAux)) {
                //tipo = tipoAux;
                tipo = tipoAux;
            } else {
                throw new WrongTypeException(this.tablaNombresTokens.get(tipoAux), lexico.obtenerNumeroLinea());
            }
        }
        return tipo;
    }

    public String expresion_5_aux() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        if (ultimoToken.equals(new Token("tk_op_mult"))) {
            match(new Token("tk_op_mult"));
            tipo = expresion_6();
            this.mepaManager.MULT();
            if (tipo.equalsIgnoreCase("tipo_boolean")) {
                throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
            }
            expresion_5_aux();
        } else {
            if (ultimoToken.equals(new Token("tk_op_div"))) {
                match(new Token("tk_op_div"));
                tipo = expresion_6();
                this.mepaManager.DIVI();
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
                expresion_5_aux();
            }
        }
        return tipo;
    }

    public String expresion_6() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
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
                this.mepaManager.UMEN(); // TODO: Corroborar si el unario va arriba o abajo del numero en la pila
                if (tipo.equalsIgnoreCase("tipo_boolean")) {
                    throw new WrongTypeException("integer", lexico.obtenerNumeroLinea());
                }
            } else {
                tipo = factor();
            }
        }
        return tipo;
    }

    public Token operador_comparacion() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException {
        Token tokenMatcheado = null;
        switch (ultimoToken.getAtributoToken()) {
            case "op_igual":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_igual"));
                break;

            case "op_mayor":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_mayor"));
                break;

            case "op_menor":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_menor"));
                break;

            case "op_distinto":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_distinto"));
                break;

            case "op_mayor_igual":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_mayor_igual"));
                break;

            case "op_menor_igual":
                tokenMatcheado = match(new Token("tk_op_relacional", "op_menor_igual"));
                break;

            default:
                String tokenEncontrado = ultimoToken.getAtributoToken();
                if (tokenEncontrado.equals("")) {
                    tokenEncontrado = ultimoToken.getNombreToken();
                }

                throw new UnexpectedTokenException("comparison operator", tokenEncontrado, lexico.obtenerNumeroLinea());

        }

        return tokenMatcheado;
    }

    public String factor() throws UnexpectedTokenException, UnexpectedCharException, UnopenedCommentException, UnclosedCommentException, WrongTypeException, WrongConstructorException, IdentifierNotDefinedException, WrongArgumentsException, IOException {
        String tipo = "";
        switch (ultimoToken.getNombreToken()) {
            // Identificador o llamada subprograma
            case "tk_id":
                Token identificador = match(new Token("tk_id"));
                if (ultimoToken.equals(new Token("tk_parentesis_izq"))) {
                    FilaFuncion fila = (FilaFuncion) obtenerIdentificador(identificador.getAtributoToken());
                    llamadaSub(identificador);
                    this.mepaManager.LLPR(fila.getLabel());
                    if (fila != null) {
                        tipo = fila.getTipoRetorno();
                    } else {
                        throw new IdentifierNotDefinedException(identificador.getAtributoToken(), lexico.obtenerNumeroLinea());
                    }

                } else {
                    Fila fila = obtenerIdentificador(identificador.getAtributoToken());
                    if (fila != null) {
                        //llamada a funcion sin parametros 
                        if (fila.getTipoConstructor().equalsIgnoreCase("function")) {
                            llamadaSub(identificador);
                            tipo = ((FilaFuncion) fila).getTipoRetorno();
                        } else if (fila.getTipoConstructor().equalsIgnoreCase("var")) {
                            this.mepaManager.APVL(nivel, ((FilaVariable) fila).getOffset());
                            tipo = ((FilaVariable) fila).getTipo();
                        } else {
                            throw new WrongConstructorException(fila.getTipoConstructor(), lexico.obtenerNumeroLinea());
                        }
                    } else {
                        throw new IdentifierNotDefinedException(identificador.getAtributoToken(), lexico.obtenerNumeroLinea());
                    }

                }
                break;
            case "tk_numero":
                Token numero = match(new Token("tk_numero"));
                this.mepaManager.APCT(numero.getAtributoToken());
                tipo = "tipo_integer";
                break;
            case "tk_boolean":
                Token bool = match(new Token("tk_boolean"));
                String boolApilar = bool.getAtributoToken().equalsIgnoreCase("true") ? "1" : "0";
                this.mepaManager.APCT(boolApilar);
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

                throw new UnexpectedTokenException("factor", this.tablaNombresTokens.getOrDefault(tokenEncontrado, tokenEncontrado), lexico.obtenerNumeroLinea());
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
        tablaNombresTokens.put("tk_tipo", "type");
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

    private Fila obtenerIdentificador(String identificador) {
        Fila simbolo = null;
        int i = this.tablasSimbolo.size() - 1;
        while (i >= 0 && simbolo == null) {
            simbolo = tablasSimbolo.get(i).obtenerSimbolo(identificador);
            i--;
        }
        return simbolo;
    }

}
