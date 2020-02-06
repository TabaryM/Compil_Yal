package yal.arbre.expressions.operateurs;

public class InferieurA implements Operateur {
    public InferieurA() {}

    @Override
    public String toMips() {
        return "slt";
    }

    @Override
    public String toString() {
        return "<";
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
