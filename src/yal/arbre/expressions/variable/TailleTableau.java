package yal.arbre.expressions.variable;

import yal.arbre.expressions.Idf;
import yal.arbre.gestionnaireTDS.*;
import yal.exceptions.AnalyseSemantiqueException;

public class TailleTableau extends Idf {

    public TailleTableau(String idf, int n) {
        super(idf, n);
    }

    @Override
    public String getType() {
        return "entier";
    }

    @Override
    public void verifier() {
        Symbole symbole = TDS.getInstance().identifier(new Entree(getIdf()));
        if(symbole == null){
            AnalyseSemantiqueException exception = new AnalyseSemantiqueException(super.getNoLigne(), "Tableau "+getIdf()+" non déclaré.");
            ErreurSemantique.getInstance().ajouter(exception);
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        // Récupérer le déplacement dans la pile de p-tab (pointeur du tableau)
        SymboleDeVariable symboleDeVariable = (SymboleDeVariable)(TDS.getInstance().identifier(new Entree(getIdf())));
        stringBuilder.append("\tlw $v0, ");
        if(symboleDeVariable.getNumBloc() == TDS.getInstance().getRacine().getNumBloc()){
            stringBuilder.append(symboleDeVariable.getDepl());
            stringBuilder.append("($s7)");
        } else {
            stringBuilder.append(symboleDeVariable.getDepl()-4);
            stringBuilder.append("($s2)");
        }
        stringBuilder.append("\n");
        // Récupérer la valeur à l'adresse pointée par p-tab (et la charger dans $v0)
        stringBuilder.append("\tlw $v0, ($v0)\n");
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        return false;
    }
}
