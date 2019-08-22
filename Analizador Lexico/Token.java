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
        return this.nombreToken;
    }

    public String getAtributoToken(){
        return this.atributoToken;
    }

    public void setNombreToken(String nombreToken){
        this.nombreToken = nombreToken;
    }

    public void setAtributoToken(String atributoToken){
        this.atributoToken = atributoToken;
    }

    public boolean equals(Token t){
        return t.getNombreToken().equalsIgnoreCase(this.nombreToken) && t.getAtributoToken().equalsIgnoreCase(this.atributoToken);
    }

    public String toString(){
        return "<" + nombreToken + " , " + atributoToken + ">";
    }

    

}