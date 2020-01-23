package yal.arbre.declaration;

import yal.exceptions.AnalyseSemantiqueException;
import yal.exceptions.ListeExceptionSem;

import java.util.ArrayList;
import java.util.Iterator;

public class ErreurSemantique implements Iterable<AnalyseSemantiqueException>{
    private static ErreurSemantique instance = new ErreurSemantique();
    private ArrayList<AnalyseSemantiqueException> erreurs;

    private ErreurSemantique() {
        erreurs = new ArrayList<>();
    }

    public static ErreurSemantique getInstance() {
        return instance;
    }

    public void ajouter(AnalyseSemantiqueException erreur){
        erreurs.add(erreur);
    }

    @Override
    public Iterator<AnalyseSemantiqueException> iterator() {
        return erreurs.iterator();
    }

    public void afficherErreurs() throws ListeExceptionSem {
        StringBuilder stringBuilder = new StringBuilder();
        for(AnalyseSemantiqueException erreur : erreurs){
            stringBuilder.append(erreur.getMessage());
        }
        throw new ListeExceptionSem(stringBuilder.toString());
    }

    public boolean isEmpty(){
        return erreurs.isEmpty();
    }
}
