package yal.arbre.expressions.operateurs;

public class Multiplication implements Operateur {
    public Multiplication() {}

    @Override
    public String toMips() {
        return "mul";
    }

    @Override
    public String toString() {
        return "*";
    }
}
