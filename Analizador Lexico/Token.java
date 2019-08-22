public class Token{

    private String nombreToken;
    private String atributoToken;

    public Token(){
        this.nombreToken = "";
        this.atributoToken = "";
    }

    public Token(String nombreToken){
        this.nombreToken = nombreToken;
        this.atributoToken = "";
    }

    public Token(String nombreToken, String atributoToken){
        this.nombreToken = nombreToken;
        this.atributoToken = atributoToken;
    }

    public String getNombreToken(){
        return nombreToken;
    }

    public String getAtributoToken(){
        return atributoToken;
    }

    public void setNombreToken(String nombreToken){
        this.nombreToken = nombreToken;
    }

    public void setAtributoToken(String atributoToken){
        this.atributoToken = atributoToken;
    }

    public boolean equals(Token t){
        return t.getNombreToken().equalsIgnoreCase(nombreToken) && t.getAtributoToken().equalsIgnoreCase(atributoToken);
    }

    public String toString(){
        return "<" + nombreToken + " , " + atributoToken + ">";
    }

    

}