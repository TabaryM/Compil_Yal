package yal.arbre.gestionnaireTDS;

public abstract class Symbole {
    private String type;

    /**
     * Instancie un symbole à ranger dans la table des symboles
     * @param type type de l'objet lié au symbole
     */
    public Symbole(String type) {
        this.type = type;
    }

    /**
     * Retourne le type de l'objet décrit par le symbole
     * @return type
     */
    public String getType() {
        return type;
    }

}
