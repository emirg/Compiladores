package AnalizadorSemantico.ElementosTS;

import java.util.ArrayList;

public class FilaProcedimiento extends Fila {

    private ArrayList listaParametros;

    public FilaProcedimiento(ArrayList listaParametros, String tipoConstructor, String nombre, int lineaDeclaracion) {
        super(tipoConstructor, nombre, lineaDeclaracion);
        this.listaParametros = listaParametros;
    }

    public ArrayList getListaParametros() {
        return listaParametros;
    }

    public void setListaParametros(ArrayList listaParametros) {
        this.listaParametros = listaParametros;
    }
    
    
}
