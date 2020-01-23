package yal.arbre.declaration;

import java.util.HashMap;

public class TDS {
    private static TDS instance;
    private HashMap<Entree, Symbole> table;
    private int cpt;

    private TDS(){
        table = new HashMap<Entree, Symbole>();
        cpt = 0;
    }

    public static TDS getInstance(){
        if(instance == null){
            instance = new TDS();
        }
        return instance;
    }

    public void ajouter(Entree e, Symbole s){

    }

    public Symbole identifier(Entree e){
        return table.get(e);
    }
}
