package yal.arbre.expressions.operateurs;

public class InferieurA implements Operateur {
    public InferieurA() {}

    @Override
    public String toMips() {
        return "\tslt $v0, $t8, $v0";
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
