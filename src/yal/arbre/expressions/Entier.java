package yal.arbre.expressions;

import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class Entier extends Idf {
    public Entier(String idf, int numLig) {
        super(idf, numLig);
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(new Entree("entier_"+getIdf()), new SymboleDeVariable(TDS.getInstance().getDepl()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration de la variable "+getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void verifier(){
        if(TDS.getInstance().identifier(new Entree("entier_"+getIdf())) == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Variable "+getIdf()+" non déclarée");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        Entree entree = new Entree("entier_"+getIdf());
        SymboleDeVariable symbole = ((SymboleDeVariable) TDS.getInstance().identifier(entree));
        StringBuilder stringBuilder = new StringBuilder("\tlw $v0, ");
        System.out.println("\nidf : "+getIdf());
        System.out.println("entree : "+entree);
        System.out.println("symbole : "+symbole);
        stringBuilder.append(symbole.getDepl());

        if(symbole.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append("($s7)\n");
        } else {
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
        return getIdf();
    }

    @Override
    public String getType() {
        return "entier";
    }
}
