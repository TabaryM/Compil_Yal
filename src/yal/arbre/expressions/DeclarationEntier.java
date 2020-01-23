package yal.arbre.expressions;

import yal.arbre.declaration.Entree;
import yal.arbre.declaration.ErreurSemantique;
import yal.arbre.declaration.Symbole;
import yal.arbre.declaration.TDS;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationEntier extends Idf {
    public DeclarationEntier(String idf, int numLig) {
        super(idf, numLig);
        try {
            TDS.getInstance().ajouter(new Entree(idf), new Symbole("entier", TDS.getInstance().getCpt()));
        } catch (Exception e) {
            AnalyseSemantiqueException tmp = new AnalyseSemantiqueException(numLig, "Double déclaration de la variable "+idf);
            ErreurSemantique.getInstance().ajouter(tmp);
        }
    }
}
