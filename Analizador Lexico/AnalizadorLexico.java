import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.rmi.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author emiliano
 */

public class AnalizadorLexico {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Cantidad de argumentos incorrecta");
        } else {
            ArrayList lines = (ArrayList) readFile(args[0]);

        }
    }

    /**
     * Open and read a file, and return the lines in the file as a list of Strings.
     * (Demonstrates Java FileReader, BufferedReader, and Java5.)
     * 
     * @param filename text file
     * @return list of strings where strings are each line in the file
     */
    public static List<String> readFile(String filename) {
        List<String> records = new ArrayList<String>();
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
