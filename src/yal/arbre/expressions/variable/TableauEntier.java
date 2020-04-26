package yal.arbre.expressions.variable;

import yal.arbre.expressions.Expression;
import yal.arbre.expressions.Idf;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

public class TableauEntier extends Idf {

    private Expression expression;

    public TableauEntier(String idf, Expression expression, int numLig) {
        super("tableau_"+idf, numLig);
        this.expression = expression;
    }

    @Override
    public String getType() {
        return "tableau";
    }

    @Override
    public void verifier() {
        // TODO : vérifier les bornes du tableau
        // TODO : vérifier que le tableau existe
        if(!expression.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Format de l'indexe du tableau incorrecte. Attendue : entier\tReçu : "+expression.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }

    }

    @Override
    public String toMIPS() {
        return null;
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
