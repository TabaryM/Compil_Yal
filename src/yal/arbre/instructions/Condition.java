package yal.arbre.instructions;

import yal.FabriqueDeNumero;
import yal.arbre.ArbreAbstrait;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.expressions.Expression;
import yal.exceptions.AnalyseSemantiqueException;

public class Condition extends Instruction {
    private Expression expression;
    private  ArbreAbstrait arbreAbstraitSi;
    private  ArbreAbstrait arbreAbstraitSinon;
    private boolean sinon = false;

    public Condition(Expression e, ArbreAbstrait asi, int n) {
        super(n);
        expression = e;
        arbreAbstraitSi = asi;
    }
    public Condition(Expression e, ArbreAbstrait asi, ArbreAbstrait asinon, int n) {
        super(n);
        expression = e;
        arbreAbstraitSi = asi;
        arbreAbstraitSinon = asinon;
        sinon = true;
    }

    @Override
    protected String getNomInstruction() {
        return "Si ";
    }

    @Override
    public void verifier() {
        if(!expression.getType().equals("bool")){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Condition non booléenne : "+expression.toString());
            ErreurSemantique.getInstance().ajouter(exception);
        }
        if(sinon && arbreAbstraitSinon == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                    "Sinon ne peut pas être vide : "+expression.toString());
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
        //condition
        stringBuilder.append(expression.toMIPS());
        stringBuilder.append("beqz $v0, siFaux");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");
        //alors
        if(null != arbreAbstraitSi){
            stringBuilder.append(arbreAbstraitSi.toMIPS());
        }

        //jump a l'etiquette
        stringBuilder.append("\tj finSi");
        stringBuilder.append(numLabel);
        stringBuilder.append("\n");

        //sinon
        //etiquette sinon
        stringBuilder.append("siFaux");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        if(sinon){
            stringBuilder.append(arbreAbstraitSinon.toMIPS());
        }

        //finsi
        stringBuilder.append("finSi");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne(boolean dansUneFonction) {
        if(arbreAbstraitSinon != null){
            return arbreAbstraitSi.contientRetourne(dansUneFonction) || arbreAbstraitSinon.contientRetourne(dansUneFonction);
        } else {
            return arbreAbstraitSi.contientRetourne(dansUneFonction);
        }
    }
}
