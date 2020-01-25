package yal.arbre.expressions;

import yal.arbre.declaration.Entree;
import yal.arbre.declaration.ErreurSemantique;
import yal.arbre.declaration.Symbole;
import yal.arbre.declaration.TDS;
import yal.exceptions.AnalyseSemantiqueException;

public class Entier extends Idf {
    public Entier(String idf, int numLig) {
        super(idf, numLig);
    }

    public void ajouterTDS(){
        try {
            TDS.getInstance().ajouter(new Entree(super.getIdf()), new Symbole("entier", TDS.getInstance().getCpt()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Double déclaration de la variable "+super.getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder("\tlw $v0, ");
        stringBuilder.append(TDS.getInstance().identifier(new Entree((getIdf()))).getDepl());
        stringBuilder.append("($s7)\n");
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        return super.getIdf();
    }
}
