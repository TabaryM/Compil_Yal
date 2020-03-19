package yal.arbre.declaration;

import yal.arbre.gestionnaireTDS.Entree;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.gestionnaireTDS.SymboleDeVariable;
import yal.arbre.gestionnaireTDS.TDS;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationEntier extends Declaration {
    public DeclarationEntier(String idf, int noLigne) {
        super(idf, noLigne);
    }

    @Override
    public void ajouterTDS() {
        try {
            TDS.getInstance().ajouter(new Entree("entier_"+getIdf()), new SymboleDeVariable(TDS.getInstance().getDepl()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration de la variable "+getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMIPS() {
        return "";
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
