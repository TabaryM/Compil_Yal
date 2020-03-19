package yal.arbre.gestionnaireTDS;

public abstract class Symbole {
    private String type;
    private int numBloc;

    /**
     * Instancie un symbole à ranger dans la table des symboles
     * @param type type de l'objet lié au symbole
     */
    public Symbole(String type, int numBloc) {
        this.type = type;
        this.numBloc = numBloc;
    }

    /**
     * Retourne le type de l'objet décrit par le symbole
     * @return type
     */
    public String getType() {
        return type;
    }

    public void setNumBloc(int numBloc){
        this.numBloc = numBloc;
    }

    public int getNumBloc(){
        return numBloc;
    }

}
