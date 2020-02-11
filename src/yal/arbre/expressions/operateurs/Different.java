package yal.arbre.expressions.operateurs;

public class Different implements Operateur {
    public Different() {}

    @Override
    public String toMips() {
        return "\tsne $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "!=";
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
