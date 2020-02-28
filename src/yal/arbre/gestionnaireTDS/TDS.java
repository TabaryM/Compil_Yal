package yal.arbre.gestionnaireTDS;

import java.util.HashMap;
import java.util.Map;

public class TDS {
    private static TDS instance;
    private HashMap<Entree, Symbole> table;
    // TODO : on ajoute les fonctions dans la tds, avec un fonction qui verifie si c'est une fonction
    private HashMap<Entree,String> fonctions;
    private int cpt;

    /**
     * Instancie la table des symboles
     */
    private TDS(){
        table = new HashMap<>();
        cpt = 0;
        fonctions = new HashMap<>();
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
            throw new Exception("Double déclaration de la "+s.getType()+e.getIdf());
        } else {
            /*
            if(s.getType().equals("fonction")){
                fonctions.put(e, )
            }
            */
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

    //TODO : Déclaration de fonction
    public void ajoutFonction(Entree idf,String content) {
        if (!fonctions.containsKey(idf)){
            fonctions.put(idf,content);
        }
    }

    public String getFonctions(){
        StringBuilder stringBuilder = new StringBuilder();
        for(Map.Entry<Entree, String> fonction : fonctions.entrySet()) {
            stringBuilder.append(fonction.getValue());
        }
        return stringBuilder.toString();
    }
}