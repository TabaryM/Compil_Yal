package yal.arbre.expressions.variable;

import yal.arbre.expressions.Idf;
import yal.arbre.gestionnaireTDS.Entree;
import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.arbre.gestionnaireTDS.Symbole;
import yal.arbre.gestionnaireTDS.TDS;
import yal.exceptions.AnalyseSemantiqueException;

public class Variable extends Idf {
    public Variable(String idf, int numLig) {
        super(idf, numLig);
    }

    @Override
    public String getType() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        return symbole.getType();
    }

    @Override
    public void verifier() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        if(symbole == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Variable "+getIdf()+" non déclaré.");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        if(symbole.getType().equals("entier")){
            return new Entier(getIdf(), getNoLigne()).toMIPS();
        } else if (symbole.getType().equals("tableau")){
            return new TableauEntier(getIdf(), getNoLigne()).toMIPS();
        } else {
            return "";
        }
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
