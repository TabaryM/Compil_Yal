package yal.arbre.instructions;

import yal.arbre.expressions.Expression;

public class Retourne extends Instruction{
    protected Expression exp ;

    public Retourne (Expression e, int n) {
        super(n);
        exp = e;
    }


    @Override
    protected String getNomInstruction() {
        return "Retourne";
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMIPS() {
        return "\tretourne "+exp.toMIPS();
    }
}
