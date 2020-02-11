package yal.arbre.expressions.operateurs;

public class Division implements Operateur{
    public Division() {}

    @Override
    public String toMips() {
        return "\tdiv $v0, $t8, $v0";
    }

    @Override
    public String toString() {
        return "/";
    }

    @Override
    public String getNatureRetour() {
        return "entier";
    }
}
