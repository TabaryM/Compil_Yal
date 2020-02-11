package yal.arbre.expressions.operateurs;

public class Multiplication implements Operateur {
    public Multiplication() {}

    @Override
    public String toMips() {
        return "\tmul $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "*";
    }

    @Override
    public String getNatureRetour() {
        return "entier";
    }
}
