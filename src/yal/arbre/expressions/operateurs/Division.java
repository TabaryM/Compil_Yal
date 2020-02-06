package yal.arbre.expressions.operateurs;

public class Division implements Operateur{
    public Division() {}

    @Override
    public String toMips() {
        return "div";
    }

    @Override
    public String toString() {
        return "/";
    }
}
