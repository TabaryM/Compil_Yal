package yal.arbre.expressions.operateurs;

public class Soustraction implements Operateur {
    public Soustraction() {}

    @Override
    public String toMips() {
        return "\tsub $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "-";
    }

    @Override
    public String getNatureRetour() {
        return "entier";
    }
}
