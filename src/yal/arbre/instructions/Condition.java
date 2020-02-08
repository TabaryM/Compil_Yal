package yal.arbre.instructions;

import yal.arbre.expressions.Expression;

public class Condition extends Instruction {
    protected Condition(int n) {
        super(n);
    }

    @Override
    protected String getNomInstruction() {
        return null;
    }

    @Override
    public void verifier() {
        // exp doit être booléen
    }

    @Override
    public String toMIPS() {
        return null;
    }
}
