package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;

public class SymboleDeFonction extends Symbole {

    private int nbParametres;
    private BlocDInstructions instructions;

    /**
     * Instancie un symbole de fonction à ranger dans la table des symboles
     *
     */
    public SymboleDeFonction(int nbParametres, BlocDInstructions instructions) {
        super("fonction");
        this.nbParametres = nbParametres;
        this.instructions = instructions;
    }

    public int getNbParametres(){
        return nbParametres;
    }

    public String toMIPS(){
        return instructions.toMIPS();
    }
}
