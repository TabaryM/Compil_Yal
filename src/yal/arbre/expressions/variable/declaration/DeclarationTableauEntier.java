package yal.arbre.expressions.variable.declaration;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationTableauEntier extends Declaration {

    private Expression expression;

    public DeclarationTableauEntier(String idf, Expression expression, int noLigne) {
        super(idf, noLigne);
        this.expression = expression;
    }

    @Override
    public void ajouterTDS() {

    }

    @Override
    public void verifier() {
        if(!expression.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Format de la taille du tableau incorrecte. Attendue : entier\tReçu : "+expression.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        return "";
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
