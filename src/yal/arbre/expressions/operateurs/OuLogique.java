package yal.arbre.expressions.operateurs;

public class OuLogique implements Operateur {
    public OuLogique() {}

    @Override
    public String toMips() {
        return "or";
    }

    @Override
    public String toString() {
        return "ou";
    }
}
