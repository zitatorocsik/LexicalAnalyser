public class Token{

    private String type;
    private String lexeme;
    private String location;

    public Token() {
        this.type = "empty";
        this.lexeme = "";
        this.location = "";
    }
    public Token(String type, String lexeme, String location) {
        this.type = type;
        this.lexeme = lexeme;
        this.location = location;
    }


    public String toString() {
        return "[" + this.type + ", " + this.lexeme + ", " + this.location + "]";
    }

    public String getType() {
        return type;
    }


    public void setType(String type) {
        this.type = type;
    }


    public String getLexeme() {
        return lexeme;
    }


    public void setLexeme(String lexeme) {
        this.lexeme = lexeme;
    }


    public String getLocation() {
        return location;
    }


    public void setLocation(String location) {
        this.location = location;
    }

  






}