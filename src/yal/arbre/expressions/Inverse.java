package yal.arbre.expressions;

import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

public class Inverse extends Expression {
    private Expression expression;
    public Inverse(Expression e, int n) {
        super(n);
        expression = e;
    }

    @Override
    public void verifier() {
        if(expression.getType().equals("entier")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Incompatibilité de types : négation booléenne d'un entier impossible");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Ecriture du beau commentaire MIPS
        stringBuilder.append("\t# Inversion de l'expression booléenne :");
        stringBuilder.append(expression.toString());
        stringBuilder.append("\n");

        // On stocke l'opérande gauche dans la pile
        stringBuilder.append(expression.toMIPS());

        // On fait l'opération
        stringBuilder.append("\txori $v0, $v0, 1\n\n");

        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    @Override
    public String toString() {
        return "non "+expression.toString();
    }

    @Override
    public String getType() {
        return "bool";
    }
}
