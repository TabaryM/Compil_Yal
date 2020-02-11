package yal.arbre.expressions.operateurs;

public class Egalite implements Operateur {
    public Egalite() {}

    @Override
    public String toMips() {
        return "\tseq $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "==";
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
