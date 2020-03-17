package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;

import java.util.HashMap;
import java.util.Iterator;

public class SymboleDeFonction extends Symbole{
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

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymboleDeFonction{");
        sb.append("nbParametres=").append(nbParametres);
        sb.append('}');
        return sb.toString();
    }
}
