package yal.arbre.instructions;

import yal.arbre.expressions.Expression;

public class AffectationTableau extends Affectation {
    public AffectationTableau(Expression e, Expression indice, String idf, int n) {
        super(e, idf, n);
    }
}
