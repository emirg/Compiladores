import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

/**
 *
 * @author emiliano
 */

public class AnalizadorLexico {

    /*
     * Posibles errores: 1) Caracteres no esten en el alfabeto 2) No cierra los
     * comentarios
     * 
     * Tener en cuenta: 1) Las palabras reservadas pueden estar en mayusculas,
     * minisculas o intercalado
     */

    private static HashMap<String, String> palabrasReservadas;
    private static BufferedReader reader;
    private static FileWriter fileWriter;
    private static int caracterActual;

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
                while (caracterActual != -1) {
                    if (caracterActual == -2) {
                        caracterActual = reader.read();
                        switch (caracterActual) {
                        case '<':
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_op_relacional , op_menor_igual> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else if (caracterActual == '>') {
                                cadenaAux = "<tk_op_relacional , op_distinto> \n"; // Modificar en el informe
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_op_relacional , op_menor> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            }
                            break;

                        case '=':
                            cadenaAux = "<tk_op_relacional , op_igual> \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '>':
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_op_relacional , op_mayor_igual> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_op_relacional , op_mayor> \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
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
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                cadenaAux = "<tk_asignacion , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_dospuntos , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            }
                            break;

                        case '.':
                            caracterActual = reader.read();
                            if (caracterActual == '.') {
                                cadenaAux = "<tk_doblepunto , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            } else {
                                cadenaAux = "<tk_punto , > \n";
                                fileWriter.write(cadenaAux);
                                caracterActual = -2;
                            }
                            break;

                        case '(':
                            cadenaAux = "<tk_parentesis_izq , > \n"; // Modificar en el informe
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case ')':
                            cadenaAux = "<tk_parentesis_der , > \n"; // Modificar en el informe
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
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;

                        case '\n': // Probablemente haya que ignorar, no devolver un <newline>, pero por ahora lo
                                   // dejamos
                            cadenaAux = "<newline> \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case '\t': // Probablemente haya que ignorar, no devolver un <tab>, pero por ahora lo
                                   // dejamos
                            cadenaAux = "<tab> \n";
                            fileWriter.write(cadenaAux);
                            caracterActual = -2;
                            break;
                        case -1: // Probablemente haya que ignorar, no devolver un <fin_archivo>, pero por ahora
                                 // lo dejamos
                            cadenaAux = "<fin_archivo>";
                            fileWriter.write(cadenaAux);
                            break;

                        default: // Falta leer los comentarios
                            if (caracterActual == '{') {
                                try {
                                    leerComentario();
                                    caracterActual = -2;
                                } catch (Exception e) {
                                    e.getMessage();
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
                                        cadenaAux = "<error: caracter no valido> \n"; // Despues mostrar caracter error
                                        fileWriter.write(cadenaAux);
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
                caracterActual = reader.read();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
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
                caracterActual = reader.read();
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return constructorID.toString();
    }

    public static void leerComentario() throws Exception {
        try {
            caracterActual = reader.read();
            while (caracterActual != '}' && caracterActual != -1) {
                caracterActual = reader.read();
            }

            if (caracterActual == -1) {
                // Error: No se cerro el comentario
                throw new Exception("Comentario no fue cerrado");

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Open and read a file, and return the lines in the file as a list of Strings.
     * (Demonstrates Java FileReader, BufferedReader, and Java5.)
     * 
     * @param filename text file
     * @return list of strings where strings are each line in the file
     */
    public static ArrayList<String> readFile(String filename) {
        ArrayList<String> records = new ArrayList<String>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null) {
                records.add(line);
            }
            reader.close();
            return records;
        } catch (Exception e) {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
    }
}
