package yal.arbre.gestionnaireTDS;

import yal.exceptions.AjoutTDSException;

import java.util.HashMap;
import java.util.Iterator;

public class TableLocale implements Iterable<Entree> {

    private TableLocale tableEnglobante;
    private HashMap<Entree, Symbole> table;
    private int numBloc;
    private int cptDepl;

    public TableLocale(TableLocale tableEnglobante, int numBloc){
        this.tableEnglobante = tableEnglobante;
        table = new HashMap<>();
        this.numBloc = numBloc;
        cptDepl = 0;
    }

    public TableLocale getTableEnglobante() {
        return tableEnglobante;
    }

    public void ajouter(Entree e, Symbole s) throws AjoutTDSException{
        if(table.containsKey(e)){
            throw new AjoutTDSException("Double déclaration de la"+s.getType()+e.getIdf());
        }
        if(s.getType().equals("entier") && numBloc != TDS.getInstance().getRacine().numBloc){
            cptDepl -= 4;
        } else if(numBloc != TDS.getInstance().getRacine().numBloc){
            cptDepl += 4;
        }
        table.put(e,s);
    }

    public boolean contient(Entree e){
        return table.containsKey(e);
    }

    public Symbole identifier(Entree e){
        Symbole res;
        try{
            res = table.get(e);
        } catch (NullPointerException exception){
            res = tableEnglobante.identifier(e);
        }
        return res;
    }

    public int getNumBloc() {
        return numBloc;
    }

    public int getCptDepl() {
        return cptDepl;
    }

    @Override
    public Iterator<Entree> iterator() {
        return table.keySet().iterator();
    }
}
