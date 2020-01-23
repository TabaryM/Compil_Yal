package yal.arbre.declaration;

public class Symbole {
    private String type;
    private int depl;

    public Symbole(String type, int depl) {
        this.type = type;
        this.depl = depl;
    }

    public String getType() {
        return type;
    }

    public int getDepl() {
        return depl;
    }
}
