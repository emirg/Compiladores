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

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Cantidad de argumentos incorrecta");
        } else {
            try {
                reader = new BufferedReader(new FileReader(args[0]));
                FileWriter fileWriter = new FileWriter("archivoTokens.txt"); // Abre el archivo
                // fileWriter.write(fileContent);
                int caracterActual = -2;
                String aux = "";
                while (caracterActual != -1) {
                    if (caracterActual == -2) {
                        caracterActual = reader.read();
                        switch (caracterActual) {
                        case '<':
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                aux = "<tk_op_relacional , op_menor_igual> \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            } else if (caracterActual == '>') {
                                aux = "<tk_op_relacional , op_distinto> \n"; // Modificar en el informe
                                fileWriter.write(aux);
                                caracterActual = -2;
                            } else {
                                aux = "<tk_op_relacional , op_menor> \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            }
                            break;

                        case '=':
                            aux = "<tk_op_relacional , op_igual> \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '>':
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                aux = "<tk_op_relacional , op_mayor_igual> \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            } else {
                                aux = "<tk_op_relacional , op_mayor> \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            }
                            break;

                        case ',':
                            aux = "<tk_coma , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case ';':
                            aux = "<tk_puntocoma , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case ':':
                            caracterActual = reader.read();
                            if (caracterActual == '=') {
                                aux = "<tk_asignacion , > \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            } else {
                                aux = "<tk_dospuntos , > \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            }
                            break;

                        case '.':
                            caracterActual = reader.read();
                            if (caracterActual == '.') {
                                aux = "<tk_doblepunto , > \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            } else {
                                aux = "<tk_punto , > \n";
                                fileWriter.write(aux);
                                caracterActual = -2;
                            }
                            break;

                        case '(':
                            aux = "<tk_parentesis_izq , > \n"; // Modificar en el informe
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case ')':
                            aux = "<tk_parentesis_der , > \n"; // Modificar en el informe
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '-':
                            aux = "<tk_op_resta , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '+':
                            aux = "<tk_op_suma , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '/':
                            aux = "<tk_op_div , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '*':
                            aux = "<tk_op_mult , > \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;
                        case ' ':
                            aux = "<blank> \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;

                        case '\n':
                            aux = "<newline> \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;
                        case '\t':
                            aux = "<tab> \n";
                            fileWriter.write(aux);
                            caracterActual = -2;
                            break;
                        case -1:
                            aux = "<fin_archivo>";
                            fileWriter.write(aux);
                            break;

                        default: // Falta leer los comentarios
                            if ((caracterActual >= 'A' && caracterActual <= 'Z')
                                    || (caracterActual >= 'a' && caracterActual <= 'z')) {
                                String identificador = leerID();
                                if (palabrasReservadas.containsKey(identificador.toLowerCase())) {
                                    aux = palabrasReservadas.get(identificador.toLowerCase());
                                    fileWriter.write(aux);
                                    caracterActual = -2;
                                } else {
                                    aux = "<tk_id," + identificador + ">";
                                    fileWriter.write(aux);
                                    caracterActual = -2;
                                }
                            } else {
                                String numero = leerNum();
                                if (caracterActual >= '0' && caracterActual <= '9') {
                                    aux = "<tk_numero," + numero + ">";
                                    fileWriter.write(aux);
                                    caracterActual = -2;
                                } else {
                                    aux = "<error: caracter no valido>";// Despues mostrar caracter error
                                    fileWriter.write(aux);
                                    caracterActual = -1;
                                }
                            }

                        }

                    }

                }

                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void cargarPalabrasReservadas() {

    }

    public static String leerNum() {
        return "";
    }

    public static String leerID() {
        return "";
    }

    public static String leerComentario() {
        return "";
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
