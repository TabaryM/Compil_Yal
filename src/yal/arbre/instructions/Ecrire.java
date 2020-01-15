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
        return "li $v0, 1\n" +
                "li $a0, "+exp.toMIPS()+"\n"+
                "syscall\n";
        //throw new UnsupportedOperationException("Not supported yet.");
    }

}
