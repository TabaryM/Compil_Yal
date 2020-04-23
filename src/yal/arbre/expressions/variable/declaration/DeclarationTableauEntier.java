package yal.arbre.expressions.variable.declaration;

import yal.arbre.expressions.Expression;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationTableauEntier extends Declaration {
    private Expression expression;

    public DeclarationTableauEntier(String idf, Expression expression, int noLigne) {
        super(idf, noLigne);
        this.expression = expression;
    }

    @Override
    public void ajouterTDS() {
        Entree entree = new Entree("tableau_"+getIdf());
        SymboleDeTableau symboleDeTableau;
        symboleDeTableau = new SymboleDeTableau(TDS.getInstance().getDepl());
        try {
            TDS.getInstance().ajouter(entree, symboleDeTableau);
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration du tableau : "+getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void verifier() {
        if(!expression.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Format de la taille du tableau incorrecte. Attendue : entier\tReçu : "+expression.getType());
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(TDS.getInstance().getTableCourrante().equals(TDS.getInstance().getRacine())){
            if(!expression.getClass().getSimpleName().equals("ConstanteEntiere")){
                AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Expression de la dimension du tableau "+getIdf()+" incorrecte. Attendue : constante entiere.\tReçu : "+expression.getClass().getSimpleName());
                ErreurSemantique.getInstance().ajouter(exception);
            }
        }
        //TODO : si on est pas dans le bloc principale...
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t# Déclaration du tableau ");
        stringBuilder.append(getIdf());
        stringBuilder.append("\n");
        stringBuilder.append("\tmove $t8, $s6\t# On récupère l'adresse du sommet de tableau dans le tas\n");
        stringBuilder.append(expression.toMIPS());
        stringBuilder.append("\tadd $gp, $gp, $v0\t# On aloue la place nécessaire dans le tas pour le tableau\n");
        stringBuilder.append("\tsw $t8, 0($sp)\t# On empile l'adresse dans le tas du tableau\n");
        stringBuilder.append("\taddi $sp, $sp, -4\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
