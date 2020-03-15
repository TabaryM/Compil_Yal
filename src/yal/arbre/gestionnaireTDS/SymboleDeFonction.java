package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;

import java.util.HashMap;
import java.util.Iterator;

public class SymboleDeFonction extends Symbole implements Iterable<SymboleDeVariable>{

    private HashMap<Entree, SymboleDeVariable> tdsLocale;

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

    public void ajouterVariableLocale(Entree entree, SymboleDeVariable symbole) throws Exception {
        if(tdsLocale.containsKey(entree)){
            throw new Exception("Double déclaration de la variable locale : "+entree.getIdf());
        } else {
            tdsLocale.put(entree, symbole);
        }
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymboleDeFonction{");
        sb.append("tdsLocale=").append(tdsLocale);
        sb.append(", nbParametres=").append(nbParametres);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public Iterator<SymboleDeVariable> iterator() {
        return tdsLocale.values().iterator();
    }
}
