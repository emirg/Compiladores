package AnalizadorSemantico;

import java.util.HashMap;

public class TablaSimbolo {
    
    private HashMap<String, String> simbolos;
    
    public TablaSimbolo(){
        simbolos = new HashMap();
    }
    
    public void agregarSimbolo(String key, String value){
        simbolos.put(key, value);
    }
    
    public String obtenerSimbolo(String key){
        return simbolos.get(key);
    }
    
    public void cambiarSimbolo(String key, String value){
        simbolos.replace(key, value);
    }
    
    public boolean existeSimbolo(String key){
        return simbolos.containsKey(key);
    }
}
