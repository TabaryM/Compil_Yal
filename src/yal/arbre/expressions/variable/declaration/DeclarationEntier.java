package yal.arbre.expressions.variable.declaration;

import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class DeclarationEntier extends Declaration {
    public DeclarationEntier(String idf, int noLigne) {
        super("entier_"+idf, noLigne);
    }

    @Override
    public void ajouterTDS() {
        try {
            TDS.getInstance().ajouter(new Entree(getIdf()), new SymboleDEntier(TDS.getInstance().getDepl()));
        } catch (Exception e) {
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(), "Double déclaration de la variable "+getIdf());
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public void initialisationDuCorpsDeLaVariable(StringBuilder stringBuilder) {
    }

    @Override
    public void verifier() {

    }

    @Override
    public String toMIPS() {
        SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(new Entree(getIdf())));
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\t# Déclaration de la variable ");
        stringBuilder.append(getIdf());
        stringBuilder.append("\n");
        stringBuilder.append("\tli $v0, 0\n");
        stringBuilder.append("\tsw $v0, ");
        if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeVariable.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeVariable.getDepl()-4);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
