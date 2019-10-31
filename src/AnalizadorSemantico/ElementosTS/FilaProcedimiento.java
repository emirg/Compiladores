package AnalizadorSemantico.ElementosTS;

import java.util.ArrayList;

public class FilaProcedimiento extends Fila {

    private ArrayList<String> listaParametros;

    public FilaProcedimiento(String tipoConstructor, String nombre, int lineaDeclaracion, ArrayList listaParametros,String label,int nivel) {
        super(tipoConstructor, nombre, lineaDeclaracion,label,nivel);
        this.listaParametros = listaParametros;
    }

   
    
    public ArrayList getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(ArrayList listaParametros) {
        this.listaParametros = listaParametros;
    }
    
    
}
