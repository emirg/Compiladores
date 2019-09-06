package AnalizadorLexico;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class AnalizadorLexico {

    // Posibles errores:
    // 1) Caracteres no esten en el alfabeto (invalidos)
    // 2) No cierra los comentarios
    // Tener en cuenta:
    // 1) Las palabras reservadas pueden estar en mayusculas, minisculas o
    // intercalado
    private HashMap<String, Token> palabrasReservadas;
    private BufferedReader reader;
    private FileWriter fileWriter;
    private int caracterActual;
    private Token ultimoTokenGenerado;
    private int numLinea = 1;

    public AnalizadorLexico(String nombreArchivo) {

        try {
            this.reader = new BufferedReader(new FileReader(nombreArchivo)); // Abre el archivo de lectura
        } catch (FileNotFoundException exception) {
            System.out.println("Archivo '" + nombreArchivo + "' no encontrado");
        }

        this.cargarPalabrasReservadas();
        this.caracterActual = -2;
        this.ultimoTokenGenerado = null;
    }

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Cantidad de argumentos incorrecta");
        } else {
            try {
                AnalizadorLexico lexico = new AnalizadorLexico(args[0]);
                lexico.abrirArchivoEscritura("archivoTokens.txt");
                // fileWriter.write(fileContent);
                lexico.caracterActual = -2;
                String cadenaAux;
                while (lexico.caracterActual != -1) { // Carater '-1' es el fin del archivo
                    if (lexico.caracterActual == -2) { // Caracter arbitrario usado para saber si se tiene que seguir leyendo
                        lexico.caracterActual = lexico.leerCaracter();
                        // System.out.println((char) lexico.caracterActual);
                        switch (lexico.caracterActual) {
                            case '<':
                                lexico.reader.mark(1);
                                lexico.caracterActual = lexico.leerCaracter();
                            switch (lexico.caracterActual) {
                                case '=':
                                    cadenaAux = "<tk_op_relacional , op_menor_igual> \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    break;
                                case '>':
                                    cadenaAux = "<tk_op_relacional , op_distinto> \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    break;
                                default:
                                    cadenaAux = "<tk_op_relacional , op_menor> \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    lexico.reader.reset();
                                    break;
                            }
                                break;


                            case '=':
                                cadenaAux = "<tk_op_relacional , op_igual> \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '>':
                                lexico.reader.mark(1);
                                lexico.caracterActual = lexico.leerCaracter();
                                if (lexico.caracterActual == '=') {
                                    cadenaAux = "<tk_op_relacional , op_mayor_igual> \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                } else {
                                    cadenaAux = "<tk_op_relacional , op_mayor> \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    lexico.reader.reset();
                                }
                                break;

                            case ',':
                                cadenaAux = "<tk_coma , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case ';':
                                cadenaAux = "<tk_puntocoma , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case ':':
                                lexico.reader.mark(1);
                                lexico.caracterActual = lexico.leerCaracter();
                                if (lexico.caracterActual == '=') {
                                    cadenaAux = "<tk_asignacion , > \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                } else {
                                    cadenaAux = "<tk_dospuntos , > \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    lexico.reader.reset();
                                }
                                break;

                            case '.':
                                lexico.reader.mark(1);
                                lexico.caracterActual = lexico.leerCaracter();
                                if (lexico.caracterActual == '.') {
                                    cadenaAux = "<tk_doblepunto , > \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                } else {
                                    cadenaAux = "<tk_punto , > \n";
                                    lexico.fileWriter.write(cadenaAux);
                                    lexico.caracterActual = -2;
                                    lexico.reader.mark(1);
                                }
                                break;

                            case '(':
                                cadenaAux = "<tk_parentesis_izq , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case ')':
                                cadenaAux = "<tk_parentesis_der , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '-':
                                cadenaAux = "<tk_op_resta , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '+':
                                cadenaAux = "<tk_op_suma , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '/':
                                cadenaAux = "<tk_op_div , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '*':
                                cadenaAux = "<tk_op_mult , > \n";
                                lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;
                            case ' ': // Probablemente haya que ignorar, no devolver un <blank>, pero por ahora lo
                                // dejamos
                                // cadenaAux = "<blank> \n";
                                // lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;

                            case '\n': // Probablemente haya que ignorar, no devolver un <newline>, pero por ahora lo
                                // dejamos
                                // cadenaAux = "<newline> \n";
                                // lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;
                            case '\r': // Idem \n pero para CRLF
                                // cadenaAux = "<newline> \n";
                                // lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;
                            case '\t': // Probablemente haya que ignorar, no devolver un <tab>, pero por ahora lo
                                // dejamos
                                // cadenaAux = "<tab> \n";
                                // lexico.fileWriter.write(cadenaAux);
                                lexico.caracterActual = -2;
                                break;
                            case -1: // Probablemente haya que ignorar, no devolver un <fin_archivo>, pero por ahora
                                // lo dejamos
                                // cadenaAux = "<fin_archivo>";
                                // lexico.fileWriter.write(cadenaAux);
                                break;

                            default:
                                if (lexico.caracterActual == '{') {
                                    try {
                                        lexico.leerComentario();
                                        lexico.caracterActual = -2;
                                    } catch (Exception e) { // Si hubo una excepcion leyendo el comentario (eof o un '@')
                                        System.out.println(e.getMessage());
                                        lexico.caracterActual = -1;
                                        lexico.fileWriter.close();
                                        lexico.reader.close();
                                    }
                                } else { // ID y Numero
                                    if ((lexico.caracterActual >= 'A' && lexico.caracterActual <= 'Z')
                                            || (lexico.caracterActual >= 'a' && lexico.caracterActual <= 'z')) { // Si es una letra
                                        // (ID/Palabra Reservada)
                                        String identificador = lexico.leerID();
                                        if (lexico.palabrasReservadas.containsKey(identificador.toLowerCase())) {
                                            cadenaAux = lexico.palabrasReservadas.get(identificador.toLowerCase()) + "\n";
                                            lexico.fileWriter.write(cadenaAux);
                                            lexico.caracterActual = -2;
                                            lexico.reader.reset();
                                        } else {
                                            cadenaAux = "<tk_id , " + identificador + "> \n";
                                            lexico.fileWriter.write(cadenaAux);
                                            lexico.caracterActual = -2;
                                            lexico.reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                        }
                                    } else { // No empieza con letra
                                        if (lexico.caracterActual >= '0' && lexico.caracterActual <= '9') {
                                            String numero = lexico.leerNum();
                                            cadenaAux = "<tk_numero , " + numero + "> \n";
                                            lexico.fileWriter.write(cadenaAux);
                                            lexico.caracterActual = -2;
                                            lexico.reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                        } else { // Probablemente un caracter invalido
                                            cadenaAux = "Linea " + lexico.numLinea + ": <Error: caracter \""
                                                    + (char) lexico.caracterActual + "\" no valido> \n";
                                            lexico.fileWriter.write(cadenaAux);
                                            try {
                                                throw new Exception(cadenaAux);
                                            } catch (Exception e) {
                                                System.out.print(e.getMessage());
                                            }
                                            lexico.caracterActual = -1;
                                        }
                                    }
                                }
                        }
                    }
                }
                lexico.fileWriter.close();
                lexico.reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public Token obtenerToken() {
        try {
            while (caracterActual == -2) { // Caracter arbitrario usado para saber si se tiene que seguir leyendo
                caracterActual = leerCaracter();
                // System.out.println((char) caracterActual);
                switch (caracterActual) {
                    case '<':
                        reader.mark(1);
                        caracterActual = leerCaracter();
                        if (caracterActual == '=') {
                            // cadenaAux = "<tk_op_relacional , op_menor_igual> \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_menor_igual");
                            caracterActual = -1;
                        } else if (caracterActual == '>') {
                            // cadenaAux = "<tk_op_relacional , op_distinto> \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_distinto");
                            caracterActual = -1;
                        } else {
                            // cadenaAux = "<tk_op_relacional , op_menor> \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_menor");
                            caracterActual = -1;
                            reader.reset();
                        }
                        break;

                    case '=':
                        // cadenaAux = "<tk_op_relacional , op_igual> \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_igual");
                        caracterActual = -1;
                        break;

                    case '>':
                        reader.mark(1);
                        caracterActual = leerCaracter();
                        if (caracterActual == '=') {
                            // cadenaAux = "<tk_op_relacional , op_mayor_igual> \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_mayor_igual");

                            caracterActual = -1;
                        } else {
                            // cadenaAux = "<tk_op_relacional , op_mayor> \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_op_relacional", "op_mayor");
                            caracterActual = -1;
                            reader.reset();
                        }
                        break;

                    case ',':
                        // cadenaAux = "<tk_coma , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_coma", "");
                        caracterActual = -1;
                        break;

                    case ';':
                        // cadenaAux = "<tk_puntocoma , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_puntocoma", "");
                        caracterActual = -1;
                        break;

                    case ':':
                        reader.mark(1);
                        caracterActual = leerCaracter();
                        if (caracterActual == '=') {
                            // cadenaAux = "<tk_asignacion , > \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_asignacion", "");
                            caracterActual = -1;
                        } else {
                            // cadenaAux = "<tk_dospuntos , > \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_dospuntos", "");
                            caracterActual = -1;
                            reader.reset();
                        }
                        break;

                    case '.':
                        reader.mark(1);
                        caracterActual = leerCaracter();
                        if (caracterActual == '.') {
                            // cadenaAux = "<tk_doblepunto , > \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_doblepunto", "");
                            caracterActual = -1;
                        } else {
                            // cadenaAux = "<tk_punto , > \n";
                            // fileWriter.write(cadenaAux);
                            this.ultimoTokenGenerado = new Token("tk_punto", "");
                            caracterActual = -1;
                            reader.mark(1);
                        }
                        break;

                    case '(':
                        // cadenaAux = "<tk_parentesis_izq , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_parentesis_izq", "");
                        caracterActual = -1;
                        break;

                    case ')':
                        // cadenaAux = "<tk_parentesis_der , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_parentesis_der", "");
                        caracterActual = -1;
                        break;

                    case '-':
                        // cadenaAux = "<tk_op_resta , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_op_resta", "");
                        caracterActual = -1;
                        break;

                    case '+':
                        // cadenaAux = "<tk_op_suma , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_op_suma", "");
                        caracterActual = -1;
                        break;

                    case '/':
                        // cadenaAux = "<tk_op_div , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_op_div", "");
                        caracterActual = -1;
                        break;

                    case '*':
                        // cadenaAux = "<tk_op_mult , > \n";
                        // fileWriter.write(cadenaAux);
                        this.ultimoTokenGenerado = new Token("tk_op_mult", "");
                        caracterActual = -1;
                        break;
                    case ' ': // Probablemente haya que ignorar, no devolver un <blank>, pero por ahora lo
                        // dejamos
                        // cadenaAux = "<blank> \n";
                        // fileWriter.write(cadenaAux);
                        //System.out.println("leyendo espacio");
                        caracterActual = -2;
                        break;

                    case '\n': // Probablemente haya que ignorar, no devolver un <newline>, pero por ahora lo
                        // dejamos
                        // cadenaAux = "<newline> \n";
                        // fileWriter.write(cadenaAux);
                        caracterActual = -2;
                        break;
                    case '\r': // Idem \n pero para CRLF
                        // cadenaAux = "<newline> \n";
                        // fileWriter.write(cadenaAux);
                        caracterActual = -2;
                        break;
                    case '\t': // Probablemente haya que ignorar, no devolver un <tab>, pero por ahora lo
                        // dejamos
                        // cadenaAux = "<tab> \n";
                        // fileWriter.write(cadenaAux);
                        caracterActual = -2;
                        break;
                    case -1: // Probablemente haya que ignorar, no devolver un <fin_archivo>, pero por ahora
                        // lo dejamos
                        // cadenaAux = "<fin_archivo>";
                        // fileWriter.write(cadenaAux);
                        break;

                    default:
                        if (caracterActual == '{') {
                            try {
                                leerComentario();
                                caracterActual = -2;
                            } catch (Exception e) { // Si hubo una excepcion leyendo el comentario (eof o un '@')
                                System.out.println(e.getMessage());
                                caracterActual = -1;
                                // fileWriter.close();
                                reader.close();
                            }
                        } else { // ID y Numero
                            if ((caracterActual >= 'A' && caracterActual <= 'Z')
                                    || (caracterActual >= 'a' && caracterActual <= 'z')) { // Si es una letra
                                // (ID/Palabra Reservada)
                                String identificador = leerID();
                                if (palabrasReservadas.containsKey(identificador.toLowerCase())) {
                                    // cadenaAux = palabrasReservadas.get(identificador.toLowerCase()) + "\n";
                                    // fileWriter.write(cadenaAux);
                                    this.ultimoTokenGenerado = palabrasReservadas.get(identificador.toLowerCase());
                                    caracterActual = -1;
                                    reader.reset();
                                } else {
                                    // cadenaAux = "<tk_id , " + identificador + "> \n";
                                    // fileWriter.write(cadenaAux);
                                    this.ultimoTokenGenerado = new Token("tk_id", identificador);
                                    caracterActual = -1;
                                    reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                }
                            } else { // No empieza con letra
                                if (caracterActual >= '0' && caracterActual <= '9') {
                                    String numero = leerNum();
                                    // cadenaAux = "<tk_numero , " + numero + "> \n";
                                    // fileWriter.write(cadenaAux);
                                    this.ultimoTokenGenerado = new Token("tk_numero", numero);
                                    caracterActual = -1;
                                    reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                } else { // Probablemente un caracter invalido
                                    String cadenaAux = "Linea " + numLinea + ": <Error: caracter \""
                                            + (char) caracterActual + "\" no valido> \n";
                                    // fileWriter.write(cadenaAux);
                                    try {
                                        throw new Exception(cadenaAux);
                                    } catch (Exception e) {
                                        System.out.print(e.getMessage());
                                    }
                                    caracterActual = -1;
                                }
                            }
                        }
                }
            }
        } catch (IOException exception) {

        }
        caracterActual = -2;
        return ultimoTokenGenerado;
    }

    public int obtenerNumeroLinea() {
        return numLinea;
    }

    private void cargarPalabrasReservadas() {
        /*         palabrasReservadas = new HashMap<String, String>();
        palabrasReservadas.put("and", "<tk_op_and , >");
        palabrasReservadas.put("or", "<tk_op_or , >");
        palabrasReservadas.put("not", "<tk_op_not , >");
        palabrasReservadas.put("program", "<tk_program , >");
        palabrasReservadas.put("begin", "<tk_begin , >");
        palabrasReservadas.put("end", "<tk_end , >");
        palabrasReservadas.put("function", "<tk_function , >");
        palabrasReservadas.put("procedure", "<tk_procedure , >");
        palabrasReservadas.put("var", "<tk_var , >");
        palabrasReservadas.put("if", "<tk_if , >");
        palabrasReservadas.put("then", "<tk_then , >");
        palabrasReservadas.put("else", "<tk_else , >");
        palabrasReservadas.put("while", "<tk_while , >");
        palabrasReservadas.put("do", "<tk_do , >");
        palabrasReservadas.put("read", "<tk_read , >");
        palabrasReservadas.put("write", "<tk_write , >");
        palabrasReservadas.put("boolean", "<tk_tipo , tipo_boolean>");
        palabrasReservadas.put("integer", "<tk_tipo , tipo_integer>");
        palabrasReservadas.put("true", "<tk_boolean , valor_true>");
        palabrasReservadas.put("false", "<tk_boolean , valor_false>"); */

        this.palabrasReservadas = new HashMap<String, Token>();
        palabrasReservadas.put("and", new Token("tk_op_and", ""));
        palabrasReservadas.put("or", new Token("tk_op_or", ""));
        palabrasReservadas.put("not", new Token("tk_op_not", ""));
        palabrasReservadas.put("program", new Token("tk_program", ""));
        palabrasReservadas.put("begin", new Token("tk_begin", ""));
        palabrasReservadas.put("end", new Token("tk_end", ""));
        palabrasReservadas.put("function", new Token("tk_function", ""));
        palabrasReservadas.put("procedure", new Token("tk_procedure", ""));
        palabrasReservadas.put("var", new Token("tk_var", ""));
        palabrasReservadas.put("if", new Token("tk_if", ""));
        palabrasReservadas.put("then", new Token("tk_then", ""));
        palabrasReservadas.put("else", new Token("tk_else", ""));
        palabrasReservadas.put("while", new Token("tk_while", ""));
        palabrasReservadas.put("do", new Token("tk_do", ""));
        palabrasReservadas.put("read", new Token("tk_read", ""));
        palabrasReservadas.put("write", new Token("tk_write", ""));
        palabrasReservadas.put("boolean", new Token("tk_tipo", "tipo_boolean"));
        palabrasReservadas.put("integer", new Token("tk_tipo", "tipo_integer"));
        palabrasReservadas.put("true", new Token("tk_boolean", "valor_true"));
        palabrasReservadas.put("false", new Token("tk_boolean", "valor_false"));
    }

    private String leerNum() {
        // String numero = "";
        StringBuilder constructorNumero = new StringBuilder();
        try {
            while (caracterActual >= '0' && caracterActual <= '9') {
                constructorNumero.append((char) caracterActual);
                reader.mark(1); // Solucion temporal: sin esto, perdemos el caracter siguiente al ID/Numero. Se
                // marca y despues se resetea la cabeza del reader para seguir en el siguiente
                // caracter directo al ID/Numero
                caracterActual = leerCaracter();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return constructorNumero.toString();
    }

    private String leerID() {
        // String identificador = "";
        StringBuilder constructorID = new StringBuilder();
        try {
            while ((caracterActual >= 'A' && caracterActual <= 'Z') || (caracterActual >= 'a' && caracterActual <= 'z')
                    || (caracterActual >= '0' && caracterActual <= '9')) {
                constructorID.append((char) caracterActual);
                reader.mark(1); // Solucion temporal: sin esto, perdemos el caracter siguiente al ID/Numero. Se
                // marca y despues se resetea la cabeza del reader para seguir en el siguiente
                // caracter directo al ID/Numero
                caracterActual = leerCaracter();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return constructorID.toString();
    }

    private void leerComentario() throws Exception {

        caracterActual = leerCaracter();
        int lineaComienzoComentario = numLinea;
        while (caracterActual != '}' && caracterActual != -1 && caracterActual != '@') {
            caracterActual = leerCaracter();
        }

        if (caracterActual == -1) {
            // Error: No se cerro el comentario

            // fileWriter.write("Linea " + lineaComienzoComentario + ": <Error: Comentario no fue cerrado> \n");
            throw new Exception("Linea " + lineaComienzoComentario + ": <Error: Comentario no fue cerrado>");
        } else if (caracterActual == '@') {
            // Error: Caracter no valido en los comentarios
            // fileWriter.write("Linea " + numLinea + ": <Error: caracter \"" + (char) caracterActual
            //        + "\" no valido en los comentarios> \n");
            throw new Exception("Linea " + numLinea + ": <Error: caracter \"" + (char) caracterActual
                    + "\" no valido en los comentarios>");
        }
    }

    private int leerCaracter() {
        int caracter = -1;
        try {
            caracter = reader.read();
            if (caracter == '\n') {
                numLinea++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return caracter;
    }

    private void abrirArchivoEscritura(String nombreArchivo) throws IOException {
        fileWriter = new FileWriter(nombreArchivo); // Abre el archivo de escritura
    }

}
