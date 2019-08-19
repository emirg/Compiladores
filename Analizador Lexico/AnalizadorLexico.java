import java.io.BufferedReader;
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

    private static HashMap<String, String> palabrasReservadas;
    private static BufferedReader reader;
    private static FileWriter fileWriter;
    private static int caracterActual;
    private static int numLinea = 1;
    // TODO: Agregar los tokens a una lista para pasarlos al Analizador Sintactico
    // private static ArrayList<Token> tokens;

    /*public AnalizadorLexico(){

    }*/

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Cantidad de argumentos incorrecta");
        } else {
            try {
                cargarPalabrasReservadas(); // Inicializa la estructura que contiene las palabras reservadas junto a sus
                                            // tokens
                reader = new BufferedReader(new FileReader(args[0])); // Abre el archivo de lectura
                fileWriter = new FileWriter("archivoTokens.txt"); // Abre el archivo de escritura
                // fileWriter.write(fileContent);
                caracterActual = -2;
                String cadenaAux = "";
                while (caracterActual != -1) { // Carater '-1' es el fin del archivo
                    if (caracterActual == -2) { // Caracter arbitrario usado para saber si se tiene que seguir leyendo
                        caracterActual = leerCaracter();
                        // System.out.println((char) caracterActual);
                        switch (caracterActual) {
                        case '<':
                            reader.mark(1);
                            caracterActual = leerCaracter();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_op_relacional , op_menor_igual> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else if (caracterActual == '>') {
                                cadenaAux = "<tk_op_relacional , op_distinto> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_op_relacional , op_menor> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                                reader.reset();
                            }
                            break;

                        case '=':
                            cadenaAux = "<tk_op_relacional , op_igual> \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '>':
                            reader.mark(1);
                            caracterActual = leerCaracter();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_op_relacional , op_mayor_igual> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_op_relacional , op_mayor> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                                reader.reset();
                            }
                            break;

                        case ',':
                            cadenaAux = "<tk_coma , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case ';':
                            cadenaAux = "<tk_puntocoma , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case ':':
                            reader.mark(1);
                            caracterActual = leerCaracter();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_asignacion , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_dospuntos , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                                reader.reset();
                            }
                            break;

                        case '.':
                            reader.mark(1);
                            caracterActual = leerCaracter();
                            if (caracterActual == '.') {
                                cadenaAux = "<tk_doblepunto , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_punto , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                                reader.mark(1);
                            }
                            break;

                        case '(':
                            cadenaAux = "<tk_parentesis_izq , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case ')':
                            cadenaAux = "<tk_parentesis_der , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '-':
                            cadenaAux = "<tk_op_resta , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '+':
                            cadenaAux = "<tk_op_suma , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '/':
                            cadenaAux = "<tk_op_div , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '*':
                            cadenaAux = "<tk_op_mult , > \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case ' ': // Probablemente haya que ignorar, no devolver un <blank>, pero por ahora lo
                                  // dejamos
                            cadenaAux = "<blank> \n";
                            // fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '\n': // Probablemente haya que ignorar, no devolver un <newline>, pero por ahora lo
                                   // dejamos
                            cadenaAux = "<newline> \n";
                            // fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case '\r': // Idem \n pero para CRLF
                            cadenaAux = "<newline> \n";
                            // fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case '\t': // Probablemente haya que ignorar, no devolver un <tab>, pero por ahora lo
                                   // dejamos
                            cadenaAux = "<tab> \n";
                            // fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case -1: // Probablemente haya que ignorar, no devolver un <fin_archivo>, pero por ahora
                                 // lo dejamos
                            cadenaAux = "<fin_archivo>";
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
                                    fileWriter.close();
                                    reader.close();
                                }
                            } else { // ID y Numero
                                if ((caracterActual >= 'A' && caracterActual <= 'Z')
                                        || (caracterActual >= 'a' && caracterActual <= 'z')) { // Si es una letra
                                                                                               // (ID/Palabra Reservada)
                                    String identificador = leerID();
                                    if (palabrasReservadas.containsKey(identificador.toLowerCase())) {
                                        cadenaAux = palabrasReservadas.get(identificador.toLowerCase()) + "\n";
                                        fileWriter.write(cadenaAux);
                                        caracterActual = -2;
                                        reader.reset();
                                    } else {
                                        cadenaAux = "<tk_id , " + identificador + "> \n";
                                        fileWriter.write(cadenaAux);
                                        caracterActual = -2;
                                        reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                    }
                                } else { // No empieza con letra
                                    if (caracterActual >= '0' && caracterActual <= '9') {
                                        String numero = leerNum();
                                        cadenaAux = "<tk_numero , " + numero + "> \n";
                                        fileWriter.write(cadenaAux);
                                        caracterActual = -2;
                                        reader.reset(); // Ir a metodo leerID/leerNum para explicacion de esto
                                    } else { // Probablemente un caracter invalido
                                        cadenaAux = "Linea " + numLinea + ": <Error: caracter \""
                                                + (char) caracterActual + "\" no valido> \n";
                                        fileWriter.write(cadenaAux);
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
                }
                fileWriter.close();
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void cargarPalabrasReservadas() {
        palabrasReservadas = new HashMap<String, String>();
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
        palabrasReservadas.put("false", "<tk_boolean , valor_false>");
    }

    public static String leerNum() {
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

    public static String leerID() {
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

    public static void leerComentario() throws Exception {

        caracterActual = leerCaracter();
        int lineaComienzoComentario = numLinea;
        while (caracterActual != '}' && caracterActual != -1 && caracterActual != '@') {
            caracterActual = leerCaracter();
        }

        if (caracterActual == -1) {
            // Error: No se cerro el comentario

            fileWriter.write("Linea " + lineaComienzoComentario + ": <Error: Comentario no fue cerrado> \n");
            throw new Exception("Linea " + lineaComienzoComentario + ": <Error: Comentario no fue cerrado>");
        } else if (caracterActual == '@') {
            // Error: Caracter no valido en los comentarios
            fileWriter.write("Linea " + numLinea + ": <Error: caracter \"" + (char) caracterActual
                    + "\" no valido en los comentarios> \n");
            throw new Exception("Linea " + numLinea + ": <Error: caracter \"" + (char) caracterActual
                    + "\" no valido en los comentarios>");
        }
    }

    public static int leerCaracter() {
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
}
