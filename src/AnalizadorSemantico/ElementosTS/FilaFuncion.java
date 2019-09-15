/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorSemantico.ElementosTS;

import java.util.ArrayList;

/**
 *
 * @author emiliano
 */
public class FilaFuncion extends FilaProcedimiento {
    
    private String tipoRetorno;

    public FilaFuncion(String tipoRetorno, ArrayList listaParametros, String tipoConstructor, String nombre, int lineaDeclaracion) {
        super(listaParametros, tipoConstructor, nombre, lineaDeclaracion);
        this.tipoRetorno = tipoRetorno;
    }

    public String getTipoRetorno() {
        return tipoRetorno;
    }

    public void setTipoRetorno(String tipoRetorno) {
        this.tipoRetorno = tipoRetorno;
    }
    
    

}
