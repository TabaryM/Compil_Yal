package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;

import java.util.HashMap;

public class SymboleDeFonction extends Symbole {

    private HashMap<Entree, Symbole> tdsLocale;

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
        tdsLocale = new HashMap<>();
    }

    public int getNbParametres(){
        return nbParametres;
    }

    public String toMIPS(){
        return instructions.toMIPS();
    }

    public void ajouterVariableLocale(Entree entree, Symbole symbole) throws Exception {
        if(tdsLocale.containsKey(entree)){
            throw new Exception("Double déclaration de la variable locale : "+entree.getIdf());
        } else {
            tdsLocale.put(entree, symbole);
        }
    }
}
