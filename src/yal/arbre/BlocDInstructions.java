package yal.arbre;

import yal.arbre.gestionnaireTDS.ErreurSemantique;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 21 novembre 2018
 *
 * @author brigitte wrobel-dautcourt
 */

public class BlocDInstructions extends ArbreAbstrait implements Iterable<ArbreAbstrait>{
    
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

    @Override
    public boolean contientRetourne() {
        boolean res = false;

        Iterator<ArbreAbstrait> iterator = programme.iterator();
        ArbreAbstrait courrant;

        while(iterator.hasNext() && !res){
            courrant = iterator.next();
            res = courrant.contientRetourne();
        }
        return res;
    }

    public int getNbInstructions(){
        return programme.size();
    }

    @Override
    public Iterator<ArbreAbstrait> iterator() {
        return programme.iterator();
    }
}
