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
        tables.add(tableCourante);
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
    public int entreeBloc(){
        tableCourante = new TableLocale(tableCourante, cptNumBloc);
        cptNumBloc++;
        tables.add(tableCourante);
        return cptNumBloc;
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
        tableCourante.ajouter(e, s);
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
        return tableCourante.getNumBloc();
    }

    public void entreeBloc(int numBloc){
        tableCourante = tables.get(numBloc);
    }

    @Override
    public Iterator<TableLocale> iterator() {
        return tables.iterator();
    }

    public TableLocale getTableCourrante() {
        return tableCourante;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TDS{");
        sb.append("tables=").append(tables);
        sb.append('}');
        return sb.toString();
    }
}
