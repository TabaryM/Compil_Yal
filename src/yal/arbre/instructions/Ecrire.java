package yal.arbre.instructions;

import yal.arbre.expressions.Expression;

public class Ecrire extends Instruction {

    protected Expression exp ;

    public Ecrire (Expression e, int n) {
        super(n) ;
        exp = e ;
    }

    @Override
    public void verifier() {
    }

    @Override
    public String toMIPS() {
        return "\t# "+getNomInstruction()+" la valeur : "+exp.toMIPS()+"\n"+
                "\tli $v0, 1\n" +
                "\tli $a0, "+exp.toMIPS()+"\n"+
                "\tsyscall\n";
    }

    @Override
    protected String getNomInstruction() {
        return "Ecrire";
    }
}
