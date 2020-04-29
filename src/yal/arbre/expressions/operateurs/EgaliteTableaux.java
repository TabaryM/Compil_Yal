package yal.arbre.expressions.operateurs;

import yal.FabriqueDeNumero;

public class EgaliteTableaux implements Operateur {

    private final boolean egalite;

    public EgaliteTableaux(boolean egalite){
        this.egalite = egalite;
    }

    @Override
    public String toMips() {
        StringBuilder stringBuilder = new StringBuilder();
        int numLabel = FabriqueDeNumero.getInstance().getNumeroLabelCondition();
        // On passe en MIPS
        stringBuilder.append("\tjal TestDimTabs\n");
        // Si le premier test échoue, on skip le second test
        stringBuilder.append("\tblez $v0, finComparaisonTabs").append(numLabel);

        stringBuilder.append("\n\tjal TestValsTabs\n");

        stringBuilder.append("finComparaisonTabs");
        stringBuilder.append(numLabel);
        stringBuilder.append(":\n");
        if(!egalite){
            // Si on demande une inégalité, on inverse le résultat de l'égalité
            stringBuilder.append("\txori $v0, $v0, 1\n");
        }
        return stringBuilder.toString();
    }

    @Override
    public String getNatureRetour() {
        return "bool";
    }
}
