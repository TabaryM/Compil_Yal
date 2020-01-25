package yal.arbre.expressions;

import yal.arbre.declaration.Entree;
import yal.arbre.declaration.Symbole;
import yal.arbre.declaration.TDS;

public abstract class Idf extends Expression{
    private String idf;
    private int numLig;

    public Idf(String idf, int numLig) {
        super(numLig);
        this.idf = idf;
        this.numLig = numLig;
    }

    public String getIdf() {
        return idf;
    }

    public int getNumLig() {
        return numLig;
    }
}
