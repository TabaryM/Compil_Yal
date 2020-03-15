package yal.arbre.gestionnaireTDS;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TDS implements Iterable<Entree> {
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
        // Si la table contient déjà l'entrée actuelle
        if(table.containsKey(e)){
            if(s.getType().equals("fonction")){
                Symbole symboleDeFonction = table.get(e);
                if(((SymboleDeFonction)symboleDeFonction).getNbParametres() == ((SymboleDeFonction) s).getNbParametres()){
                    throw new Exception("Double déclaration de la fonction"+e.getIdf()+" avec "+((SymboleDeFonction) s).getNbParametres()+" parametres");
                }
            } else {
                throw new Exception("Double déclaration de la "+s.getType()+e.getIdf());
            }
        } else {
            System.out.println("On ajoute l'entrée "+e.getIdf()+" associée au symbole "+s);
            table.put(e, s);
            if(s.getType().equals("entier")){
                cpt -= 4;       // Pour le moment on fait que ça,  car il n'y a que des entiers
            }
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

    @Override
    public Iterator<Entree> iterator() {
        return table.keySet().iterator();
    }
}