package yal.arbre;

import yal.arbre.declaration.ErreurSemantique;
import yal.arbre.declaration.TDS;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * 21 novembre 2018
 *
 * @author brigitte wrobel-dautcourt
 */

public class BlocDInstructions extends ArbreAbstrait {
    
    protected ArrayList<ArbreAbstrait> programme ;

    public BlocDInstructions(int n) {
        super(n) ;
        programme = new ArrayList<>() ;
    }
    
    public void ajouter(ArbreAbstrait a) {
        programme.add(a) ;
    }
    
    @Override
    public String toString() {
        return programme.toString() ;
    }

    @Override
    public void verifier() {
        for(ArbreAbstrait ligne : programme){
            ligne.verifier();
        }
        if(!ErreurSemantique.getInstance().isEmpty()){
            ErreurSemantique.getInstance().afficherErreurs();
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ArbreAbstrait ligne : programme) {
            stringBuilder.append(ligne.toMIPS());
        }
        return stringBuilder.toString();
    }

    public int getNbInstructions(){
        return programme.size();
    }
}
