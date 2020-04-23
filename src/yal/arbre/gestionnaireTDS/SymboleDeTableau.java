package yal.arbre.gestionnaireTDS;

public class SymboleDeTableau extends SymboleDeVariable {

    /**
     * Instancie un symbole à ranger dans la table des symboles
     *
     * @param depl déplacement par rapport au sommet de la pile initial
     */
    public SymboleDeTableau(int depl) {
        super(depl, "tableau");
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
