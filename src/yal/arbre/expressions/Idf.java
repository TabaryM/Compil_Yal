package yal.arbre.expressions;

public abstract class Idf extends Expression{
    private String idf;

    public Idf(String idf, int numLig) {
        super(numLig);
        this.idf = idf;
    }

    public String getIdf() {
        return idf;
    }

}
