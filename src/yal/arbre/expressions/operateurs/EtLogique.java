package yal.arbre.expressions.operateurs;

public class EtLogique implements Operateur {
    public EtLogique() {}

    @Override
    public String toMips() {
        return "and";
    }

    @Override
    public String toString() {
        return "et";
    }
}
