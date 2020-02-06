package yal.arbre.expressions.operateurs;

public class Addition implements Operateur {
    public Addition() {}

    @Override
    public String toMips() {
        return "add";
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
