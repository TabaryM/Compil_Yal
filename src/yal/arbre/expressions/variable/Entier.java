package yal.arbre.expressions.variable;

import yal.arbre.expressions.Idf;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class Entier extends Idf {
    public Entier(String idf, int numLig) {
        super(idf, numLig);
    }

    @Override
    public void verifier(){
        if(TDS.getInstance().identifier(new Entree(getIdf())) == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Variable "+getIdf()+" non déclarée");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        Entree entree = new Entree(getIdf());
        SymboleDeVariable symbole = ((SymboleDeVariable) TDS.getInstance().identifier(entree));
        StringBuilder stringBuilder = new StringBuilder("\tlw $v0, ");
        if(symbole.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symbole.getDepl());
            stringBuilder.append("($s7)\n");
        } else {
            stringBuilder.append(symbole.getDepl()-4);
            stringBuilder.append("($s2)\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }

    @Override
    public String toString() {
        return getType()+"_"+getIdf();
    }

    @Override
    public String getType() {
        return "entier";
    }
}
