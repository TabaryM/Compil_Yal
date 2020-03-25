package yal.arbre.gestionnaireTDS;

import yal.arbre.ArbreAbstrait;
import yal.arbre.BlocDInstructions;
import yal.arbre.declaration.DeclarationEntier;
import yal.arbre.expressions.Entier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SymboleDeFonction extends Symbole{
    private int nbParametres;
    private BlocDInstructions instructions;

    public SymboleDeFonction(int nbParametres, BlocDInstructions instructions) {
        super("fonction", TDS.getInstance().getNumBloc());
        this.nbParametres = nbParametres;
        this.instructions = instructions;
    }

    public int getNbParametres(){
        return nbParametres;
    }

    public String toMIPS(){
        TDS.getInstance().entreeBloc(getNumBloc());
        StringBuilder stringBuilder = new StringBuilder();
        // On stocke l'adresse à laquelle retourner une fois la fonction finie
        stringBuilder.append("\tsw $ra, 0($sp)\t# On stocke l'adresse de retour de la fonction\n\tadd $sp, $sp, -4\n");
        // Chainage dynamique
        stringBuilder.append("\tsw $s2, 0($sp)\t# On stocke l'adresse de la base des variables locales\n\tadd $sp, $sp, -4\n");
        // empilage des variables locales
        int place = TDS.getInstance().getTableCourrante().getCptDepl();
        stringBuilder.append("\tadd $sp, $sp, ");
        stringBuilder.append(place);
        stringBuilder.append("\t# Emplacement mémoire pour les variables locales\n");

        stringBuilder.append("\t# Initialisation des variables localas à 0\n");
        DeclarationEntier declarationEntier;
        for(Entree entree : TDS.getInstance().getTableCourrante()){
            SymboleDeVariable symboleDeVariable = ((SymboleDeVariable) TDS.getInstance().identifier(entree));
            stringBuilder.append("\tli, $v0, 0\n");
            stringBuilder.append("\tsw, $v0, ");
            stringBuilder.append(symboleDeVariable.getDepl()-8);
            stringBuilder.append("($s2)\n");
        }

        // Assigner les paramètres à leur position
        for(int i = 0; i < nbParametres; i++){
            stringBuilder.append("\tlw $v0, ");
            stringBuilder.append(i*4+4);
            stringBuilder.append("($s2)\t#On récupère la valeur du paramètre effectif\n");

            stringBuilder.append("\tsw $v0, ");
            stringBuilder.append(-((nbParametres-i)*4)-4);
            stringBuilder.append("($s2)\t#On récupère la valeur du paramètre effectif\n");
        }

        // Liste d'instructions
        stringBuilder.append(instructions.toMIPS());
        TDS.getInstance().sortieBloc();

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
