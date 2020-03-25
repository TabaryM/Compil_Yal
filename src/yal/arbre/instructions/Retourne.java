package yal.arbre.instructions;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.gestionnaireTDS.SymboleDeFonction;
import yal.arbre.gestionnaireTDS.TDS;
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
                    "Type de retour incorrect. Attendu : entier. Trouvé : "+exp.getType());
            ErreurSemantique.getInstance().ajouter(exception);

        }

    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        int nbVarLoc = TDS.getInstance().getTableCourrante().getNbVariableLocales();

        // On évalue l'expression à retourner
        stringBuilder.append(exp.toMIPS());

        // Retour au bloc principal, on nettoie la pile des variables locales
        stringBuilder.append("\t#On dépile tout ce que l'on a empilé durant l'appel de la fonction\n");
        stringBuilder.append("\taddi $sp, $sp, ");
        stringBuilder.append(4*nbVarLoc+8);
        stringBuilder.append("\n");

        // On chainge dynamique en arrière
        stringBuilder.append("\tlw $a0, 0($sp)\n");

        stringBuilder.append("\tjr $a0\t#On retourne à l'appel de la fonction\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return true;
    }
}
