package AnalizadorSemantico.ElementosTS;

public class Fila {
    
    private String tipoConstructor;
    protected String nombre;
    private int lineaDeclaracion;
    private String label;
    private int nivel;

    public Fila(String tipoConstructor, String nombre, int lineaDeclaracion,String label,int nivel) {
        this.tipoConstructor = tipoConstructor;
        this.nombre = nombre;
        this.lineaDeclaracion = lineaDeclaracion;
        this.label=label;
        this.nivel=nivel;
    }
    
      public int getNivel() {
        return this.nivel;
    }
    

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }
    
     public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setTipoConstructor(String tipoConstructor) {
        this.tipoConstructor = tipoConstructor;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setLineaDeclaracion(int lineaDeclaracion) {
        this.lineaDeclaracion = lineaDeclaracion;
    }

    public String getTipoConstructor() {
        return tipoConstructor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getLineaDeclaracion() {
        return lineaDeclaracion;
    }
    
    
}
