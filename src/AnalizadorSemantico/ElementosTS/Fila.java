package AnalizadorSemantico.ElementosTS;

public class Fila {
    
    private String tipoConstructor;
    protected String nombre;
    private int lineaDeclaracion;
    private String label;

    public Fila(String tipoConstructor, String nombre, int lineaDeclaracion,String label) {
        this.tipoConstructor = tipoConstructor;
        this.nombre = nombre;
        this.lineaDeclaracion = lineaDeclaracion;
        this.label=label;
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
