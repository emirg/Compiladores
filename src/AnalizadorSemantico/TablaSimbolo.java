package AnalizadorSemantico;

import AnalizadorSemantico.ElementosTS.Fila;
import java.util.HashMap;

public class TablaSimbolo {
    
    private HashMap<String, Fila> simbolos;
    
    public TablaSimbolo(){
        simbolos = new HashMap();
    }
    
    public void agregarSimbolo(String key, Fila value){
        simbolos.put(key, value);
    }
    
    public Fila obtenerSimbolo(String key){
        return simbolos.get(key);
    }
    
    public void cambiarSimbolo(String key, Fila value){
        simbolos.replace(key, value);
    }
    
    public boolean existeSimbolo(String key){
        return simbolos.containsKey(key);
    }
}
