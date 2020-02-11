package yal.arbre.expressions.operateurs;

public class Addition implements Operateur {
    public Addition() {}

    @Override
    public String toMips() {
        return "\tadd $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "+";
    }

    @Override
    public String getNatureRetour() {
        return "entier";
    }
}
