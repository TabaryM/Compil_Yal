package yal.arbre;

import yal.arbre.gestionnaireTDS.ErreurSemantique;
import yal.exceptions.AnalyseSemantiqueException;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * 21 novembre 2018
 *
 * @author brigitte wrobel-dautcourt
 */

public class BlocDInstructions extends ArbreAbstrait implements Iterable<ArbreAbstrait>{
    
    protected ArrayList<ArbreAbstrait> listeInstructions;

    public BlocDInstructions(int n) {
        super(n) ;
        listeInstructions = new ArrayList<>() ;
    }
    
    public void ajouter(ArbreAbstrait a) {
        listeInstructions.add(a) ;
    }
    
    @Override
    public String toString() {
        return listeInstructions.toString() ;
    }

    @Override
    public void verifier() {
        for(ArbreAbstrait ligne : listeInstructions){
            /*
            if(ligne.getClass().getSimpleName().equals("Fonction")){
                if(ligne.contientRetourne()){
                    AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                            "Instruction Retourne hors d'une fonction : ");
                    ErreurSemantique.getInstance().ajouter(exception);
                }
            }
             */
            ligne.verifier();
        }
    }

    @Override
    public String toMIPS() {
        StringBuilder stringBuilder = new StringBuilder();
        for (ArbreAbstrait ligne : listeInstructions) {
            stringBuilder.append(ligne.toMIPS());
        }
        return stringBuilder.toString();
    }

    @Override
    public boolean contientRetourne() {
        boolean res = false;

        Iterator<ArbreAbstrait> iterator = listeInstructions.iterator();
        ArbreAbstrait courrant;

        while(iterator.hasNext()){
            courrant = iterator.next();
            res |= courrant.contientRetourne(); // Passe à vrai si on rencontre un vrai
        }
        return res;
    }

    public int getNbInstructions(){
        return listeInstructions.size();
    }

    @Override
    public Iterator<ArbreAbstrait> iterator() {
        return listeInstructions.iterator();
    }
}
