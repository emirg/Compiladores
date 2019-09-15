package AnalizadorSemantico.ElementosTS;

public class Fila {
    
    private String tipoConstructor;
    private String nombre;
    private int lineaDeclaracion;

    public Fila(String tipoConstructor, String nombre, int lineaDeclaracion) {
        this.tipoConstructor = tipoConstructor;
        this.nombre = nombre;
        this.lineaDeclaracion = lineaDeclaracion;
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
