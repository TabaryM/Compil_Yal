package yal.arbre.instructions;

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

    }

    @Override
    public String toMIPS() {
        return null;
    }
}
