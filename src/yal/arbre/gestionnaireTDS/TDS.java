package yal.arbre.gestionnaireTDS;

import yal.exceptions.AjoutTDSException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TDS implements Iterable<TableLocale> {
    // collection de TDS
    private TableLocale racine;
    private TableLocale tableCourante;
    private ArrayList<TableLocale> tables;

    private static TDS instance;
    private int cptNumBloc;

    /**
     * Instancie la table des symboles
     */
    private TDS(){
        cptNumBloc = 0;
        tables = new ArrayList<>();
        racine = new TableLocale(null, 0);
        tableCourante = racine;
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
     * Créer une nouvelle table dans un nouveau bloc
     */
    public void entreeBloc(){
        tableCourante = new TableLocale(racine, cptNumBloc);
        cptNumBloc++;
        tables.add(tableCourante);
    }

    /**
     * On sort d'un bloc et on met à jour la table courante
     */
    public void sortieBloc(){
        tableCourante = tableCourante.getTableEnglobante();
    }

    /**
     * Ajoute un symbole lié à une entrée dans la table des symboles
     * @param e l'entrée du symbole dans la table
     * @param s le symbole
     * @throws AjoutTDSException Si le symbole est déjà déclaré
     */
    public void ajouter(Entree e, Symbole s) throws AjoutTDSException {
        System.out.println("Ajout de : "+e+", "+s);
        tableCourante.ajouter(e, s);
        /*
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
            table.put(e, s);
            if(s.getType().equals("entier")){
                cpt -= 4;       // Pour le moment on fait que ça,  car il n'y a que des entiers
            }
        }
        */
    }

    /**
     * Retourne le symbole associé à une entrée de la table des symboles
     * @param e entrée lié au symbole recherché
     * @return table.get(e)
     */
    public Symbole identifier(Entree e){
        return tableCourante.identifier(e);
    }

    /**
     * Retourne la valeur du compteur de déplacement de la table courrante
     * @return int tableCourante.getCptDepl()
     */
    public int getDepl() {
        return tableCourante.getCptDepl();
    }

    public TableLocale getRacine() {
        return racine;
    }

    public int getNumBloc() {
        return cptNumBloc;
    }

    @Override
    public Iterator<TableLocale> iterator() {
        return tables.iterator();
    }

    public TableLocale getTableCourrante() {
        return tableCourante;
    }
}
