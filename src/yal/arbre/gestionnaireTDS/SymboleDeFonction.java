package yal.arbre.gestionnaireTDS;

public class SymboleDeFonction extends Symbole {

    private int nbParametres;
    /**
     * Instancie un symbole de fonction à ranger dans la table des symboles
     *
     */
    public SymboleDeFonction(int nbParametres) {
        super("fonction");
        this.nbParametres = nbParametres;
    }

    public int getNbParametres(){
        return nbParametres;
    }
}
