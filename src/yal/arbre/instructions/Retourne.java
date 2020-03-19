package yal.arbre.instructions;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

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
        if(!exp.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Type de retour incorrect. Attendu : entier Trouvé : "+exp.getType());
            ErreurSemantique.getInstance().ajouter(exception);

        }

    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // On évalue l'expression à retourner
        stringBuilder.append(exp.toMIPS());
        stringBuilder.append("\tlw $a0, 0($s2)\n");
        stringBuilder.append("\tjr $a0\t#On retourne la où la fonction à été appelée\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return true;
    }
}
