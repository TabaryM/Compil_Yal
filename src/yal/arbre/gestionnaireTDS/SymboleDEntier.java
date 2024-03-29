package yal.arbre.gestionnaireTDS;

public class SymboleDEntier extends SymboleDeVariable{
    /**
     * Instancie un symbole à ranger dans la table des symboles
     *
     * @param depl déplacement par rapport au sommet de la pile initial
     */
    public SymboleDEntier(int depl) {
        super(depl, "entier");
    }

    @Override
    public void depilageToMIPS(StringBuilder stringBuilder) {
        stringBuilder.append("\taddi $sp, $sp, 4\n");
    }
}
