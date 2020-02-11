package yal.arbre.declaration;

import yal.exceptions.AnalyseSemantiqueException;

import java.util.HashMap;

public class TDS {
    private static TDS instance;
    private HashMap<Entree, Symbole> table;
    private int cpt;

    /**
     * Instancie la table des symboles
     */
    private TDS(){
        table = new HashMap<>();
        cpt = 0;
    }

    /**
     * Retourne l'instance unique du singleton
     * @return instance
     */
    public static TDS getInstance(){
        if(instance == null){
            instance = new TDS();
        }
        return instance;
    }

    /**
     * Ajoute un symbole lié à une entrée dans la table des symboles
     * @param e l'entrée du symbole dans la table
     * @param s le symbole
     * @throws Exception Si le symbole est déjà déclaré
     */
    public void ajouter(Entree e, Symbole s) throws Exception{
        if(table.containsKey(e)){
            throw new Exception("Double déclaration de la variable "+e.getIdf());
        } else {
            table.put(e, s);
            cpt -= 4;       // Pour le moment on fait que ça,  car il n'y a que des entiers
        }
    }

    /**
     * Retourne le symbole associé à une entrée de la table des symboles
     * @param e entrée lié au symbole recherché
     * @return table.get(e)
     */
    public Symbole identifier(Entree e){
        return table.get(e);
    }

    /**
     * Retourne le compteur de variable déclarées dans le programme compilé
     * @return this.cpt
     */
    public int getCpt() {
        return cpt;
    }
}
