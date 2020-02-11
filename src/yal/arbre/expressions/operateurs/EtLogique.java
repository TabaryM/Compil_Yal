package yal.arbre.expressions.operateurs;

public class EtLogique implements Operateur {
    public EtLogique() {}

    @Override
    public String toMips() {
        return "\tand $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "et";
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
