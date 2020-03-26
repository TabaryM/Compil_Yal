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
        if(s.getType().equals("entier")){
            cptDepl -= 4;
        }
        table.put(e,s);
    }

    public boolean contient(Entree e){
        return table.containsKey(e);
    }

    public Symbole identifier(Entree e){
        Symbole res;
        res = table.get(e);
        if(res == null && tableEnglobante != null){
            res = tableEnglobante.identifier(e);
        }
        return res;
    }

    public int getNumBloc() {
        return numBloc;
    }

    public void setNumBloc(int numBloc) {
        this.numBloc = numBloc;
        for(Symbole symbole : table.values()){
            symbole.setNumBloc(numBloc);
        }
    }

    public int getCptDepl() {
        return cptDepl;
    }

    public int getNbVariableLocales() {
        return table.size();
    }

    @Override
    public Iterator<Entree> iterator() {
        return table.keySet().iterator();
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("TableLocale{");
        sb.append("table=").append(table);
        sb.append(", numBloc=").append(numBloc);
        sb.append(", cptDepl=").append(cptDepl);
        sb.append('}');
        return sb.toString();
    }
}
