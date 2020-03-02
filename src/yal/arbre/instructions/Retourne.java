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
        StringBuilder stringBuilder = new StringBuilder();
        // On évalue l'expression à retourner
        stringBuilder.append(exp.toMIPS());
        // On réinitialise la pile à sa position avant l'appel de la fonction
        stringBuilder.append("\t#On dépile tout ce que l'on a empilé durant l'appel de la fonction\n");
        stringBuilder.append("\taddi $sp, $sp, 8\n");
        stringBuilder.append("\tlw $a0, ($sp)\n");
        stringBuilder.append("\t#On retourne la où la fonction à été appelée\n");
        stringBuilder.append("\tjr $a0\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne(boolean dansUneFonction) {
        return dansUneFonction;
    }
}
