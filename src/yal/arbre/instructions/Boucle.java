package yal.arbre.instructions;

import yal.FabriqueDeNumero;
import yal.arbre.ArbreAbstrait;
import yal.arbre.declaration.ErreurSemantique;
import yal.arbre.expressions.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Boucle extends Instruction {
    private Expression expression;
    private ArbreAbstrait arbreAbstrait;
    public Boucle(Expression e, ArbreAbstrait a, int n) {
        super(n);
        expression = e;
        arbreAbstrait = a;
    }

    @Override
    protected String getNomInstruction() {
        return "boucle tant que ";
    }

    @Override
    public void verifier() {
        if(!expression.getType().equals("bool")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Condition de boucle non booléenne : "+expression.toString());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        int numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#");
        stringBuilder.append(getNomInstruction());
        stringBuilder.append(expression.toString());
        stringBuilder.append("\n");
        //etiquette
        stringBuilder.append("tantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");

        //condition
        stringBuilder.append(expression.toMIPS());

        //si faux on sort
        stringBuilder.append("\tbeq $v0,$zero,finTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");
        //contenu boucle
        stringBuilder.append("#Alors\n");
        stringBuilder.append(arbreAbstrait.toMIPS());
        //jump a l'etiquette
        stringBuilder.append("\tj tantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");

        //fintantque
        stringBuilder.append("finTantQue");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n\n");

        return stringBuilder.toString();
    }
}
