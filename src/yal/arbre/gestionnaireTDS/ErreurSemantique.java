package yal.arbre.gestionnaireTDS;

import yal.exceptions.AnalyseSemantiqueException;
import yal.exceptions.ListeExceptionSem;

import java.util.ArrayList;
import java.util.Iterator;

public class ErreurSemantique implements Iterable<AnalyseSemantiqueException>{
    private static ErreurSemantique instance = new ErreurSemantique();
    private ArrayList<AnalyseSemantiqueException> erreurs;

    /**
     * Créer l'instance unique de la liste des erreurs sémantiques
     */
    private ErreurSemantique() {
        erreurs = new ArrayList<>();
    }

    /**
     * Retourne l'instance de la classe contenant la liste des erreurs sémantiques
     * @return instance
     */
    public static ErreurSemantique getInstance() {
        return instance;
    }

    /**
     * Ajoute une erreur à la liste des erreurs sémantiques
     * @param erreur la nouvelle erreur sémantique
     */
    public void ajouter(AnalyseSemantiqueException erreur){
        erreurs.add(erreur);
    }

    /**
     * Retourne l'itérateur de la liste des erreurs sémantiques
     * @return (ArrayListe).iterator()
     */
    @Override
    public Iterator<AnalyseSemantiqueException> iterator() {
        return erreurs.iterator();
    }

    /**
     * Affiche toutes les erreures enregistrées dans la liste des erreures.
     * @throws ListeExceptionSem Une erreur contenant toutes les erreurs sémantiques détectées
     */
    public void afficherErreurs() throws ListeExceptionSem {
        StringBuilder stringBuilder = new StringBuilder();
        for(AnalyseSemantiqueException erreur : erreurs){
            stringBuilder.append(erreur.getMessage());
        }
        throw new ListeExceptionSem(stringBuilder.toString());
    }

    /**
     * Vérifie si la liste des erreures est vide
     * @return (ArrayListe).isEmpty()
     */
    public boolean isEmpty(){
        return erreurs.isEmpty();
    }
}
