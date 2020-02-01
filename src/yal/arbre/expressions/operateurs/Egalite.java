package yal.arbre.expressions.operateurs;

public class Egalite implements Operateur {
    public Egalite() {}

    @Override
    public String toMips() {
        return "seq";
    }

    @Override
    public String toString() {
        return "==";
    }
}
