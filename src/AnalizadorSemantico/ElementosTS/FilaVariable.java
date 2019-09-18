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
public class FilaVariable extends Fila {

    private String tipo; // Integer o Boolean
    private boolean esParametro;

    public FilaVariable(String tipoConstructor, String nombre, int lineaDeclaracion, String tipo, boolean esParametro) {
        super(tipoConstructor, nombre, lineaDeclaracion);
        this.tipo = tipo;
        this.esParametro = esParametro;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public boolean esParametro() {
        return esParametro;
    }

    public void setEsParametro(boolean esParametro) {
        this.esParametro = esParametro;
    }

}
