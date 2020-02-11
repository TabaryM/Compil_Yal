package yal.arbre.expressions.operateurs;

public class SuperieurA implements Operateur {
    public SuperieurA() {}

    @Override
    public String toMips() {
        return "\tsgt $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return ">";
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
