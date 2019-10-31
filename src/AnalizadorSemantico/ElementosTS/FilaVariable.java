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
    private int offset;


    public FilaVariable(String tipoConstructor, String nombre, int lineaDeclaracion,String label, String tipo, boolean esParametro,int offset,int nivel) {
        super(tipoConstructor, nombre, lineaDeclaracion,label,nivel);
        this.tipo = tipo;
        this.esParametro = esParametro;
        this.offset=offset;
    }

    public String getTipo() {
        return tipo;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
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
    
    @Override
    public boolean equals(Object f){
       FilaVariable fila= (FilaVariable) f;
       return (fila.getNombre().equalsIgnoreCase(nombre) );
    }

}
