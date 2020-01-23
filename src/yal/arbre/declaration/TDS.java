package yal.arbre.declaration;

import yal.exceptions.AnalyseSemantiqueException;

import java.util.HashMap;

public class TDS {
    private static TDS instance;
    private HashMap<Entree, Symbole> table;
    private int cpt;

    private TDS(){
        table = new HashMap<>();
        cpt = 0;
    }

    public static TDS getInstance(){
        if(instance == null){
            instance = new TDS();
        }
        return instance;
    }

    public void ajouter(Entree e, Symbole s) throws Exception{
        if(table.containsKey(e)){
            throw new Exception("Double déclaration de la variable "+e.getIdf());
        } else {
            table.put(e, s);
        }
    }

    public Symbole identifier(Entree e){
        return table.get(e);
    }

    public int getCpt() {
        return cpt;
    }
}
