/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AnalizadorSemantico.ElementosTS;

/**
 *
 * @author emiliano
 */
public class FilaVariable extends Fila{
    
    private String tipo; // Integer o Boolean

    public FilaVariable(String tipo, String tipoConstructor, String nombre, int lineaDeclaracion) {
        super(tipoConstructor, nombre, lineaDeclaracion);
        this.tipo = tipo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
    
    
}
