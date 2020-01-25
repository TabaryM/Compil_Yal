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
        return "\t# Ecrire la valeur : "+exp.toString()+"\n"+
                // On charge dans $v0 la valeur que l'on veut afficher
                exp.toMIPS()+
                // On met cette valeur dans $a0 parceque c'est comme ça que MIPS affiche
                "\tmove $a0, $v0\n"+
                // On dit à MIPS que l'on veut appeller la commande "afficher"
                "\tli $v0, 1\n" +
                "\tsyscall\n"+
                // On ajoute une nouvelle ligne pour la lisibilité
                "\n\t# new line\n"+
                "\tli $v0, 4\n" +
                "\tla $a0, newline\n" +
                "\tsyscall\n";
    }

    @Override
    protected String getNomInstruction() {
        return "Ecrire";
    }
}
