package yal.arbre.instructions;

import yal.arbre.BlocDInstructions;
import yal.arbre.declaration.ErreurSemantique;
import yal.arbre.expressions.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Boucle extends Instruction {
    private Expression expression;
    private BlocDInstructions blocDInstructions;
    protected Boucle(Expression e, BlocDInstructions b, int n) {
        super(n);
        expression = e;
        blocDInstructions = b;
    }

    @Override
    protected String getNomInstruction() {
        return "boucle tant que";
    }

    @Override
    public void verifier() {
        if(!expression.getType().equals("bool")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Condition de boucle non booléenne : "+expression.toString());
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(blocDInstructions.getNbInstructions() < 1){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Boucle sans instruction : requiert au minimum une instruction");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        return null;
    }
}
