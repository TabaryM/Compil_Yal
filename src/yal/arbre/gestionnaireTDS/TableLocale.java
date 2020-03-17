package yal.arbre.gestionnaireTDS;

import java.util.HashMap;
import java.util.Iterator;

public class TableLocale implements Iterable<Entree> {

    private TableLocale tableEnglobante;
    private HashMap<Entree, Symbole> table;
    private int numBloc;

    int cptDepl;

    public TableLocale(TableLocale tableEnglobante, int numBloc){
        this.tableEnglobante = tableEnglobante;
        table = new HashMap<>();
        this.numBloc = numBloc;
        cptDepl = 0;
    }

    public TableLocale getTableEnglobante() {
        return tableEnglobante;
    }

    public void ajouter(Entree e, Symbole s) throws Exception{
        if(table.containsKey(e)){
            throw new Exception("Double déclaration de la"+s.getType()+e.getIdf());
        }
        if(s.getType().equals("entier")){
            cptDepl -=4;
        }
        table.put(e,s);
    }

    public boolean contient(Entree e){
        return table.containsKey(e);
    }

    public Symbole identifier(Entree e){
        return table.get(e);
    }

    @Override
    public Iterator<Entree> iterator() {
        return table.keySet().iterator();
    }
}
