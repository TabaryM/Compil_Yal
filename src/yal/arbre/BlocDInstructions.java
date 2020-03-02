package yal.arbre;

import yal.arbre.expressions.Fonction;
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
            if(ligne.getClass().getSimpleName().equals("Fonction")){
                if(!ligne.contientRetourne(true)){
                    AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                            "Instruction Retourne manquante dans la fonction : "+ligne.toString());
                    ErreurSemantique.getInstance().ajouter(exception);
                }
            } else {
                if(ligne.contientRetourne(false)){
                    AnalyseSemantiqueException exception = new AnalyseSemantiqueException(getNoLigne(),
                            "Instruction Retourne hors d'une fonction : ");
                    ErreurSemantique.getInstance().ajouter(exception);
                }
            }
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
    public boolean contientRetourne(boolean dansUneFonction) {
        boolean res = false;

        Iterator<ArbreAbstrait> iterator = programme.iterator();
        ArbreAbstrait courrant;

        while(iterator.hasNext()){
            courrant = iterator.next();
            res = courrant.contientRetourne(dansUneFonction);
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
