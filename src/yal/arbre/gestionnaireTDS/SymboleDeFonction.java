package yal.arbre.gestionnaireTDS;

import yal.arbre.BlocDInstructions;
import yal.arbre.expressions.Entier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SymboleDeFonction extends Symbole{
    private int nbParametres;
    private BlocDInstructions instructions;
    private BlocDInstructions variablesLocales;
    private ArrayList<Entier> parametres;

    public SymboleDeFonction(int nbParametres, BlocDInstructions instructions, ArrayList<Entier> parametres, BlocDInstructions variablesLocales) {
        super("fonction", TDS.getInstance().getNumBloc());
        this.nbParametres = nbParametres;
        this.instructions = instructions;
        this.variablesLocales = variablesLocales;
        this.parametres = parametres;
    }

    public int getNbParametres(){
        return nbParametres;
    }

    public String toMIPS(){
        StringBuilder stringBuilder = new StringBuilder();
        // On stocke l'adresse à laquelle retourner une fois la fonction finie
        stringBuilder.append("\t# On stocke l'adresse de retour de la fonction\n");
        stringBuilder.append("\tsw $ra, 0($sp)\n\tadd $sp, $sp, -4\n");
        // Chainage dynamique
        stringBuilder.append("\tsw $s2, 0($sp)\n\tadd $sp, $sp, -4\n");
        // empilage des variables locales

        // Liste d'instructions
        stringBuilder.append(instructions.toMIPS());
        return stringBuilder.toString();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("SymboleDeFonction{");
        sb.append("nbParametres=").append(nbParametres);
        sb.append('}');
        return sb.toString();
    }
}
