package yal.arbre.expressions.operateurs;

public class SuperieurA implements Operateur {
    public SuperieurA() {}

    @Override
    public String toMips() {
        return "sgt";
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
