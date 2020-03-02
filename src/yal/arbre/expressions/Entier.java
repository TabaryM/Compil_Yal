package yal.arbre.expressions;

import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class Entier extends Idf {
    public Entier(String idf, int numLig) {
        super(idf, numLig);
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(new Entree(super.getIdf()), new SymboleDeVariable(TDS.getInstance().getCpt()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la variable "+super.getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
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
        StringBuilder stringBuilder = new StringBuilder("\tlw $v0, ");

        // On récupère le déplacement dans la mémoire
        SymboleDeVariable symbole = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree(getIdf())));
        int deplacement = symbole.getDepl();

        stringBuilder.append(deplacement);
        stringBuilder.append("($s7)\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne(boolean dansUneFonction) {
        return false;
    }

    @Override
    public String toString() {
        return super.getIdf();
    }

    @Override
    public String getType() {
        return "entier";
    }
}
