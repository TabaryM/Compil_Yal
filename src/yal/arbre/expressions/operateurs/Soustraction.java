package yal.arbre.expressions.operateurs;

public class Soustraction implements Operateur {
    public Soustraction() {}

    @Override
    public String toMips() {
        return "sub";
    }

    @Override
    public String toString() {
        return "-";
    }
}
